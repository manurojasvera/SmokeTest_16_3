// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

class ApiEndpoints {

  /**
    * app-orchestration-scripting endpoints
    */
  val getScriptsList = "/listscripts/%d"
  val getScriptRun = "/getrun/%d/%d"
  val getStatisticsRaw = "/statistics/raw"
  val getStatistics = "/statistics"
  val getScript = "/getscript/%d/%s"

  val traceScriptRun = "/tracerun/%d/%d"
  val startScript = "/startscript/%s/%d/%d"
  val resumeScript = "/resumerun/%d/%d/%b/%s"
  val saveScript = "/savescript/%d/%s"
  val validateScript = "/validatescriptjson/"

  val deleteScript = "/deletescript/%d/%s"
}
