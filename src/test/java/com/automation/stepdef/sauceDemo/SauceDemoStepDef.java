package com.automation.stepdef.sauceDemo;

import java.io.IOException;
import java.util.List;

import com.automation.pages.*;
import com.automation.pages.saucedemo.*;
import com.automation.utils.ConfigManager;
import com.fasterxml.jackson.core.type.TypeReference;
import org.testng.Assert;

import com.automation.models.SauceData;
import com.automation.utils.JsonUtils;

import io.cucumber.java.en.*;
import io.qameta.allure.Step;

import static com.automation.driver.DriverManager.*;

public class SauceDemoStepDef extends BasePage {
	private LoginPage loginPage;
	private ProductListPage productListPage;
	private CartPage cartPage;
	private CheckoutInfoPage checkoutInfoPage;
	private CheckoutOverviewPage checkoutOverviewPage;
	private CheckoutCompletePage checkoutCompletePage;
	
	private SauceData currentTestData;
	private List<String> addedProducts;
	
	@Step("Navigate to SauceDemo application")
	@Given("user navigates to SauceDemo application")
	public void user_navigates_to_sauce_demo_application() {
		getDriver().get(ConfigManager.get("url"));
		log.info("Navigated to: {}", ConfigManager.get("url"));
	}
	
	@Step("Verify user is on login page")
	@Given("user is on the login page")
	public void user_is_on_the_login_page() {
	    loginPage = new LoginPage();
	    Assert.assertTrue(loginPage.isOnLoginPage(), "Not on login page");
	}
	
	@Step("User logs in with test data from {0} at index {1}")
	@When("user logs in with valid test data from {string} using index {int}")
	public void user_logs_in_with_valid_test_data_from_using_index(String jsonFile, Integer index) throws IOException {
		List<SauceData> testDataList = JsonUtils.getTestData(jsonFile, new TypeReference<List<SauceData>>(){});
		currentTestData = testDataList.get(index);
		productListPage = loginPage.loginFunction(currentTestData.getUsername(), currentTestData.getPassword());
		Assert.assertNotEquals("Login failed", productListPage);
	}

	@Step("Verify user is on products page")
	@Then("user should be on the products page")
	public void user_should_be_on_the_products_page() {
	    Assert.assertTrue(productListPage.isOnPLP(), "Not on PLP");
	}
	
	@Step("Add all products from test data to cart")
	@When("user adds all products from test data to cart")
	public void user_adds_all_products_from_test_data_to_cart() throws IOException {
		addedProducts = currentTestData.getProducts();
		for (String product : addedProducts) {
			productListPage.addProductToCart(product);
		}
	}
	
	@Step("Navigate to shopping cart")
	@When("user navigates to cart")
	public void user_navigates_to_cart() {
	    cartPage = productListPage.clickShoppingCart();
	}
	
	@Step("Verify cart contains all added products")
	@Then("cart should contain all added products")
	public void cart_should_contain_all_added_products() {
	    Assert.assertTrue(cartPage.verifyCartHasProducts(addedProducts), "Cart products mismatch");
	}

	@Step("Proceed to checkout")
	@When("user proceeds to checkout")
	public void user_proceeds_to_checkout() {
		checkoutInfoPage = cartPage.proceedToCheckout();
	}
	
	@Step("Fill checkout information from test data")
	@When("user fills checkout information from test data")
	public void user_fills_checkout_information_from_test_data() {
	    checkoutInfoPage.fillCheckoutInfo(currentTestData.getFirstName(), 
	    		currentTestData.getLastName(), currentTestData.getPostalCode());
	}
	
	@Step("Continue to overview page")
	@When("user continues to overview page")
	public void user_continues_to_overview_page() {
	    checkoutOverviewPage = checkoutInfoPage.continueToOverview();
	}

	@Step("Verify order summary shows correct items")
	@Then("order summary should show correct items")
	public void order_summary_should_show_correct_items() {
	    Assert.assertTrue(checkoutOverviewPage.verifyOrderItemSummary(addedProducts), "Order summary mismatch");
	}
	
	@Step("Verify price calculation is valid")
	@Then("price calculation should be valid")
	public void price_calculation_should_be_valid() {
	    Assert.assertTrue(checkoutOverviewPage.validatePriceCalculation(), "Price mismatch.");
	}
	
	@Step("Complete the purchase")
	@When("user completes the purchase")
	public void user_completes_the_purchase() {
	    checkoutCompletePage = checkoutOverviewPage.finishPurchase();
	}
	
	@Step("Verify order confirmation displays: {0}")
	@Then("order confirmation should display {string}")
	public void order_confirmation_should_display(String expectedMessage) {
	    Assert.assertTrue(checkoutCompletePage.verifyConfirmationmessage(expectedMessage), "Not correct message!");
	}
	
	@Step("Attempt login with test data from {0} at index {1}")
	@When("user attempts login with test data from {string} using index {int}")
	public void user_attempts_login_with_test_data_from_using_index(String jsonFile, Integer index) throws IOException {
		List<SauceData> testDataList = JsonUtils.getTestData(jsonFile, new TypeReference<List<SauceData>>(){});
		currentTestData = testDataList.get(index);
		loginPage.loginFunction(currentTestData.getUsername(), currentTestData.getPassword());
	}
	
	@Step("Verify error message matches test data")
	@Then("user should see error message from test data")
	public void user_should_see_error_message_from_test_data() {
	    String expectedError = currentTestData.getErrorMessage();
	    String actualError = loginPage.getErrorMessage();
	    Assert.assertEquals(actualError, expectedError, "Error message mismatch" );
	}
	
	@Step("Verify user remains on login page")
	@Then("user should remain on login page")
	public void user_should_remain_on_login_page() {
	    Assert.assertTrue(loginPage.isOnLoginPage(), "Not on login page");
	}

	@Step("Add product '{0}' to cart")
	@When("user adds product {string} to cart")
	public void user_adds_product_to_cart(String productName) {
	    productListPage.addProductToCart(productName);
	}
	
	@Step("Verify cart badge shows {0} item(s)")
	@Then("cart badge should show {int} item")
	public void cart_badge_should_show_item(Integer productCount) {
		int actualCount = productListPage.getItemCount();
	    Assert.assertEquals(actualCount, productCount, "Mismatch in cart badge");
	}
	
}
