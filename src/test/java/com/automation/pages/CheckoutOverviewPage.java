package com.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class CheckoutOverviewPage extends TestBase {
	@FindBy(className = "title")
	WebElement pageTitle;
	
	@FindBy(id = "finish")
	WebElement finishButton;
	
	public CheckoutOverviewPage() {
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * verify if we reached checkout overview page
	 * @return true if reached and false if not reached
	 */
	public boolean verifyPageTitle() {
		try {
			waitForVisibility(pageTitle);
			return pageTitle.isDisplayed() && pageTitle.getText().equalsIgnoreCase("Checkout: Overview");
		} catch (Exception e) {
			log.error("Did not reach checkout overview page.");
			return false;
		}
	}
	
	public void verifyItemsInCart(String productName) {
		String xpath = "//div[contains(@class='inventory_item_name') and normalize-space()='" + productName +"']";
		
	}
}
