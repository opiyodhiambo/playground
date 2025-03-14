const SPREAD_SHEET_ID = 'SHEET_ID';
const SLIDES_ID = 'SLIDES_ID';
const FOLDER_ID = "FOLDER_ID";
const BATCH_SIZE = 500;

function generatePresentationData() {

  clearExistingTriggers(); // Ensure no duplicate triggers

  let scriptProperties = PropertiesService.getScriptProperties();
  let lastProcessed = parseInt(scriptProperties.getProperty("lastProcessed") || "1", 10);
  
  let spreadSheet = SpreadsheetApp.openById(SPREAD_SHEET_ID);
  let slidesTemplate = DriveApp.getFileById(SLIDES_ID);

  let googleSlideMappingSheet = spreadSheet.getSheetByName("Google Slide mapping");
  let countySheet = spreadSheet.getSheetByName("Data_county_level");
  let customerSheet = spreadSheet.getSheetByName("Data_customer_level");
  let customerMonthlySheet = spreadSheet.getSheetByName("customer_monthly");

  // Here, we exclude the first two rows (header + title row).
  let headers = customerSheet.getDataRange().getValues()[1];
  let customers = customerSheet.getDataRange().getValues().slice(2); 
  let totalRows = customers.length;

  let customerIdIndex = headers.indexOf("id RCU");
  let departmentNameIndex = headers.indexOf("Département");
  let departmentCodeIndex = headers.indexOf("dep code");


  Logger.log(`Processing from row ${lastProcessed} to ${Math.min(lastProcessed + BATCH_SIZE, totalRows)}`);
  
  // Processing the customers in batches
  for (let i = lastProcessed - 1; i < Math.min(lastProcessed - 1 + BATCH_SIZE, totalRows); i++) {
    let customerRow = customers[i];
    let customerId = customerRow[customerIdIndex]; 
    let departmentName = customerRow[departmentNameIndex];
    let departmentCode = customerRow[departmentCodeIndex];

    let slides = createNewCustomerSlide(slidesTemplate, customerId, departmentName);

    updateSlide19(departmentCode, departmentName, countySheet, slides);
    updateSlide20(departmentCode, countySheet, slides);
    updateSlide23(customerId, departmentCode, countySheet, customerSheet, slides);
    updateSlide24(departmentCode, countySheet, slides);
    updateSlide25(customerId, customerMonthlySheet, slides);
    updateSlide26(customerId, googleSlideMappingSheet, customerMonthlySheet, slides);
  }

  lastProcessed += BATCH_SIZE;
  if (lastProcessed < totalRows) {
    scriptProperties.setProperty("lastProcessed", lastProcessed);
    ScriptApp.newTrigger("generatePresentationData")
      .timeBased()
      .after(1 * 60 * 1000) // Execute in intervals of 1 minute to avoid google script timeout (2 minutes)
      .create();
  } else {
    scriptProperties.deleteProperty("lastProcessed"); // All done
    Logger.log("All templates processed!");

    ScriptApp.newTrigger("generatePresentationData")
      .timeBased()
      .everyWeeks(2) // Runs again in 2 weeks
      .create();
  }
}


function updateSlide19(departmentCode, departmentName, countySheet, slides) {
  Logger.log("Updating Slide 19");
  let slide = slides.getSlides()[18];

  let departmentRow = extractDepartmentRow(departmentCode, countySheet);
  if (!departmentRow) return;
  
  let visits = countySheet.getRange(`D${departmentRow}`).getValue();
  let visitsEvol = countySheet.getRange(`I${departmentRow}`).getValue() * 100;
  let contacts = countySheet.getRange(`F${departmentRow}`).getValue();
  let contactsEvol = countySheet.getRange(`L${departmentRow}`).getValue() * 100;
  let estimations = countySheet.getRange(`G${departmentRow}`).getValue();
  let estimationsEvol = countySheet.getRange(`M${departmentRow}`).getValue() * 100;

  let shapes = slide.getShapes();

  shapes[2].getText().setText(`Etat des lieux dans ${departmentName}`);
  shapes[8].getText().setText(visits.toString());
  shapes[9].getText().setText(contacts.toString());
  shapes[10].getText().setText(estimations.toString());
  shapes[11].getText().setText(visitsEvol.toFixed() + "%");
  shapes[12].getText().setText(contactsEvol.toFixed() + "%");
  shapes[13].getText().setText(estimationsEvol.toFixed() + "%");

  Logger.log("Slide 19 update completed.");
  
}

function updateSlide20(departmentCode, countySheet, slides) {
  Logger.log("Updating Slide 20");
  let slide = slides.getSlides()[19];

  let textFinder = countySheet.getRange("A:A").createTextFinder(departmentCode).findNext();
  if (!textFinder) {
    Logger.log(`❌ Department code ${departmentCode} not found.`);
    return;
  }

  let departmentRow = extractDepartmentRow(departmentCode, countySheet);
  if (!departmentRow) return;

  let departmentName = countySheet.getRange(`B${departmentRow}`).getValue();
  let averageSales = countySheet.getRange(`N${departmentRow}`).getValue();
  let apartmentPrice = countySheet.getRange(`O${departmentRow}`).getValue();
  let apartmentPriceChange = countySheet.getRange(`P${departmentRow}`).getValue();
  let mansionPrice = countySheet.getRange(`Q${departmentRow}`).getValue();
  let mansionPriceChange = countySheet.getRange(`R${departmentRow}`).getValue();

  let shapes = slide.getShapes();

  shapes[3].getText().setText(`Baromètre des prix dans ${departmentName}`);
  shapes[10].getText().setText(averageSales.toString());
  shapes[13].getText().setText(averageSales.toString());
  shapes[8].getText().setText(apartmentPrice.toLocaleString() + " €");
  shapes[9].getText().setText((apartmentPriceChange * 100).toFixed(3) + " %");
  shapes[11].getText().setText(mansionPrice.toLocaleString() + " €");
  shapes[12].getText().setText((mansionPriceChange * 100).toFixed(3) + " %");

   Logger.log("Slide 20 update completed.");

}

function updateSlide23(customerId, departmentCode, countySheet, customerSheet, slides) {
  Logger.log("Updating Slide 23");
  let slide = slides.getSlides()[22];
  let tables = slide.getTables();
  if (tables.length === 0) {
    Logger.log("❌ No tables found on Slide 23.");
    return;
  }
  let table = tables[0];

  let customerRow = extractCustomerRow(customerId, customerSheet);
  if (!customerRow) return;

  let priceInDepartment = getPriceOfGoodInDepartment(departmentCode, countySheet).toFixed()
  if (priceInDepartment === null) return;

  let mainPackage = customerSheet.getRange("Q" + (customerRow + 1)).getValue() + " - " +
        customerSheet.getRange("R" + (customerRow + 1)).getValue() + " annonces";

    let subscriptionPrice = customerSheet.getRange("G" + (customerRow + 1)).getValue() + "€" 
    let discountPercentage = customerSheet.getRange("H" + (customerRow + 1)).getValue() * 100 + "%" 
    let adsPublished = customerSheet.getRange("R" + (customerRow + 1)).getValue() + 
      " en moyenne sur les 6 derniers mois";
    let customerPrice = customerSheet.getRange("AC" + (customerRow + 1)).getValue().toFixed() 
    
    let averagePriceComparison = customerPrice + "€ vs " + priceInDepartment + 
      "€ en moyenne dans votre département";

    let percentageDeviation =  calculatePriceDeviation(customerPrice, priceInDepartment).toFixed() + "%";

    let similarCustomerLeadsPerAd = getSimilarLeadsPerSales(departmentCode, countySheet).toFixed(1);
    let leadsPerAd = customerSheet.getRange("AE" + (customerRow + 1)).getValue() + " vs " +
      similarCustomerLeadsPerAd + " en moyenne dans votre département";
    let cplBuyers = customerSheet.getRange("M" + (customerRow + 1)).getValue().toFixed() + "€ Vs " + 
      customerSheet.getRange("N" + (customerRow + 1)).getValue().toFixed() + "€ clients similaires";

    table.getCell(0, 1).getText().setText(mainPackage);
    table.getCell(1, 1).getText().setText(subscriptionPrice);
    table.getCell(2, 1).getText().setText(discountPercentage);
    table.getCell(3, 1).getText().setText(adsPublished);
    table.getCell(4, 1).getText().setText(averagePriceComparison);
    table.getCell(5, 1).getText().setText(percentageDeviation);
    table.getCell(6, 1).getText().setText(leadsPerAd);
    table.getCell(7, 1).getText().setText(cplBuyers);

    Logger.log("Slide 23 Update Complete");
}

function updateSlide24(departmentCode, countySheet, slides) {
  Logger.log("Updating Slide 24");
  let slide = slides.getSlides()[23];
  let elements = slide.getPageElements();

  let packData = extractPackData(departmentCode, countySheet);
  let totalPacks = packData["Total agences"];

  elements.forEach((element, index) => {
    if (element.getPageElementType() === SlidesApp.PageElementType.SHAPE) {
      let shape = element.asShape();
      let text = shape.getText().asString();

      if (text.includes("vos confrères consomment en majorité des packs")) {
        let updatedText = `vos confrères consomment en majorité des packs ${totalPacks} etc. ce qui tire leurs performances vers le haut`;
        shape.getText().setText(updatedText);
      }

    }
    else if (element.getPageElementType() == SlidesApp.PageElementType.TABLE) {
      let table = element.asTable();

      if (index === 2) { // Pack Consumption Table
        table.getCell(2, 1).getText().setText(packData["Total Ultimmo"].toString());
        table.getCell(3, 1).getText().setText(packData["Ultimo Essentiel"].toString());
        table.getCell(4, 1).getText().setText(packData["Ultimmo Expert"].toString());
        table.getCell(5, 1).getText().setText(packData["Ultimmo Absolu"].toString());

        table.getCell(6, 1).getText().setText(packData["Total Duo"].toString());
        table.getCell(7, 1).getText().setText(packData["Duo access"].toString());
        table.getCell(8, 1).getText().setText(packData["Duo power"].toString());
        table.getCell(9, 1).getText().setText(packData["Duo success"].toString());

        table.getCell(10, 1).getText().setText(packData["Total agences"].toString());

        // Calculate & update percentages
        for (let r = 2; r < 10; r++) {
          let value = parseInt(table.getCell(r, 1).getText().asString(), 10);
          let percentage = (value / packData["Total agences"]) * 100 || 0;
          table.getCell(r, 2).getText().setText(percentage.toFixed() + "%");
        }

      } else if (index === 3) { // Options Table
        table.getCell(2, 1).getText().setText(packData["RDVQ"].toString());
        table.getCell(3, 1).getText().setText(packData["Encart pub"].toString());
        table.getCell(4, 1).getText().setText(packData["Pack Pro"].toString());
        table.getCell(5, 1).getText().setText(packData["CDP"].toString());
        table.getCell(6, 1).getText().setText(packData["Boost Extend +"].toString());

        // Calculate & update percentages
        for (let r = 2; r < 7; r++) {
          let value = parseInt(table.getCell(r, 1).getText().asString(), 10);
          let percentage = (value / packData["Total agences"]) * 100 || 0;
          table.getCell(r, 2).getText().setText(percentage.toFixed() + "%");
        }
      }
    }
  });

  Logger.log("Slide 24 Update Complete");
}

function updateSlide25(customerId, customerMonthlySheet, slides){
  Logger.log("Updating Slide 25");
  let slide = slides.getSlides()[24];

  let creditData = extractCreditData(customerId, customerMonthlySheet);
  if (creditData.length === 0) {
    Logger.log(`No credit data found for Customer: ${customerId}`);
    return;
  }

  let tables = slide.getTables();
  if (tables.length === 0) {
      return;
  }

  let table = tables[0];
  let headers = ["Décembre 2024", "Novembre 2024", "Octobre 2024", "Septembre 2024"];

  let dataMap = {};
  creditData.forEach(entry => {
      dataMap[entry.monthYear] = entry;
  });

  let totalUnusedPortion = 0;
  let count = 0;

  for (let column = 1; column <= headers.length; column++) {
    let monthHeader = headers[column - 1];  // Example: "Décembre 2024"
    let monthYearKey = convertHeaderToMonthYear(monthHeader);  // Converts it to "2024-12"

    let entry = dataMap[monthYearKey];
    if (!entry) {
      Logger.log(`No data for ${monthYearKey}`);
      continue;
    }

    let unusedPortion = entry.unusedPortion || "0%";
    let unusedCredit = entry.unusedCredit ?? "0";  
    let usedCredit = entry.usedCredit ?? "0";
    let totalCredit = entry.totalCredit ?? "0";

    table.getCell(1, column).getText().setText(unusedPortion);
    table.getCell(2, column).getText().setText(unusedCredit);
    table.getCell(3, column).getText().setText(usedCredit);
    table.getCell(4, column).getText().setText(totalCredit);

    let unusedPercentage = parseFloat(entry.unusedPortion.replace("%", ""));
    if (!isNaN(unusedPercentage)) {
      totalUnusedPortion += unusedPercentage;
      count++;
    }
  }

  let averageUnused = count > 0 ? (totalUnusedPortion / count).toFixed(0) + "%" : "XX%";
  let shapes = slide.getShapes();
    for (let shape of shapes) {
      let text = shape.getText().asString();
      if (text.includes("Vous utilisez en moyenne moins de XX% de vos boosts chaque mois.")) {
        let updatedText = text.replace("XX%", averageUnused);
        shape.getText().setText(updatedText);
        break;
      }
    }
  Logger.log("Slide 25 Update Complete");
}

function updateSlide26(customerId, googleSlideMappingSheet, customerMonthlySheet, slides) {
  Logger.log("Investigating Slide 26");
  let slide = slides.getSlides()[25];

  let lastRow = customerMonthlySheet.getLastRow();
  let customerData = []; // grouped data for each customer

  for (let row = 2; row <= lastRow; row++) {
    let currentCustomerId = customerMonthlySheet.getRange(`B${row}`).getValue();
    if (currentCustomerId !== customerId) continue;

    let monthYear = customerMonthlySheet.getRange(`A${row}`).getValue();  // Full Date

    let displays = customerMonthlySheet.getRange(`BP${row}`).getValue();
    let detailDisplays = customerMonthlySheet.getRange(`BQ${row}`).getValue();
    let alertsSent = customerMonthlySheet.getRange(`BT${row}`).getValue();
    let emailsSent = customerMonthlySheet.getRange(`AG${row}`).getValue();
    let phoneContacts = customerMonthlySheet.getRange(`AJ${row}`).getValue();
    let attractiveness = (displays !== 0) ? ((detailDisplays / displays) * 100).toFixed() + "%" : "0%";

    customerData.push({
      monthYear,
      displays,
      detailDisplays,
      alertsSent,
      emailsSent,
      phoneContacts,
      attractiveness
    });
  }

  if (customerData.length === 0) {
    Logger.log(`⚠️ No data found for Customer: ${customerId}`);
    return;
  }

  let chartData = extractChartDataForCustomer(customerData);  
  updateSlide26Chart(googleSlideMappingSheet, slide, chartData, customerId);

  let lastEntry = customerData[customerData.length - 1];
  
  Logger.log("Last entry :: " + JSON.stringify(lastEntry, null, 2));

  updateSlide26Data(
    slide, 
    lastEntry.displays, 
    lastEntry.attractiveness, 
    lastEntry.detailDisplays, 
    lastEntry.alertsSent, 
    lastEntry.emailsSent, 
    lastEntry.phoneContacts
    );

  Logger.log("Slide 26 Update Complete");
}

function createNewCustomerSlide(slidesTemplate, customerId, departmentName) {
  let newFileName = `Presentation_${customerId}_${departmentName}_${new Date().toISOString()}`;
  let newSlideFile = slidesTemplate.makeCopy(newFileName, DriveApp.getFolderById(FOLDER_ID));
  let slides = SlidesApp.openById(newSlideFile.getId());
  Logger.log(`📌 Created new presentation: ${newFileName}`);
  return slides
}

function extractPackData(departmentCode, countySheet) {
  let departmentRow = extractDepartmentRow(departmentCode, countySheet);
  if (!departmentRow) return null; // Exit if department is not found

  let packData = {
    "Ultimo Essentiel": countySheet.getRange(`AG${departmentRow}`).getValue(),
    "Ultimmo Expert": countySheet.getRange(`AH${departmentRow}`).getValue(),
    "Ultimmo Absolu": countySheet.getRange(`AI${departmentRow}`).getValue(),
    "Duo access": countySheet.getRange(`AD${departmentRow}`).getValue(),
    "Duo power": countySheet.getRange(`AE${departmentRow}`).getValue(),
    "Duo success": countySheet.getRange(`AF${departmentRow}`).getValue(),
    "RDVQ": countySheet.getRange(`AL${departmentRow}`).getValue(),
    "Encart pub": countySheet.getRange(`AN${departmentRow}`).getValue(),
    "Pack Pro": countySheet.getRange(`AJ${departmentRow}`).getValue(),
    "CDP": countySheet.getRange(`AK${departmentRow}`).getValue(),
    "Boost Extend +": countySheet.getRange(`AM${departmentRow}`).getValue()
  };

  // Calculate totals
  packData["Total Ultimmo"] = packData["Ultimo Essentiel"] + packData["Ultimmo Expert"] + packData["Ultimmo Absolu"];
  packData["Total Duo"] = packData["Duo access"] + packData["Duo power"] + packData["Duo success"];
  packData["Total agences"] = packData["Total Ultimmo"] + packData["Total Duo"];

  Logger.log("Extracted Pack Data: " + JSON.stringify(packData, null, 2));

  return packData;
}

function getPriceOfGoodInDepartment(departmentCode, countySheet) {
  let row = extractDepartmentRow(departmentCode, countySheet);
  if (!row) {
    Logger.log(`❌ Department ${departmentCode} not found in county sheet.`);
    return null;
  }
  return countySheet.getRange(`AB${row}`).getValue();

}

function getSimilarLeadsPerSales(departmentCode, countySheet) {
  let countyDepartments = countySheet.getRange("A:A").getValues().flat(); // Get all departments
  let countyRowIndex = countyDepartments.indexOf(departmentCode);

  if (countyRowIndex === -1) {
      Logger.log("❌ Department " + departmentCode + " not found in county sheet.");
      return null; 
  }
  return countySheet.getRange("W" + (countyRowIndex + 1)).getValue();
}

  
function calculatePriceDeviation(customerPrice, priceInDepartment) {
  if (priceInDepartment === 0 || priceInDepartment === null) {
      Logger.log("❌ Error: Division by zero or missing department price.");
      return "N/A";
  }
  let deviation = ((customerPrice - priceInDepartment) / priceInDepartment) * 100;
  return deviation; 
}

function extractCreditData(customerId, customerMonthlySheet) {
  Logger.log("Extracting Credit Data");

  let dataRange = customerMonthlySheet.getDataRange().getValues();
  let headers = dataRange[0]; 
  let creditData = [];

  for (let i = 1; i < dataRange.length; i++) { 
    if (dataRange[i][1] !== customerId) continue; // Column B :: Customer ID

    let dateValue = dataRange[i][0]; // Column A :: Full Date
    let year = dateValue.getFullYear();
    let month = dateValue.getMonth() + 1;

    // Filter required months
    if (!((year === 2024 && [9, 10, 11, 12].includes(month)) || (year === 2025 && month === 1))) {
      continue;
    }

    let unusedPortion = dataRange[i][headers.indexOf("BL")] * 100;
    let unusedCredit = dataRange[i][headers.indexOf("BM")];
    let usedCredit = dataRange[i][headers.indexOf("BO")];
    let totalCredit = dataRange[i][headers.indexOf("BN")];

    creditData.push({
      customerId: customerId,
      monthYear: `${year}-${month.toString().padStart(2, '0')}`,
      unusedPortion: unusedPortion.toFixed() + "%",
      unusedCredit: unusedCredit,
      usedCredit: usedCredit,
      totalCredit: totalCredit
    });
  }
  Logger.log("Successfully extracted Credit Data");
  return creditData;
}

function convertHeaderToMonthYear(header) {
    let months = {
        "Janvier": "01", "Février": "02", "Mars": "03", "Avril": "04",
        "Mai": "05", "Juin": "06", "Juillet": "07", "Août": "08",
        "Septembre": "09", "Octobre": "10", "Novembre": "11", "Décembre": "12"
    };

    let parts = header.split(" ");  // ["Décembre", "2024"]
    let month = months[parts[0]];   // Convert "Décembre" -> "12"
    let year = parts[1];            // "2024"

    return `${year}-${month}`;
}

function updateSlide26Data(slide, displays, attractiveness, detailDisplays, alertsSent, emailsSent, phoneContacts) {
  let elements = slide.getPageElements();

  // Update standard elements (Numbers)
  elements.forEach((element, index) => {
    try {
      if (element.getPageElementType() == SlidesApp.PageElementType.GROUP) {
        let group = element.asGroup();
        group.getChildren().forEach((child, childIndex) => {
          if (child.getPageElementType() == SlidesApp.PageElementType.SHAPE) {
            let shapeText = child.asShape().getText();

            switch (index) {
              case 4: // Displays
                if (childIndex === 1) shapeText.setText(displays.toString());
                break;
              case 5: // Attractiveness Rate
                if (childIndex === 1) shapeText.setText(attractiveness.toString());
                break;
              case 6: // Detail Displays
                if (childIndex === 1) shapeText.setText(detailDisplays.toString());
                break;
              case 7: // Alerts Sent
                if (childIndex === 1) shapeText.setText(alertsSent.toString());
                break;
            }
          }
        });
      }
    } catch (e) {
      Logger.log(`Error updating Element ${index}: ${e.message}`);
    }
  });

  // Update Element 8 (Emails Sent & Phone Contacts)
  let element8 = slide.getPageElements()[8];
  if (element8.getPageElementType() == SlidesApp.PageElementType.GROUP) {
    let children = element8.asGroup().getChildren();

    if (children.length === 2) {
      let emailsGroup = children[0].asGroup().getChildren();
      let phoneGroup = children[1].asGroup().getChildren();

      if (emailsGroup.length > 1 && emailsGroup[1].getPageElementType() == SlidesApp.PageElementType.SHAPE) {
        emailsGroup[1].asShape().getText().setText(emailsSent.toString());
        Logger.log("Updated emailsSent in Element 8, Group 0");
      }

      if (phoneGroup.length > 0 && phoneGroup[0].getPageElementType() == SlidesApp.PageElementType.SHAPE) {
        phoneGroup[0].asShape().getText().setText(phoneContacts.toString());
        Logger.log("Updated phoneContacts in Element 8, Group 1");
      }
    } else {
      Logger.log("Unexpected structure in Element 8.");
    }
  } else {
    Logger.log("Element 8 is not a group.");
  }

  Logger.log("Slide 26 text elements updated.");
}

function extractChartData(customerMonthlySheet) {
  let lastRow = customerMonthlySheet.getLastRow();
  let chartData = {};

  for (let row = 2; row <= lastRow; row++) {
    let customerId = customerMonthlySheet.getRange(`B${row}`).getValue(); // Customer ID
    let dateValue = customerMonthlySheet.getRange(`A${row}`).getValue(); // Date (Year-Month)

    if (!(dateValue instanceof Date)) {
      Logger.log(`Skipping row ${row}: Invalid date format.`);
      continue;
    }

    let year = dateValue.getFullYear();
    let month = dateValue.getMonth() + 1;
    let monthYearLabel = new Date(year, month - 1).toLocaleString("default", { month: "long", year: "numeric" });

    // Extract values
    let emailsSent = customerMonthlySheet.getRange(`AG${row}`).getValue(); // Emails Sent
    let phoneContacts = customerMonthlySheet.getRange(`AJ${row}`).getValue(); // Phone Contacts

    // Initialize customer if not present
    if (!chartData[customerId]) {
      chartData[customerId] = {};
    }

    // Initialize month-year if not present
    if (!chartData[customerId][monthYearLabel]) {
      chartData[customerId][monthYearLabel] = { emailsSent: 0, phoneContacts: 0 };
    }

    // Sum up values
    chartData[customerId][monthYearLabel].emailsSent += emailsSent;
    chartData[customerId][monthYearLabel].phoneContacts += phoneContacts;
  }

  // Convert to sorted array per customer
  let formattedData = {};
  Object.keys(chartData).forEach(customerId => {
    formattedData[customerId] = Object.entries(chartData[customerId])
      .map(([monthYear, values]) => ({
        monthYear,
        emailsSent: values.emailsSent,
        phoneContacts: values.phoneContacts,
      }))
      .sort((a, b) => new Date(a.monthYear) - new Date(b.monthYear))
      .slice(-12); // Keep last 12 months
  });

  return formattedData;
}

function extractChartDataForCustomer(customerEntries) {
  if (!Array.isArray(customerEntries)) {
    Logger.log("⚠️ extractChartDataForCustomer received an invalid input: " + JSON.stringify(customerEntries));
    return []; // Return an empty array instead of causing an error
  }

  return customerEntries.map(entry => ({
    monthYear: entry.monthYear,
    emailsSent: entry.emailsSent ?? 0,  // Ensure emailsSent is never null/undefined
    phoneContacts: entry.phoneContacts ?? 0 // Ensure phoneContacts is never null/undefined
  }));
}


function updateSlide26Chart(googleSlideMappingSheet, slide, chartData, customerId) {

  if (!googleSlideMappingSheet) {
    Logger.log("Sheet 'Google Slide Mapping' not found.");
    return;
  }

  if (!chartData || chartData.length === 0) {
    Logger.log(`No chart data available for Customer: ${customerId}`);
    return;
  }

  let range = googleSlideMappingSheet.getRange("A82:D95");
  let existingData = range.getValues(); // Get current table data

  let chartDataMap = {};
  chartData.forEach(entry => {
      chartDataMap[entry.monthYear] = entry;
  });

  for (var i = 2; i < existingData.length; i++) {
    let monthYear = existingData[i][1]; // Column B (L12M data)
    
    if (chartDataMap[monthYear]) {
        existingData[i][2] = chartDataMap[monthYear].emailsSent; // Update Column C
        existingData[i][3] = chartDataMap[monthYear].phoneContacts; // Update Column D
    }
  }
  range.setValues(existingData);
    
  Logger.log("Data updated successfully! Now extracting chart...");

  let charts = googleSlideMappingSheet.getCharts();

  if (charts.length === 0) {
    Logger.log("No charts found in the sheet.");
    return;
  }

  let chartImage = charts[0].getBlob();

  let images = slide.getImages();
  if (images.length === 0) {
    Logger.log("No images found in Slide 26.");
    return;
  }

  images[0].replace(chartImage);
  Logger.log("Chart image replaced successfully.");
}

function extractDepartmentRow(departmentCode, countySheet) {
  let textFinder = countySheet.getRange("A:A").createTextFinder(departmentCode).findNext();
  if (!textFinder) {
    Logger.log(`❌ Department code ${departmentCode} not found.`);
    return;
  }

  return  textFinder.getRow();
}

function extractCustomerRow(customerId, customerSheet) {
  let textFinder = customerSheet.getRange("B:B").createTextFinder(customerId).findNext();
  if (!textFinder) {
    Logger.log(`❌ Customer ID ${customerId} not found.`);
    return null;
  }
  return textFinder.getRow();
}

function clearExistingTriggers() {
  let triggers = ScriptApp.getProjectTriggers();
  triggers.forEach(trigger => {
    if (trigger.getHandlerFunction() === "generatePresentationData") {
      ScriptApp.deleteTrigger(trigger);
    }
  });
}







