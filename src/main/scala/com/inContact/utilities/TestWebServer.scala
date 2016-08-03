// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol

case class TestData(field1: String)

trait TestDataSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val testDataFormat = jsonFormat1(TestData)
}

/**
  * Creates a Test Web Server and binds it to a port to setup and tear down something to hit with requests
  * @param testSystem actor system that you want to test
  * @param route routes that will be spawned
  * @param port port that you want the server to bind to for the tests
  */
class TestWebServer(testSystem: ActorSystem, route: Route, port: Int) {

  val apiHelper = new ApiHelper
  implicit val system = testSystem
  implicit val materialize = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  /**
    * Bind the route to the http://localhost:$port
    */
  val bindingFuture = Http().bindAndHandle(route, "localhost", port)

  /**
    * Unbind from port and shutdown the test server
    */
  def shutdown(): Unit = {

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())

  }

  /**
    * Validate the server is started and ready to go before testing
    * @param port port that you have started the server on
    * @param uri uri that you want to hit to test
    * @param expectedResponse response you expect from the test uri to validate server is running
    * @return boolean - true if ready / false if not
    */
  def checkStartUp(port: Int, uri: String, expectedResponse: String): Boolean = {

    var result = false
    var retries = 0
    val successCode = 200
    val totalRetries = 5
    val exitWhile = 6
    val waitTime = 5

    while (retries < totalRetries){
      try {
        val response = apiHelper.get(s"http://localhost:$port/$uri", waitTime, successCode)
        result = response == expectedResponse
        retries = if (result) exitWhile else retries + 1
      }
      catch {
        case e: Exception => println(e.getMessage)
      }
    }

    result

  }
}
