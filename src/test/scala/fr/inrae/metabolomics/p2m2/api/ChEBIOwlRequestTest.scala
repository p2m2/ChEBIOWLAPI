package fr.inrae.metabolomics.p2m2.api

import utest.{TestSuite, Tests}

object ChEBIOwlRequestTest extends TestSuite {
  val tests = Tests {
    ChEBIOwlRequest.get("test")
  }
}
