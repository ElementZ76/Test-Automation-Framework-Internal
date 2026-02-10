package com.automation.stepdef;

import java.io.IOException;
import java.util.List;

import org.testng.Assert;

import com.automation.base.TestBase;
import com.automation.models.SauceData;
import com.automation.pages.CartPage;
import com.automation.pages.CheckoutCompletePage;
import com.automation.pages.CheckoutInfoPage;
import com.automation.pages.CheckoutOverviewPage;
import com.automation.pages.LoginPage;
import com.automation.pages.ProductListPage;
import com.automation.utils.JsonUtils;

import io.cucumber.java.en.*;

public class StepDef extends TestBase {
	private LoginPage loginPage;
	private ProductListPage productListPage;
	private CartPage cartPage;
	private CheckoutInfoPage checkoutInfoPage;
	private CheckoutOverviewPage checkoutOverviewPage;
	private CheckoutCompletePage checkoutCompletePage;
	
	private SauceData currentTestData;
	private List<String> addedProducts;
	
	@Given("user navigates to SauceDemo application")
	public void user_navigates_to_sauce_demo_application() {
		driver.get(prop.getProperty("url"));
		log.info("Navigated to: {}", prop.getProperty("url"));
	}

	@Given("user is on the login page")
	public void user_is_on_the_login_page() {
	    loginPage = new LoginPage();
	    Assert.assertTrue(loginPage.isOnLoginPage(), "Not on login page");
	}

	@When("user logs in with valid test data from {string} using index {int}")
	public void user_logs_in_with_valid_test_data_from_using_index(String jsonFile, Integer index) throws IOException {
		List<SauceData> testDataList = JsonUtils.getSauceData(jsonFile);
		currentTestData = testDataList.get(index);
		productListPage = loginPage.loginFunction(currentTestData.getUsername(), currentTestData.getPassword());
		Assert.assertNotEquals("Login failed", productListPage);
	}

	@Then("user should be on the products page")
	public void user_should_be_on_the_products_page() {
	    Assert.assertTrue(productListPage.isOnPLP(), "Not on PLP");
	}

	@When("user adds all products from test data to cart")
	public void user_adds_all_products_from_test_data_to_cart() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user navigates to cart")
	public void user_navigates_to_cart() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("cart should contain all added products")
	public void cart_should_contain_all_added_products() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user proceeds to checkout")
	public void user_proceeds_to_checkout() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user fills checkout information from test data")
	public void user_fills_checkout_information_from_test_data() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user continues to overview page")
	public void user_continues_to_overview_page() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("order summary should show correct items")
	public void order_summary_should_show_correct_items() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("price calculation should be valid")
	public void price_calculation_should_be_valid() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user completes the purchase")
	public void user_completes_the_purchase() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("order confirmation should display {string}")
	public void order_confirmation_should_display(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user attempts login with test data from {string} using index {int}")
	public void user_attempts_login_with_test_data_from_using_index(String string, Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("user should see error message from test data")
	public void user_should_see_error_message_from_test_data() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("user should remain on login page")
	public void user_should_remain_on_login_page() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("user adds product {string} to cart")
	public void user_adds_product_to_cart(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("cart badge should show {int} item")
	public void cart_badge_should_show_item(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
}
