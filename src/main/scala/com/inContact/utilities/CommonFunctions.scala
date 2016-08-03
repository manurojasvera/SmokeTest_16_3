// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import java.net.ServerSocket
import com.inContact.objects.TfsWorkItem

/**
  * Class to centralize commonly used functions throughout all of our base specs
  * @param creation passed in to validate creation in unit test
  */
class CommonFunctions(creation: Boolean) {

  private val created = creation
  /**
    * Allows you to generate the TFS Urls needed for our com.inContact.specs
    *
    * @param tfsId id of the tfs work item you want to link to
    * @param tfsItemName work item type that you are linking to
    * @return url string for tfs item
    */
  def generateTfsMarkup(tfsId: Int, tfsItemName: TfsWorkItem.Value): String = {
    val tfsUrl = "http://corptfsapp01:8080/tfs/DefaultCollection/inContact"
    var tfsString = ""
    if (tfsItemName.equals(TfsWorkItem.UserStory)) {
      tfsString = "User Story"
    }
    else {
      tfsString = tfsItemName.toString
    }
    "<a href=\"%s/_workitems/edit/%d\" target=\"_blank\">***TFS-%d %s***</a>".format(tfsUrl, tfsId, tfsId, tfsString)
  }

  /**
    * Generate the result markup string for you with custom border and bold text
    *
    * @param resultString text that you want to appear in the result field
    * @return string with correct markup for result notification
    */
  def generateResultMarkup(resultString: String): String = {
    "Results: <div style='border: 3px steelblue; border-style: inset; padding: 5px'>***%s***</div>".format(resultString)
  }

  /**
    * Generate doc markup for docs made
    *
    * @param docUrl url to the doc
    * @param docUrlTitle title of the doc
    * @param extraText extra text that will be added to url
    * @return string with doc markup
    */
  def generateDocMarkup(docUrl: String, docUrlTitle: String, extraText: String): String = {
    "<b><a href=\"http://mojo/devops/Shared Documents/Orchestration Service/%s\" target=\"_blank\">%s</a></b><i>%s</i>".format(docUrl, docUrlTitle, extraText)
  }

  /**
    * Get an open socket on the system to use for many different things
    * @return socket that is available to use
    */
  def getOpenSocket: Int = {
    val socket = new ServerSocket(0)
    val freePort = socket.getLocalPort
    socket.close()
    freePort
  }

  /**
    * Helper to print values to the console during test runs for debugging purposes
    * @param valueToPrint value that you want to print
    */
  def printToConsole(valueToPrint: String): Unit = {
    println(valueToPrint)
  }

  /**
    * Override toString method for class
    * @return string
    */
  override def toString: String = {
    "CommonFunctions Created %b".format(created)
  }

}
