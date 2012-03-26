package org.mammon.features;

import static junit.framework.Assert.fail;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class IOUStepDefinitions {
	@Given("^(\\w+) owes (\\w+) (\\d+\\.\\d{2}) euros$")
	public void givenBorrowerOwesLenderMoney(String personA, String personB, String amount) {

	}

	@When("^(\\w+) creates an IOU of (\\d+\\.\\d{2}) euros$")
	public void whenBorrowerCreatesAnIOU(String person, String amount) {

	}

	@When("^sends the IOU to (\\w+)$")
	public void whenBorrowerSendsTheIOULender(String person) {

	}

	@Then("^(\\w+) should hold an IOU of (\\d+\\.\\d{2}) euros redeemable by (\\w+)$")
	public void thenLenderHoldsIOU(String personB, String amount, String personA) {
		fail();
	}

	@Given("^(\\w+) holds an IOU of (\\d+\\.\\d{2}) euros redeemable by (\\w+)$")
	public void givenLenderHoldsIOU(String lender, String amount, String borrower) {

	}

	@When("^(\\w+) lists the IOU's$")
	public void whenLenderListsIOUs(String lender) {

	}

	@Then("^(\\w+) should have the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void thenLenderShouldOwnIOU(String lender, String borrower, String amount) {
		fail();
	}

	@When("^(\\w+) redeems the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void lenderRedeemsIOU(String lender, String borrower, String amount) {

	}
}