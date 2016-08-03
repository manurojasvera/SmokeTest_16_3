// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import com.inContact.objects.Actors

/**
  * Class to help build the Actor Json strings more easily
  */
class ActorHelper() {

  /**
    * Create json string for cast member
 *
    * @param id id of the cast member
    * @param actor type of actor you want to add
    * @param serializedMessage values to pass into the serializedMessage for the cast member
    * @param caption caption that appears near action
    * @param x x position of the action
    * @param y y position of the action
    * @return individual cast actor json string
    */
  def createActorCastJson(id: Int, actor: Actors.Value, serializedMessage: Map[String,Any], caption: String, x: Double, y: Double): String = {
    val builtSerializedMessage = buildSerializedMessage(actor, serializedMessage)
    var returnString = ""
    if (actor equals Actors.Begin) {
      returnString = "{\"id\":%d," +
                      "\"actor\":\"%s\"," +
                      "\"prompt\":{" +
                        "\"messageType\":\"%s\"," +
                        "\"serializedMessage\":{" +
                          "\"parameters\":%s" +
                          "}" +
                        "}," +
                      "\"caption\":\"%s\"," +
                      "\"x\":%f," +
                      "\"y\":%f" +
                      "},"
    }
    else {
      returnString = "{\"id\":%d," +
                     "\"actor\":\"%s\"," +
                     "\"prompt\":{" +
                        "\"messageType\":\"%s\"," +
                        "\"serializedMessage\":%s" +
                      "}," +
                     "\"caption\":\"%s\"," +
                     "\"x\":%f," +
                     "\"y\":%f" +
                     "},"
    }

    returnString.format(id, actor.toString, actor.toString + "Configuration", builtSerializedMessage, caption, x, y)
  }

  /**
    * Create json string for program for cast member
 *
    * @param id id of the cast member
    * @param dialogs paths that could result from action
    * @param start whether or not this is the start of the script
    * @param end whether or not this is the end of the script
    * @return individual program for the cast members json string
    */
  def createActorProgramJson(id: Int, dialogs: Map[String,Int], start: Boolean, end: Boolean): String = {
    val sb = new StringBuilder
    var result = ""
    if (dialogs.nonEmpty) {
      for ((dialogKey, dialogValue) <- dialogs) {
        sb append "\"%s\":%d,".format(dialogKey, dialogValue)
      }
      if (sb.nonEmpty) {
        result = sb.substring(0, sb.length - 1)
      }
    }
    "{\"actorId\":%d,\"dialogs\":{%s},\"start\":%b,\"end\":%b},".format(id, result, start, end)
  }

  /**
    * Build the serialized message for the actor
 *
    * @param actor type of actor you want to build the serialized message for
    * @param serializedMessage values to pass into the serializedMessage for the case member
    * @return built serialized message string for your actor in valid json
    */
  private def buildSerializedMessage(actor: Actors.Value, serializedMessage: Map[String,Any]): String = {
    var result = ""
    if (serializedMessage.nonEmpty) {
      val sb = buildSerializedResult(serializedMessage)
      if (sb.nonEmpty) {
        result = "{" + sb.substring(0, sb.length - 1) + "}"
      }
    }
    if (result.isEmpty) {
      result = buildEmptyResult(actor)
    }
    result
  }

  /**
    * Build the serialized message into a string builder object
 *
    * @param serializedMessage Map that contains all the data we want in the serialized message
    * @return string builder with the serialized message in it
    */
  private def buildSerializedResult(serializedMessage: Map[String,Any]): StringBuilder = {
    val sb = new StringBuilder
    for ((paramKey, paramValue) <- serializedMessage) {
      paramValue match {
        case m: Map[_,_] if m.keySet.forall(_.isInstanceOf[String]) => sb append buildInnerCollection(paramKey, m.asInstanceOf[Map[String,Any]])
        case b: Boolean => sb append "\"%s\":%b,".format(paramKey, b)
        case s: String =>
          if (s.nonEmpty) {
            sb append "\"%s\":\"%s\",".format(paramKey, s)
          }
          else {
            sb append "\"%s\":null,".format(paramKey)
          }
        case i: Int => sb append "\"%s\":%d,".format(paramKey, i)
      }
    }
    sb
  }

  /**
    * If there is no serialized message values in the map passed in set string to either empty brackets or null
 *
    * @param actor type of actor you want to build the serialized message for
    * @return correct empty value type for the actor passed in
    */
  private def buildEmptyResult(actor: Actors.Value): String = {
    var result = ""
    if (actor equals Actors.Begin) {
      result = "{}"
    }
    else {
      result = "null"
    }
    result
  }

  /**
    * When the serialized message has inner collections this will build them
 *
    * @param paramKey key value for the inner collection
    * @param innerCollection inner collection of key/value pairs
    * @return built inner collection string for your actor in valid json
    */
  private def buildInnerCollection(paramKey: String, innerCollection: Map[String,Any]): String = {
    val sb = new StringBuilder
    var innerValues = ""
    for ((icKey, icValue) <- innerCollection) {
      icValue match {
        case b: Boolean => sb append "\"%s\":%b,".format(icKey, b)
        case s: String => sb append "\"%s\":\"%s\",".format(icKey, s)
        case i: Int => sb append "\"%s\":%d,".format(icKey, i)
      }
    }
    if (sb.nonEmpty) {
      innerValues = sb.substring(0, sb.length - 1)
    }
    else {
      innerValues = ""
    }
    "\"%s\": {%s},".format(paramKey, innerValues)
  }

}
