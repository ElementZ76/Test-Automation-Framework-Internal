package com.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class LoginPage extends TestBase {
	
	ProductListPage productPage;
	
	@FindBy(id = "user-name")
	WebElement userNameInput;
	
	@FindBy(xpath = "//input[@data-test='password']")
	WebElement passwordInput;
	
	@FindBy(name = "login-button")
	WebElement loginBtn;
	
	public LoginPage() {
		PageFactory.initElements(driver, this);
		log.info("LoginPage initialized");
	}
	
	/** 
	 * Login with credentials
	 * @param username
	 * @param password
	 */
	
	public void loginFunction(String username, String password) {
		sendText(userNameInput, username);
		log.info("Input Username successful");
		sendText(passwordInput, password);
		log.info("Input Password successful");
		clickOn(loginBtn);
	}
	
	// login button click method
	public ProductListPage clickLoginBtn() {
		waitForClickability(loginBtn);
		clickOn(loginBtn);
		log.info("Loggin button clicked");
		return new ProductListPage();
	}
	
    // verifications
    public boolean isOnLoginPage() {
        boolean isDisplayed = loginBtn.isDisplayed();
        log.debug("Is on login page: {}", isDisplayed);
        return isDisplayed;
    }

}
