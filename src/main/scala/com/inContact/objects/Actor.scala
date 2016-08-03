// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.objects

import com.inContact.utilities.ActorHelper

/**
  * Actor of the system that will be an action in your script
  *
  * @param id Id of the actor (Its location in the script)
  * @param actor Actor enum saying what type of actor this is
  * @param serializedMessage Actors serialized message, data differs between actors
  * @param dialogs Dialogs are your paths (e.g. default = actor id it will go to, error = actor id it will go to)
  * @param caption caption that appears near action
  * @param start Whether or not this is the start of your script
  * @param end Whether or not this is the end of your script
  * @param x x position of the action
  * @param y y position of the action
  */
class Actor(id: Integer, actor: Actors.Value, serializedMessage: Map[String,Any], dialogs: Map[String,Int],
            start: Boolean, caption: String = "", end: Boolean, x: Double = 1, y: Double = 1) {

  val actorHelper = new ActorHelper

  val Id = id
  val Actor = actor
  val Prompt = actor + "Configuration"
  val SerializedMessage = serializedMessage
  val Dialogs = dialogs
  val Start = start
  val End = end
  val Caption = if (caption.isEmpty) actor.toString else caption
  val X = x
  val Y = y

  /**
    * Will create cast json for the actor values passed in
 *
    * @return string with cast json
    */
  def toJsonCast: String = {
    actorHelper.createActorCastJson(Id, Actor, SerializedMessage, Caption, X, Y)
  }

  /**
    * Will create program json for the actor values passed in
 *
    * @return string with program json
    */
  def toJsonProgram: String = {
    actorHelper.createActorProgramJson(Id, Dialogs, Start, End)
  }

}
