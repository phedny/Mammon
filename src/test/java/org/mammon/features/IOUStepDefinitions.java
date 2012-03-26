package org.mammon.features;

import static junit.framework.Assert.fail;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class IOUStepDefinitions {
	@Given("^(\\w+) owes (\\w+) (\\d+\\.\\d{2}) euros$")
	public void givenBorrowerOwesLenderMoney(String borrower, String lender, String amount) {

	}

	@When("^(\\w+) creates an IOU of (\\d+\\.\\d{2}) euros$")
	public void whenBorrowerCreatesAnIOU(String borrower, String amount) {

	}

	@When("^sends the IOU to (\\w+)$")
	public void whenBorrowerSendsTheIOUToLender(String lender) {

	}

	@Then("^(\\w+) should have the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void thenLenderShouldOwnIOU(String lender, String borrower, String amount) {
		fail();
	}

	@Given("^(\\w+) holds the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void givenLenderHoldsIOU(String lender, String borrower, String amount) {

	}

	@When("^(\\w+) lists the IOU's$")
	public void whenLenderListsIOUs(String lender) {

	}

	@When("^(\\w+) redeems the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void lenderRedeemsIOU(String lender, String borrower, String amount) {

	}

	@When("^(\\w+) forwards the IOU \\[(\\w+), (\\d+\\.\\d{2})\\] to (\\w+)$")
	public void whenBorrowerForwardsIOU(String borrower, String forwardee, String amount, String lender) {

	}

	@Then("^(\\w+) should have no IOU's$")
	public void thenLenderShouldHaveNoIOUs() {
	    // Express the Regexp above with the code you wish you had
	}
}