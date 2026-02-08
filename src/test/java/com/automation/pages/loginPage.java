package com.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class loginPage extends TestBase {
	
	productListPage productPage;
	
	@FindBy(id = "user-name")
	WebElement userNameInput;
	
	@FindBy(xpath = "//input[@data-test='password']")
	WebElement passwordInput;
	
	@FindBy(name = "login-button")
	WebElement loginBtn;
	
	public loginPage() {
		PageFactory.initElements(driver, this);
		log.info("LoginPage initialized");
	}
	
	
	// login method
	public void loginFunction(String username, String password) {
		sendText(userNameInput, username);
		log.info("Input Username successful");
		sendText(passwordInput, password);
		log.info("Input Password successful");
		loginBtn.click();
	}
	
	// login button click method
	public productListPage clickLoginBtn() {
		waitForClickability(loginBtn);
		loginBtn.click();
		log.info("Loggin button clicked");
		return new productListPage();
	}
	
    // verifications
    public boolean isOnLoginPage() {
        boolean isDisplayed = loginBtn.isDisplayed();
        log.debug("Is on login page: {}", isDisplayed);
        return isDisplayed;
    }

}
