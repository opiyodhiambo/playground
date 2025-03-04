import logging
import time

from flask import Flask, request, jsonify
from selenium.webdriver.support.wait import WebDriverWait

from selenium_helper import set_up_driver

app = Flask(__name__)


def scrape_virenta():
    print("Starting virenta scrapper...")
    selenium_driver = None
    try:
        selenium_driver = set_up_driver()
        selenium_driver.get("https://google.com/")
        WebDriverWait(selenium_driver, 10).until(lambda d: d.title)
        title = selenium_driver.title
        logging.info(f"Page title: {title if title else 'Title not found'}")

    except Exception as e:
        logging.error(f"Error during scraping: {str(e)}")
        title = "Error occurred"

    finally:
        if selenium_driver:
            selenium_driver.quit()

    print(f"title :: {title}")
    return title


@app.route('/submit', methods=['GET'])
def submit_form():
    print("Received request. Scraping Virenta Africa website...")
    result = scrape_virenta()
    return jsonify({"result": result})


if __name__ == '__main__':
    print("Starting Flask app....")
    app.run(debug=True)
