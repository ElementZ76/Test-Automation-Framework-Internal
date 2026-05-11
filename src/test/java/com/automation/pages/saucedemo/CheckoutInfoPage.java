package com.automation.pages.saucedemo;

import com.automation.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.automation.driver.DriverManager.*;

public class CheckoutInfoPage extends BasePage {
	@FindBy(className = "title")
	private WebElement pageTitle;
	
	@FindBy(id = "first-name")
	WebElement firstNameInput;
	
	@FindBy(id = "last-name")
	WebElement lastNameInput;
	
	@FindBy(id = "postal-code")
	WebElement postalCodeInput;
	
	@FindBy(id = "continue")
	WebElement continueButton;
	
	public CheckoutInfoPage() {
		PageFactory.initElements(getDriver(), this);
	}
	
	public boolean isOnCheckoutInfoPage() {
		try {
			waitForVisibility(pageTitle);
			return pageTitle.isDisplayed() && 
					pageTitle.getText().equalsIgnoreCase("Checkout: Your Information");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Fill all checkout fields
	 * @param firstName
	 * @param lastName
	 * @param postalCode
	 * @return CheckoutInfoPage 
	 */
	public CheckoutInfoPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
		sendText(firstNameInput, firstName);
	    log.info("Entered first name: {}", firstName);
	    sendText(lastNameInput, lastName);
	    log.info("Entered last name: {}", lastName);
	    sendText(postalCodeInput, postalCode);
	    log.info("Entered postal code: {}", postalCode);
	    return this;
	}
	
	/**
	 * @return CheckoutOverviewPage
	 */
	public CheckoutOverviewPage continueToOverview() {
		clickOn(continueButton);
		log.info("Continuing to overview page");
		return new CheckoutOverviewPage();
	}
	
	
	
	
}
