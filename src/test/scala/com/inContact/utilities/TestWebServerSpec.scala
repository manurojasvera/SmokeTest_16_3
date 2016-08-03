// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import com.inContact.objects.{Functional, SignedOff, Sprint04252016, TfsWorkItem}
import com.inContact.specs.BaseFeatureSpec
import org.scalatest.BeforeAndAfterAll
import play.api.libs.json.Json

class TestWebServerSpec extends BaseFeatureSpec with BeforeAndAfterAll {
  info("As an automation engineer")
  info("I want the ability to instantiate a test web server")
  info("So that I can easily test rest services")

  val feature = 0
  val userStory = 0
  markup { commonFunctions.generateTfsMarkup(feature, TfsWorkItem.Feature) }

  //init
  val apiHelper = new ApiHelper
  val successCode = 200
  val port = 12500
  val uri = s"http://localhost:$port/test"
  val testServer = new TestWebServer(ActorSystem(),
    path("test") {
      post {
        complete(HttpEntity("POST Ok"))
      } ~
        get {
          complete(HttpEntity("GET Ok"))
        }
    }, port)
  var serverStarted = false

  override def beforeAll(): Unit = {
    serverStarted = testServer.checkStartUp(port, "test", "GET Ok")
  }

  override def afterAll(): Unit = {
    testServer.shutdown()
  }

  feature("Test Web Server") {
    scenario("Able to use test web server to perform a get",
      Functional, Sprint04252016, SignedOff) {
      markup { commonFunctions.generateTfsMarkup(userStory, TfsWorkItem.UserStory) }
      if (serverStarted) {
        Given("Test server is running")
          // setup above
        When("I perform a get on the /test uri")
          val response = apiHelper.get(uri, 0, successCode)
        Then("Check the response")
          assert(response === "GET Ok")
          markup { commonFunctions.generateResultMarkup(s"GET Response: $response") }
      }
      else {
        fail("Server was not started")
      }
    }

    scenario("Able to use test web server to perform a post",
      Functional, Sprint04252016, SignedOff) {
      markup { commonFunctions.generateTfsMarkup(userStory, TfsWorkItem.UserStory) }
      if (serverStarted) {
        Given("Test server is running")
        // setup above
        When("I perform a get on the /test uri")
          val response = apiHelper.post(uri, Json.parse("{}"), 0, successCode)
        Then("Check the response")
          assert(response === "POST Ok")
          markup { commonFunctions.generateResultMarkup(s"POST Response: $response") }
      }
      else {
        fail("Server was not started")
      }
    }
  }
}
