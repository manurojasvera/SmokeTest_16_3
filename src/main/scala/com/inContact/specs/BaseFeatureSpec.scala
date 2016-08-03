// Copyright (C) 2016 the original author or authors.
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.

package com.inContact.specs

import akka.util.Timeout
import com.inContact.utilities.{CommonFunctions, ScriptHelper}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest._

import scala.concurrent.duration._

/**
  * Base FeatureSpec
  */
class BaseFeatureSpec extends FeatureSpecLike with GivenWhenThen with TypeCheckedTripleEquals with BeforeAndAfterAll {

  // Commonly used values inherited by all classed extending
  val defaultBusinessUnitNumber = 0
  val scriptHelper = new ScriptHelper(true)
  val commonFunctions = new CommonFunctions(true)
  val testTimeout = new Timeout(10.seconds)
}
