package com.inContact.objects

/**
  * Created by gustavo.ramirez on 8/2/2016.
  */

import com.inContact.specs.BaseFeatureSpec
import com.inContact.utilities.APIFunctions

class APISpec extends BaseFeatureSpec {

  feature("Smoke test 16.3"){

    scenario("I am going to test if an API exits"){
      val expectedResult = "HTTP/1.1 200 OK"

      Given("The BU, base URL and the API")
        var bu = "bu=%d".format(886907)
        var base_uri = "https://storage.ucnlabext.com"
        val fileId = "98aff691-8783-4e7a-ba4e-0604a55937b0"
        var api = "/api/files/%s/exists?".format(fileId)
        var url = base_uri + api + bu

      When("It has been passed the url and headers")
        val authorization = "Basic RjFsMzV0MHJAZzM6NXQwcjRnMzRtNHoxbmd0MzRt"
        val buketName = "hugotestingbucket"
        val region = "us-west-1"
        var apiFunc = new APIFunctions
        var testResult = apiFunc.getRestContent(url, authorization, buketName, region)

      Then("Compare the expected result with the requested statutus")

      assert(testResult.contains(expectedResult))
      info("TEST RESULT => %s == %s\n".format(expectedResult, testResult))
    }

    scenario("text1SC2"){
      val expectedResult = "true"

      Given("text2SC2")

      When("text3SC2")

      Then("text4SC2")
        var testResult = "true"

      assert(testResult.contains(expectedResult))
      info("TEST RESULT => %s == %s\n".format(expectedResult, testResult))
    }
  }

}
