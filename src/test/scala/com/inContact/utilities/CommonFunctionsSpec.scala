// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.utilities

import com.inContact.objects.TfsWorkItem
import com.inContact.specs.BaseFeatureSpec

class CommonFunctionsSpec extends BaseFeatureSpec {
  info("As an automation engineer")
  info("I want the ability to have access to common methods in all base specs")
  info("So that I can easily execute tests")

  val feature = 0
  val userStory = 0
  markup { commonFunctions.generateTfsMarkup(feature, TfsWorkItem.Feature) }

  feature("Common Functions Testing") {
    scenario("I'm able to easily generate tfs markup") {
      Given("Using CommonFunctions class from the base spec")
      When("I'm able to generate markup for tfs")
        val tfsMarkupFeature = commonFunctions.generateTfsMarkup(feature, TfsWorkItem.Feature)
        val tfsMarkupUserStory = commonFunctions.generateTfsMarkup(userStory, TfsWorkItem.UserStory)
      Then("I got the correct values")
        val featureResult = tfsMarkupFeature contains "***TFS-0 Feature***"
        assert(featureResult)
        val userStoryResult = tfsMarkupUserStory contains "***TFS-0 User Story***"
        assert(userStoryResult)
        markup { commonFunctions.generateResultMarkup(s"Tfs Markup for Feature: $featureResult<br/>" +
                                                      s"TfsMarkup for User Story: $userStoryResult")
        }
    }

    scenario("I'm able to easily generate result markup") {
      Given("Using CommonFunctions class from the base spec")
      When("I'm able to generate markup for result")
        val resultMarkup = commonFunctions.generateResultMarkup("RESULT")
      Then("I got the correct values")
        val result = resultMarkup contains "***RESULT***"
        assert(result)
        markup { commonFunctions.generateResultMarkup(s"Result Markup: $result")
        }
    }
  }
}
