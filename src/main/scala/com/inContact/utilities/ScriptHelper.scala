// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import com.inContact.objects.{Actor, Actors}
import play.api.libs.json.{JsValue, Json}

/**
  * Class to help the building and manipulating of a script with the actor Json created
  *
  * @param creation passed in to validate creation in unit test
  */
class ScriptHelper(creation: Boolean) {

  private val created = creation

  val waitTime = 300
  val longWaitTime = 3000
  val apiHelper = new ApiHelper
  val apiEndpoints = new ApiEndpoints
  var runId = 0
  def getNextRunId: Int = {
    runId = runId - 1
    runId
  }

  /**
    * Get a basic default script with Begin/End in it
    *
    * @param validJson whether or not you want valid json script with begin/end (true) or invalid (false)
    * @param withParameters whether or not you want valid/invalid json with parameters
    * @return string with correct valid/invalid json depending on boolean
    */
  def getScriptJson(validJson: Boolean, withParameters: Boolean): String = {
    var json = ""
    if (validJson) {
      if (withParameters) {
        val begin = new Actor(1, Actors.Begin, Map(("ani", "someAni"), ("dnis", "someDnis")), Map(("default", 2), ("error", 2)), start = true, end = false)
        val end = new Actor(2, Actors.End, Map(), Map(), start = false, end = true)
        json = createScriptJson(List(begin, end))
      }
      else {
        val begin = new Actor(1, Actors.Begin, Map(), Map(("default", 2), ("error", 2)), start = true, end = false)
        val end = new Actor(2, Actors.End, Map(), Map(), start = false, end = true)
        json = createScriptJson(List(begin, end))
      }
    }
    else {
      if (withParameters) {
        json =
          "{\"cast\":[" +
            "{\"id\":1,\"actor\":\"Begin\",\"prompt\":{\"mesageType\":\"BeginConfiguration\",\"serializedMessage\":" +
            "{\"parameters\":{\"ani:\"someAni\",\"dnis\":someDnis\"}}},\"caption\":\"Begin\",\"x\":1.000000,\"y\":1.000000}," +
            "{\"id\":2,\"actor\":\"End\",\"prompt\":{\"messageType\":\"EndConfiguration\",\"serializedMessage\":null}," +
            "\"caption\":\"End\",\"x\":1.000000,\"y\":1.000000}]," +
            "\"programs\":[" +
            "{\"actorId\":1,\"dialgs\":{\"default\":2,\"error\":2},\"start\":true,\"end\":false}," +
            "{\"actorId\":2,\"dialogs\":{},\"start\":false,\"end\":true}]}"
      }
      else {
        json =
          "{\"cast\":[" +
            "{\"id\":1,\"actor\":\"Begin\",\"prompt\":{\"mesageType\":\"BeginConfiguration\",\"serializedMessage\":{\"parmaters\":{}}}" +
            ",\"caption\":\"Begin\",\"x\":1.000000,\"y\":1.000000}," +
            "{\"id\":2,\"actor\":\"End\",\"prompt\":{\"messageType\":\"EndConfiguration\",\"serializedMessage\":null}" +
            ",\"caption\":\"End\",\"x\":1.000000,\"y\":1.000000}]," +
            "\"programs\":[" +
            "{\"actorId\":1,\"dialgs\":{\"default\":2,\"error\":2},\"start\":true,\"end\":false}," +
            "{\"actorId\":2,\"dialogs\":{},\"start\":false,\"end\":true}]}"
      }
    }
    json
  }

  /**
    * Get a script with Begin > Wait > Assign > End
    *
    * @param paramValues if provided will pass in waitTime in parameters of begin
    * @param waitSerializedMessage can wait on the waitTime passed in with parameters or can just provide your own wait
    * @return valid json for the script specified
    */
  def getWaitScript(paramValues: Map[String,Any] = Map(), waitSerializedMessage: Map[String,Any] = Map()): JsValue = {
    val beginId = 1
    val waitId = 2
    val assignSuccessId = 3
    val assignErrorId = 4
    val endId = 5
    //create script
    val begin = new Actor(beginId, Actors.Begin, paramValues, Map(("default", waitId), ("error", assignErrorId)), start = true, end = false)
    val wait = new Actor(waitId, Actors.Wait, waitSerializedMessage, Map(("default", assignSuccessId), ("error", assignErrorId)), start = false, end = false)
    val assign1 = new Actor(assignSuccessId, Actors.Assign, Map(("variableName", "result"), ("variableValue", """\"Successfully proceeded past wait!\"""")),
      Map(("default", endId), ("error", assignErrorId)), start = false, end = false)
    val assign2 = new Actor(assignErrorId, Actors.Assign, Map(("variableName", "error"), ("variableValue", """\"An error occurred!\"""")),
      Map(("default", endId), ("error", endId)), start = false, end = false)
    val end = new Actor(endId, Actors.End, Map(), Map(), start = false, end = true)
    Json.parse(createScriptJson(List(begin, wait, assign1, assign2, end)))
  }

  /**
    * Get a script with Begin > If > True(Assign) | False(Assign) | Error(Assign) > End
    *
    * @param paramValues if provided will pass in predicate value in parameters of begin
    * @param ifSerializedMessage can set to the value passed in with parameters or can just do predicate with an expression
    * @param ifPaths if not set will get the default paths true > assignTrue & false > assignFalse & error > assignError
    * @return valid json for the script specified
    */
  def getIfScript(paramValues: Map[String,Any] = Map(), ifSerializedMessage: Map[String,Any] = Map(),
                  ifPaths: Map[String,Int] = Map(("true", 3), ("false", 4), ("error", 5))): JsValue = {
    val beginId = 1
    val ifId = 2
    val assignTruePathId = 3
    val assignFalsePathId = 4
    val assignErrorId = 5
    val endId = 6
    //create script
    val begin = new Actor(beginId, Actors.Begin, paramValues, Map(("default", ifId), ("error", assignErrorId)), start = true, end = false)
    val ifActor = new Actor(ifId, Actors.If, ifSerializedMessage, ifPaths, start = false, end = false)
    val assign1 = new Actor(assignTruePathId, Actors.Assign, Map(("variableName", "pathTaken"), ("variableValue", """\"True path was taken!\"""")),
      Map(("default", endId), ("error", assignErrorId)), start = false, end = false)
    val assign2 = new Actor(assignFalsePathId, Actors.Assign, Map(("variableName", "pathTaken"), ("variableValue", """\"False path was taken!\"""")),
      Map(("default", endId), ("error", assignErrorId)), start = false, end = false)
    val assign3 = new Actor(assignErrorId, Actors.Assign, Map(("variableName", "error"), ("variableValue", """\"An error occurred!\"""")),
      Map(("default", endId), ("error", endId)), start = false, end = false)
    val end = new Actor(endId, Actors.End, Map(), Map(), start = false, end = true)
    Json.parse(createScriptJson(List(begin, ifActor, assign1, assign2, assign3, end)))
  }

  /**
    * Get a script with Begin > Assign > Success(End) | Error(Assign > End)
    *
    * @param paramValues if provided will pass in assign value as parameters of begin
    * @param assignValues can override value passed in with parameters or set your own value
    * @return valid json for the script specified
    */
  def getAssignScript(paramValues: Map[String,Any] = Map(), assignValues: Map[String,Any] = Map()): JsValue = {
    val beginId = 1
    val assignSuccessId = 2
    val assignErrorId = 3
    val endId = 4
    //create script
    val begin = new Actor(beginId, Actors.Begin, paramValues, Map(("default", assignSuccessId), ("error", assignErrorId)), start = true, end = false)
    val assign1 = new Actor(assignSuccessId, Actors.Assign, assignValues, Map(("default", endId), ("error", assignErrorId)), start = false, end = false)
    val assign2 = new Actor(assignErrorId, Actors.Assign, Map(("variableName", "error"), ("variableValue", """\"An error occurred!\"""")),
      Map(("default", endId), ("error", endId)), start = false, end = false)
    val end = new Actor(endId, Actors.End, Map(), Map(), start = false, end = true)
    Json.parse(createScriptJson(List(begin, assign1, assign2, end)))
  }

  /** Get a script with Begin > Switch > Assign(Path1) | Assign(Path2) | Error(Assign) > End
    *
    * @param paramValues if provided will pass in predicate value in parameters of begin
    * @param switchSerializedMessage can set to the value passed in with parameters or can just do predicate with an expression
    * @param switchPaths if not set will get the default paths true > assignTrue & false > assignFalse & error > assignError
    * @return valid json for the script specified
    */
  def getSwitchScript(paramValues: Map[String,Any] = Map(), switchSerializedMessage: Map[String,Any] = Map(),
                      switchPaths: Map[String,Int]): JsValue = {
    val beginId = 1
    val switchId = 2
    val assignPath1Id = 3
    val assignPath2Id = 4
    val assignErrorId = 5
    val endId = 6
    //create script
    val begin = new Actor(beginId, Actors.Begin, paramValues, Map(("default", switchId), ("error", assignErrorId)), start = true, end = false)
    val switchActor = new Actor(switchId, Actors.Switch, switchSerializedMessage, switchPaths, start = false, end = false)
    val assign1 = new Actor(assignPath1Id, Actors.Assign, Map(("variableName", "pathTaken"), ("variableValue", """\"Switch path 1 was taken!\"""")),
      Map(("default", endId), ("error", assignErrorId)), start = false, end = false)
    val assign2 = new Actor(assignPath2Id, Actors.Assign, Map(("variableName", "pathTaken"), ("variableValue", """\"Switch path 2 was taken!\"""")),
      Map(("default", endId), ("error", assignErrorId)), start = false, end = false)
    val assign3 = new Actor(assignErrorId, Actors.Assign, Map(("variableName", "error"), ("variableValue", """\"An error occurred!\"""")),
      Map(("default", endId), ("error", endId)), start = false, end = false)
    val end = new Actor(endId, Actors.End, Map(), Map(), start = false, end = true)
    Json.parse(createScriptJson(List(begin, switchActor, assign1, assign2, assign3, end)))
  }

  /**
    * Create a json script with specified actors/programs
    *
    * @param actors list of the actors you want to add to the script
    * @return json string with full script
    */
  def createScriptJson(actors: List[Actor]): String = {
    val castSb = new StringBuilder
    var castString = ""
    actors.foreach(actor => castSb append actor.toJsonCast)
    castString = castSb.substring(0, castSb.length - 1)

    val programSb = new StringBuilder
    var programString = ""
    actors.foreach(actor => programSb append actor.toJsonProgram)
    programString = programSb.substring(0, programSb.length - 1)
    "{\"cast\":[%s],\"program\":[%s]}".format(castString, programString)
  }

  /**
    * Get the parameters out of the json to pass into the script begin action
    *
    * @param json JsValue that you have parameters in the first action that you want to post to startScript
    * @return string with parameters
    */
  def getScriptParameters(json: JsValue): String = {
    val firstActor = (json \ "cast")(0)
    val parsedParams = (firstActor \ "prompt" \ "serializedMessage" \ "parameters").toString
    val removeUndefined = 10
    parsedParams.substring(removeUndefined, parsedParams.length - 1)
  }

  /**
    * Save a script using the api
    *
    * @param scriptName name of the script that you want to save
    * @param businessNumber number of the business unit you want to save the script to
    * @param baseUrl base url for the api that you are attempting to call (e.g. http://localhost:9000)
    * @param expectedStatusCode status code that you expect from the call
    * @param scriptJson Script json that you want to persist
    * @return data returned from the api call in string format
    */
  def saveScript(scriptName: String, businessNumber: Int, baseUrl: String, expectedStatusCode: Int, scriptJson: JsValue): String = {
    val url = "%s%s".format(baseUrl, apiEndpoints.saveScript.format(businessNumber, scriptName))
    apiHelper.post(url, scriptJson, waitTime, expectedStatusCode)
  }

  /**
    * Start a script run using the api
    *
    * @param scriptName name of the script that you want to save
    * @param businessNumber number of the business unit you want to save the script to
    * @param baseUrl base url for the api that you are attempting to call (e.g. http://localhost:9000)
    * @param expectedStatusCode status code that you expect from the call
    * @param parameters Parameters from your script that you want to pass into the begin
    * @param specifiedWaitTime Time that you want to wait after starting a script if not supplied will use default
    * @return data returned from the api call in string format
    */
  def startScript(scriptName: String, businessNumber: Int, runId: Int, baseUrl: String, expectedStatusCode: Int,
                  parameters: JsValue, specifiedWaitTime: Int = longWaitTime): String = {
    val url = "%s%s".format(baseUrl, apiEndpoints.startScript.format(scriptName, businessNumber, runId))
    apiHelper.post(url, parameters, specifiedWaitTime, expectedStatusCode)
  }

  /**
    * Validate a script
    *
    * @param scriptToValidate JSValue of a script you want to validate
    * @param baseUrl base url for the api that you are attempting to call (e.g. http://localhost:9000)
    * @param expectedStatusCode status code that you expect from the call
    * @return true/false: true if valid | false if invalid
    */
  def validateScript(scriptToValidate: JsValue, baseUrl: String, expectedStatusCode: Int): Boolean = {
    val url = "%s%s".format(baseUrl, apiEndpoints.validateScript)
    val result = apiHelper.post(url, scriptToValidate, waitTime, expectedStatusCode)
    result.toLowerCase contains "valid"
  }

  /**
    * Override toString method for class
    *
    * @return string
    */
  override def toString: String = {
    "ScriptHelper Created %b".format(created)
  }
}
