package com.inContact.utilities
/**
  * Created by gustavo.ramirez on 8/2/2016.
  */
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient

class APIFunctions extends App{

  def getRestContent(url:String, Authorization : String, BucketName : String, Region : String ): String = {
    val httpClient = new DefaultHttpClient()
    val getRequest = new HttpGet(url)

    getRequest.addHeader("Authorization",Authorization)
    getRequest.addHeader("BucketName",BucketName)
    getRequest.addHeader("Region",Region)
    val httpResponse = httpClient.execute(getRequest)
    val entity = httpResponse.getEntity()
    var content = ""
    if (entity != null) {
      import org.apache.http.util.EntityUtils
      content = EntityUtils.toString(entity)
    }
    httpClient.getConnectionManager().shutdown()
    var v1 = httpResponse.getStatusLine.toString()
    //return content

    return v1
  }
}