package com.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class CheckoutInfoPage extends TestBase {
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
		PageFactory.initElements(driver, this);
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
		try {
			sendText(firstNameInput, firstName);
			sendText(lastNameInput, lastName);
			sendText(postalCodeInput, postalCode);
			log.info("Checkout info entered.");
		} catch (Exception e) {
			log.error("Checkout info was not entered. Error: {}", e);
		}
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
