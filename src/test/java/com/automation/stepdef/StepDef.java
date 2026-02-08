package com.automation.stepdef;

import com.automation.base.TestBase;

import io.cucumber.java.en.*;

public class StepDef extends TestBase {
	@Given("user navigates to SauceDemo application")
	public void user_navigates_to_sauce_demo_application() {
	}

	@Given("user is on the login page")
	public void user_is_on_the_login_page() {
	}

	@When("user logs in with valid credentials")
	public void user_logs_in_with_valid_credentials() {
	}

	@Then("user should be redirected to the products page")
	public void user_should_be_redirected_to_the_products_page() {
	    
	}

	@When("user adds multiple products to cart")
	public void user_adds_multiple_products_to_cart() {
	    
	}

	@When("user clicks on the shopping cart icon")
	public void user_clicks_on_the_shopping_cart_icon() {
	    
	}

	@Then("user should see all added products in the cart")
	public void user_should_see_all_added_products_in_the_cart() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user proceeds to checkout")
	public void user_proceeds_to_checkout() {
	    
	}

	@When("user fills in checkout information")
	public void user_fills_in_checkout_information() {
	   
	}

	@When("user continues to checkout overview")
	public void user_continues_to_checkout_overview() {
	    
	}

	@Then("user should see order summary with correct items")
	public void user_should_see_order_summary_with_correct_items() {
	    
	}

	@When("user completes the purchase")
	public void user_completes_the_purchase() {
	 
	}

	@Then("user should see order confirmation message")
	public void user_should_see_order_confirmation_message() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("user should see {string} message")
	public void user_should_see_message(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
}
