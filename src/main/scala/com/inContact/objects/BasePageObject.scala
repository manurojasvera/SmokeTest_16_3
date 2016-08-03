// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.objects

import java.util

import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.selenium.Page

/**
  * Base class for our PageObjects that contain a lot of base methods for interacting with the interface
  * @param webDriver webDriver is the current browser session that you have started
  */
class BasePageObject(webDriver: WebDriver) extends Page {

  /** WebDriver that contains all info about your current browser */
  implicit val driver = webDriver
  /** Default beginning URI for the orchestration pages */
  val url = "http://localhost:9000"
  val defaultWaitTime = 10

  /**
    * Wait for the time input
    * @param seconds time for the driver to wait
    */
  def wait(seconds: Int): Unit = {
    new WebDriverWait(driver, seconds)
  }

  /**
    * Wait for the element to be clickable for the amount of time input
    * @param seconds time for the driver to wait
    * @param by element for the driver to wait to become clickable
    * @return whether or not the function was successful
    */
  def waitForElementToBeClickable(by: By, seconds: Int): Boolean = {
    var result = false
    try {
      new WebDriverWait(driver, seconds).until(ExpectedConditions.elementToBeClickable(driver.findElement(by)))
      result = true
    }
    catch {
      case e: Exception => result = false
    }
    result
  }

  /**
    * Wait for the element to be visible on the page for the amount of time input
    * @param seconds time for the driver to wait
    * @param by element for the driver to wait to become visible
    * @return whether or not the function was successful
    */
  def waitForElementToBeVisible(by: By, seconds: Int): Boolean = {
    var result = false
    try {
      new WebDriverWait(driver, seconds).until(ExpectedConditions.visibilityOfElementLocated(by))
      result = true
    }
    catch {
      case e: Exception => result = false
    }
    result
  }

  /**
    * Wait for the element to have text present for the amount of time input
    * @param seconds time for the driver to wait
    * @param by element for the driver to check text in
    * @param text text that you are waiting for in the element
    * @return whether or not the function was successful
    */
  def waitForTextToBePresent(by: By, text: String, seconds: Int): Boolean = {
    var result = false
    try {
      new WebDriverWait(driver, seconds).until(ExpectedConditions.textToBePresentInElement(driver.findElement(by), text))
      result = true
    }
    catch {
      case e: Exception => result = false
    }
    result
  }

  /**
    * Set the text of the element
    * @param text text that you want to send to the element
    * @param by selenium By you want to use to find the element
    * @return whether or not the function was successful
    */
  def setInputText(text: String, by: By): Boolean = {
    var result = waitForElementToBeClickable(by, defaultWaitTime)
    try {
      driver.findElement(by).sendKeys(text)
      result = true
    }
    catch {
      case e: Exception => result = false
    }
    result
  }

  /**
    * Click the element
    * @param by selenium By you want to use to find the element
    * @return whether or not the function was successful
    */
  def clickElement(by: By): Boolean = {
    var result = waitForElementToBeClickable(by, defaultWaitTime)
    try {
      driver.findElement(by).click()
      result = true
    }
    catch {
      case e: Exception => result = false
    }
    result
  }

  /**
    * Click the checkbox depending on the current selected status of the checkbox and the boolean passed in
    * @param checkIt true - you want to check if the checkbox is not checked, if so check it
    *                false - you want to check if the checkbox is checked, if so un-check it
    * @param by selenium By you want to use to find the element
    * @return whether or not the function was successful
    */
  def clickCheckbox(checkIt: Boolean, by: By): Boolean = {
    var result = waitForElementToBeClickable(by, defaultWaitTime)
    try {
      val isChecked = validateElementSelected(by)
      if (isChecked && !checkIt) {
        result = clickElement(by)
      }
      else if (!isChecked && checkIt) {
        result = clickElement(by)
      }
    }
    catch {
      case e: Exception => result = false
    }
    result
  }

  /**
    * Clear the elements text
    * @param by selenium By you want to use to find the element
    * @return whether or not the function was successful
    */
  def clearElement(by: By): Boolean = {
    var result = waitForElementToBeVisible(by, defaultWaitTime)
    try {
      driver.findElement(by).clear()
      result = true
    }
    catch {
      case e: Exception => result = false
    }
    result
  }

  /**
    * Get the elements text
    * @param by selenium By you want to use to find the element
    * @return String - elements text
    */
  def getElementText(by: By): String = {
    waitForElementToBeVisible(by, defaultWaitTime)
    driver.findElement(by).getText
  }

  /**
    * Get the element
    * @param by selenium By you want to use to find the element
    * @return WebElement - element you were searching for
    */
  def getElement(by: By): WebElement = {
    waitForElementToBeVisible(by, defaultWaitTime)
    driver.findElement(by)
  }

  /**
    * Get a list of elements with the same selenium by args from within the element provided
    * (e.g.) many elements with "theElement" className within the table element passed in
    * @param element element that you want to search within for the common values
    * @return List[WebElement] - list of elements that you were searching for
    */
  def getElements(element: WebElement, by: By): util.Iterator[WebElement] = {
    element.findElements(by).iterator()
  }

  /**
    * Refresh the page if needed
    * @return whether or not the function was successful
    */
  def refreshPage(): Unit = {
    driver.navigate().refresh()
  }

  /**
    * Validate the element is displayed on the page
    * @param by selenium By you want to use to find the element
    * @return Boolean - Whether or not element is displayed
    */
  def validateElementIsDisplayed(by: By): Boolean = {
    driver.findElement(by).isDisplayed
  }

  /**
    * Validate the element is enabled on the page
    * @param by selenium By you want to use to find the element
    * @return Boolean - Whether or not the element is enabled
    */
  def validateElementIsEnabled(by: By): Boolean = {
    driver.findElement(by).isEnabled
  }

  /**
    * Validate the element is selected
    * @param by selenium By you want to use to find the element
    * @return Boolean - Whether or not the element is selected
    */
  def validateElementSelected(by: By): Boolean = {
    driver.findElement(by).isSelected
  }

  /**
    * Navigate to url provided and if validateNavigation is true validate you successfully navigated
    * to the the expectedUrl
    * @param url url that you want to navigate to
    * @param expectedUrl if validateNavigation check that driver.CurrentUfl = expectedUrl
    * @param validateNavigation whether or not you want to validate navigation was successful
    * @return Boolean - True if validateNavigation = false else if validateNavigation = true then check that you're on
    *         the correct page
    */
  def navigateToPage(url: String, expectedUrl: String = "", validateNavigation: Boolean = false): Boolean = {
    var result = true
    driver.get(url)
    if (validateNavigation){
      result = driver.getCurrentUrl.equals(expectedUrl)
    }
    result
  }

  /**
    * Switch window handles and return back the url of the page you switched to
    * @param currentWindowHandle handle of the window that you're on now and don't want to switch to (driver.getWindowHandle)
    * @return url of the window that you are now on
    */
  def switchWindowHandles(currentWindowHandle: String): String = {
    while(driver.getWindowHandles.size() < 2) {
      wait(2)
    }
    val windowHandles = driver.getWindowHandles
    windowHandles.remove(currentWindowHandle)
    val otherAvailableWindow = windowHandles.toArray.last.toString
    driver.switchTo().window(otherAvailableWindow)
    driver.getCurrentUrl
  }

}