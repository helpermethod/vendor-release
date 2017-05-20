/**
  * Copyright 2014 Marco Vermeulen
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package steps

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import support.{Candidate, Mongo, Version}

class PersistenceSteps extends ScalaDsl with EN with Matchers {
  Then( """^"(.*?)" Version "(.*?)" with URL "(.*?)" was published as (.*?)$""") { (candidate: String, version: String, url: String, platform: String) =>
    withClue("Version was not published") {
      Mongo.versionPublished(candidate, version, url, platform) shouldBe true
    }
  }

  Given( """^a "(.*?)" Version "(.*?)" with URL "(.*?)" already exists$""") { (candidate: String, version: String, url: String) =>
    Mongo.insertVersion(Version(candidate, version, "UNIVERSAL", s"http://somecandidate.org/$candidate/$version"))
  }

  Given( """^the existing Default "(.*?)" Version is "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.insertVersion(
      Version(
        candidate = candidate,
        version = version,
        platform = "UNIVERSAL",
        url = s"http://somecandidate.org/$candidate/$version"))
    Mongo.insertCandidate(
      Candidate(
        candidate = candidate,
        name = candidate.capitalize,
        description = s"$candidate description",
        default = version,
        websiteUrl = s"http://somecandidate.org/$candidate",
        distribution = "UNIVERSAL"))
  }

  Then( """^the Default "(.*?)" Version has changed to "(.*?)"$""") { (candidate: String, version: String) =>
    Mongo.isDefault(candidate, version) shouldBe true
  }

  Given( """^Candidate "(.*?)" Version "(.*?)" does not exists$""") { (candidate: String, version: String) =>
    Mongo.versionExists(candidate, version) shouldBe false
  }

  Given( """^Candidate "(.*?)" does not exist$""") { (candidate: String) =>
    Mongo.candidateExists(candidate) shouldBe false
  }
}