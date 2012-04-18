package org.mammon.features;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class IOUStepDefinitions {
	@Given("^(\\w+) owes (\\w+) (\\d+\\.\\d{2}) euros$")
	public void givenBorrowerOwesLenderMoney(String borrower, String lender, String amount) {
	    // Express the Regexp above with the code you wish you had
	}

	@Given("^(\\w+) holds the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void givenLenderHoldsIOU(String lender, String borrower, String amount) {
		// Express the Regexp above with the code you wish you had
	}

	@Given("^(\\w+) holds the forwarded IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void givenLenderHoldsForwardedIOU(String lender, String borrower, String amount) {
		// Express the Regexp above with the code you wish you had
	}

	@When("^(\\w+) creates an IOU of (\\d+\\.\\d{2}) euros$")
	public void whenBorrowerCreatesAnIOU(String borrower, String amount) {
	    // Express the Regexp above with the code you wish you had
	}

	@When("^sends the IOU to (\\w+)$")
	public void whenBorrowerSendsTheIOUToLender(String lender) {
	    // Express the Regexp above with the code you wish you had
	}

	@When("^(\\w+) lists the IOU's$")
	public void whenLenderListsIOUs(String lender) {
	    // Express the Regexp above with the code you wish you had
	}

	@When("^(\\w+) redeems the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void lenderRedeemsIOU(String lender, String borrower, String amount) {
	    // Express the Regexp above with the code you wish you had
	}

	@When("^(\\w+) forwards the IOU \\[(\\w+), (\\d+\\.\\d{2})\\] to (\\w+)$")
	public void whenBorrowerForwardsIOU(String borrower, String forwardee, String amount, String lender) {
	    // Express the Regexp above with the code you wish you had
	}

	@When("^(\\w+) request a refresh of \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void whenLenderRequestARefresh(String lender, String borrower, String amount) {
		// Express the Regexp above with the code you wish you had
	}

	@Then("^(\\w+) should have the IOU \\[(\\w+), (\\d+\\.\\d{2})\\]$")
	public void thenLenderShouldOwnIOU(String lender, String borrower, String amount) {
		// Express the Regexp above with the code you wish you had
	}

	@Then("^(\\w+) should have no IOU's$")
	public void thenLenderShouldHaveNoIOUs(String lender) {
		// Express the Regexp above with the code you wish you had
	}

	@Then("^\\[(\\w+), (\\d+\\.\\d{2})\\] should be forwardable$")
	public void thenIOUShouldBeForwardable(String borrower, String amount) {
		// Express the Regexp above with the code you wish you had
	}

}