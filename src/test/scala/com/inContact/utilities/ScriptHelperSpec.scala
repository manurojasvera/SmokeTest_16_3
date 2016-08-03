// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import com.inContact.objects._
import com.inContact.specs.BaseFeatureSpec
import play.api.libs.json.Json

class ScriptHelperSpec extends BaseFeatureSpec {
  info("As an automation engineer")
  info("I want the ability to build scripts and call script apis")
  info("So that I can easily execute tests")

  val feature = 0
  val userStory = 0
  val hostUri = "http://172.20.228.121"
  val successStatusCode = 200
  markup { commonFunctions.generateTfsMarkup(feature, TfsWorkItem.Feature) }

  val validScript = Json.parse(scriptHelper.getScriptJson(validJson = true, withParameters = false))
  val validScriptWithParams = Json.parse(scriptHelper.getScriptJson(validJson = true, withParameters = true))
  val invalidScript = Json.parse(scriptHelper.getScriptJson(validJson = false, withParameters = false))

  feature("Script Helper Validation") {
    scenario("I'm able to save a script using Script Helper") {
      Given("I have a valid script to save")
        // setup above
        val scriptName = "apiTest_SaveScriptLibrary"
      When("I save the script calling the api")
        val result = scriptHelper.saveScript(scriptName, defaultBusinessUnitNumber, hostUri, successStatusCode, validScript)
      Then("I get the correct response back")
        assert(result.toLowerCase() contains "saved")
        markup { commonFunctions.generateResultMarkup(s"Save Script with valid json result: $result") }
    }

    scenario("I'm able to save and start a script using Script Helper") {
      Given("I have a valid script to save/start")
        // setup above
        val scriptName = "apiTest_StartScriptLibrary"
        val saveResult = scriptHelper.saveScript(scriptName, defaultBusinessUnitNumber, hostUri, successStatusCode, validScript)
        assert(saveResult.toLowerCase() contains "saved")
      When("I save the script calling the api")
        val startResult = scriptHelper.startScript(scriptName, defaultBusinessUnitNumber, scriptHelper.getNextRunId, hostUri,
                                                   successStatusCode, Json.parse("{}"))
      Then("I get the correct response back")
        assert(startResult.toLowerCase() contains "started")
        markup { commonFunctions.generateResultMarkup(s"Save Script with valid json result: $saveResult<br/><br/>" +
                                      s"Start Script with valid json result: $startResult") }
    }

    scenario("I'm able to check the validity of a script using Script Helper expected true") {
      Given("I have a valid script to check validity")
        // setup above
      When("I check the scripts validity I get a boolean value back")
        val scriptValidation = scriptHelper.validateScript(validScriptWithParams, hostUri, successStatusCode)
      Then("I get the correct boolean value back")
        assert(scriptValidation)
        markup { commonFunctions.generateResultMarkup(s"Script Validation: $scriptValidation") }
    }

    scenario("I'm able to check the validity of a script using Script Helper expected false") {
      Given("I have a valid script to check validity")
        // setup above
      When("I check the scripts validity I get a boolean value back")
        val scriptValidation = scriptHelper.validateScript(invalidScript, hostUri, successStatusCode)
      Then("I get the correct boolean value back")
        assert(!scriptValidation)
        markup { commonFunctions.generateResultMarkup(s"Script Validation: $scriptValidation") }
    }

    scenario("I'm able to get a basic script json that is valid") {
      Given("I want a basic valid script to test")
      When("I ask script helper for a valid basic script")
        // setup above
      Then("Validate I get a valid script back")
        val validScriptResult = validScript.toString() contains
          "{" +
            "\"id\":1," +
            "\"actor\":\"Begin\"," +
            "\"prompt\":{" +
            "\"messageType\":\"BeginConfiguration\"," +
              "\"serializedMessage\":{" +
                "\"parameters\":{}" +
              "}" +
            "}," +
            "\"caption\":\"Begin\"," +
            "\"x\":1," +
            "\"y\":1" +
           "},"
        assert(validScriptResult)
        val validScriptWithParamsResult =  validScriptWithParams.toString() contains
          "\"serializedMessage\":{\"parameters\":{\"ani\":\"someAni\",\"dnis\":\"someDnis\"}}"
        assert(validScriptWithParamsResult)
        markup { commonFunctions.generateResultMarkup(s"Get Script with valid json result: $validScriptResult<br/><br/>" +
                                      s"Get Script with parameters valid json result: $validScriptWithParamsResult") }
    }

    scenario("I'm able to get a basic script json that is invalid") {
      Given("I want a basic invalid script to test")
      When("I ask script helper for an invalid basic script")
        // setup above
      Then("Validate I get an invalid script back")
        val invalidScriptResult = invalidScript.toString() contains
          "{" +
            "\"id\":1," +
            "\"actor\":\"Begin\"," +
            "\"prompt\":{" +
              "\"mesageType\":\"BeginConfiguration\"," +
              "\"serializedMessage\":{" +
                "\"parmaters\":{}" +
              "}" +
            "}," +
            "\"caption\":\"Begin\"," +
            "\"x\":1," +
            "\"y\":1" +
           "},"
        assert(invalidScriptResult)
        markup { commonFunctions.generateResultMarkup(s"Get Script with valid json result: $invalidScriptResult") }
    }

    scenario("I'm able to get a basic assign scripts json") {
      Given("I want a basic assign script to test")
      When("I ask script helper for a basic assign script")
        val script = scriptHelper.getAssignScript(paramValues = Map(("valueToChange", "Starting Value")),
                                                  assignValues = Map(("variableName", "valueToChange"), ("variableValue", """\"Value has been changed!\"""")))
      Then("Validate I get a valid script back with an assign")
        val assignScriptResult = script.toString contains
          "\"id\":2,\"actor\":\"Assign\",\"prompt\":{\"messageType\":\"AssignConfiguration\",\"serializedMessage\":" +
            "{\"variableName\":\"valueToChange\",\"variableValue\":"
        assert(assignScriptResult)
        val scriptValidation = scriptHelper.validateScript(script, hostUri, successStatusCode)
        markup { commonFunctions.generateResultMarkup(s"Get an Assign Script with valid json result: $assignScriptResult<br/><br/>" +
                                      s"Script Validation: $scriptValidation") }
    }

    scenario("I'm able to get a basic switch scripts json") {
      Given("I want a basic assign script to test")
      When("I ask script helper for a basic switch script")
        val script = scriptHelper.getSwitchScript(switchSerializedMessage = Map(("predicate", "0")), switchPaths = Map(("0", 3), ("1", 4), ("error", 5)))
      Then("Validate I get a valid script back with an assign")
        val switchScriptResult = script.toString contains
          "\"id\":2,\"actor\":\"Switch\",\"prompt\":{\"messageType\":\"SwitchConfiguration\",\"serializedMessage\":{\"predicate\":\"0\"}"
        assert(switchScriptResult)
        val scriptValidation = scriptHelper.validateScript(script, hostUri, successStatusCode)
        markup { commonFunctions.generateResultMarkup(s"Get a Switch Script with valid json result: $switchScriptResult<br/><br/>" +
          s"Script Validation: $scriptValidation") }
    }

    scenario("I'm able to get a basic if scripts json") {
      Given("I want a basic if script to test")
      When("I ask script helper for a basic if script")
        val script = scriptHelper.getIfScript(ifSerializedMessage = Map(("predicate", "10 > 5")))
      Then("Validate I get a valid script back with an if")
        val ifScriptResult = script.toString() contains
          "\"id\":2,\"actor\":\"If\",\"prompt\":{\"messageType\":\"IfConfiguration\",\"serializedMessage\":{\"predicate\":\"10 > 5\"}}"
        assert(ifScriptResult)
        val scriptValidation = scriptHelper.validateScript(script, hostUri, successStatusCode)
      markup { commonFunctions.generateResultMarkup(s"Get an If Script with valid json result: $ifScriptResult<br/><br/>" +
                                    s"Script Validation: $scriptValidation") }
    }

    scenario("I'm able to get a basic wait scripts json") {
      Given("I want a basic wait script to test")
      When("I ask script helper for a basic wait script")
        val script = scriptHelper.getWaitScript(waitSerializedMessage = Map(("waitAmount", "6*2")))
      Then("Validate I get a valid script back with an if")
        val waitScriptResult = script.toString() contains
          "\"id\":2,\"actor\":\"Wait\",\"prompt\":{\"messageType\":\"WaitConfiguration\",\"serializedMessage\":{\"waitAmount\":\"6*2\"}}"
        assert(waitScriptResult)
        val scriptValidation = scriptHelper.validateScript(script, hostUri, successStatusCode)
        markup { commonFunctions.generateResultMarkup(s"Get an Wait Script with valid json result: $waitScriptResult<br/><br/>" +
                                      s"Script Validation: $scriptValidation") }
    }

    scenario("I'm able to pass actor objects to createScriptJson to make scripts") {
      Given("I want to get the json of a basic script")
        val beginId = 1
        val endId = 2
        val begin = new Actor(beginId, Actors.Begin, Map(), Map(("default", endId), ("error", endId)), start = true, end = false)
        val end = new Actor(endId, Actors.End, Map(), Map(), start = false, end = true)
      When("I pass the objects created to createScriptJson I get valid json back")
        val scriptJson = Json.parse(scriptHelper.createScriptJson(List(begin, end)))
        val scriptValidation = scriptHelper.validateScript(scriptJson, hostUri, successStatusCode)
        assert(scriptValidation)
      Then("Then script json I created has the correct actors")
        val containsBegin = scriptJson.toString().toLowerCase() contains "begin"
        val containsEnd = scriptJson.toString().toLowerCase() contains "end"
        val containsIf = scriptJson.toString().toLowerCase() contains "if"
        assert(containsBegin)
        assert(containsEnd)
        assert(!containsIf)
        markup { commonFunctions.generateResultMarkup(
                                 s"Create Begin > End Script result: Begin - $containsBegin If - $containsIf End - $containsEnd<br/><br/>" +
                                 s"Script Validation: $scriptValidation") }
    }

    scenario("I'm able to pull out script parameters from the begin actor") {
      Given("I want to get the json of a basic script")
        // Setup above
      When("I request the parameters from a script using script helper")
        val parameters = scriptHelper.getScriptParameters(validScriptWithParams)
      Then("Then script json I created has the correct actors")
        assert(parameters === "{\"ani\":\"someAni\",\"dnis\":\"someDnis\"}")
        markup { commonFunctions.generateResultMarkup(s"Get script parameters result: $parameters") }
    }
  }
}
