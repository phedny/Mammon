package org.mammon.features;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import static junit.framework.Assert.fail;

public class IOUStepDefinitions {
	@Given("^(\\w+) owes (\\w+) (\\d+\\.\\d{2}) euros")
	public void aOwesBMoney(String personA, String personB, String amount) {

	}

	@When("^(\\w+) creates an IOU of (\\d+\\.\\d{2}) euros")
	public void aCreatesAnIOU(String person, String amount) {

	}

	@When("^sends the IOU to (\\w+)")
	public void sendsTheIOUTo(String person) {

	}

	@Then("^(\\w+) should hold an IOU of (\\d+\\.\\d{2}) euros redeemable by (\\w+)")
	public void bHoldsIOU(String personB, String amount, String personA) {
		fail();
	}
}