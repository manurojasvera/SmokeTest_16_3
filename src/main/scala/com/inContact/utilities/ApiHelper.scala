// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import org.apache.http.client.methods.{HttpDelete, HttpGet, HttpPost, HttpPut}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import play.api.libs.json.JsValue

class ApiHelper {

  /**
    * Make a post API call using an HttpClient
    * @param url url for the post request
    * @param postBody post body to apply to the request / currently only accept valid JSON if you don't want anything
    *                 posted provide empty JSON (e.g. {})
    * @param specifiedWaitTime time to wait after making the api call
    * @param expectedStatusCode status code that you expect from the call
    * @return data returned from the api call in string format
    */
  def post(url: String, postBody: JsValue, specifiedWaitTime: Int, expectedStatusCode: Int): String = {
    val httpClient = HttpClientBuilder.create().build()
    val post = new HttpPost(url)
    post.setEntity(new StringEntity(postBody.toString(), "UTF8"))
    post.setHeader("Content-type", "application/json")
    val response = httpClient.execute(post)

    var content = ""
    if (response.getStatusLine.getStatusCode equals expectedStatusCode) {
      content = EntityUtils.toString(response.getEntity)
    }
    else {
      content = s"An error occurred calling $url -- Response: ${response.getStatusLine.getStatusCode}"
    }

    Thread.sleep(specifiedWaitTime)
    httpClient.close()
    content
  }

  /**
    * Make a get API call using an HttpClient
    * @param url url for the get request
    * @param specifiedWaitTime time to wait after making the api call
    * @param expectedStatusCode status code that you expect from the call
    * @return data returned from the api call in string format
    */
  def get(url: String, specifiedWaitTime: Int, expectedStatusCode: Int): String = {
    val httpClient = HttpClientBuilder.create().build()
    val get = new HttpGet(url)
    val response = httpClient.execute(get)

    var content = ""
    if (response.getStatusLine.getStatusCode equals expectedStatusCode) {
      content = EntityUtils.toString(response.getEntity)
    }
    else {
      content = s"An error occurred calling $url -- Response: ${response.getStatusLine.getStatusCode}"
    }

    Thread.sleep(specifiedWaitTime)
    httpClient.close()
    content
  }

  /**
    * Make a delete API call using an HttpClient
    * @param url url for the get request
    * @param specifiedWaitTime time to wait after making the api call
    * @param expectedStatusCode status code that you expect from the call
    * @return data returned from the api call in string format
    */
  def delete(url: String, specifiedWaitTime: Int, expectedStatusCode: Int): String = {
    val httpClient = HttpClientBuilder.create().build()
    val delete = new HttpDelete(url)
    val response = httpClient.execute(delete)

    var content = ""
    if (response.getStatusLine.getStatusCode equals expectedStatusCode) {
      content = EntityUtils.toString(response.getEntity)
    }
    else {
      content = s"An error occurred calling $url -- Response: ${response.getStatusLine.getStatusCode}"
    }

    Thread.sleep(specifiedWaitTime)
    httpClient.close()
    content
  }

  /**
    * Make a put API call using an HttpClient
    * @param url url for the post request
    * @param putBody put body to apply to the request / currently only accept valid JSON if you don't want anything
    *                put provide empty JSON (e.g. {})
    * @param specifiedWaitTime time to wait after making the api call
    * @param expectedStatusCode status code that you expect from the call
    * @return data returned from the api call in string format
    */
  def put(url: String, putBody: JsValue, specifiedWaitTime: Int, expectedStatusCode: Int): String = {
    val httpClient = HttpClientBuilder.create().build()
    val put = new HttpPut(url)
    put.setEntity(new StringEntity(putBody.toString(), "UTF8"))
    put.setHeader("Content-type", "application/json")
    val response = httpClient.execute(put)

    var content = ""
    if (response.getStatusLine.getStatusCode equals expectedStatusCode) {
      content = EntityUtils.toString(response.getEntity)
    }
    else {
      content = s"An error occurred calling $url -- Response: ${response.getStatusLine.getStatusCode}"
    }

    Thread.sleep(specifiedWaitTime)
    httpClient.close()
    content
  }

}
