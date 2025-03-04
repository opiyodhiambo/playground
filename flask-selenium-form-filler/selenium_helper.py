from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service


def set_up_driver():
    options = Options()
    options.add_argument("--headless") # Helps it run without a UI
    options.add_argument("--no-sandbox")
    options.add_argument("disable-dev-shm-usage")
    options.add_argument("--window-size=1920,1080")

    service = Service("/usr/bin/chromedriver")
    driver = webdriver.Chrome(service=service, options=options)
    return driver
