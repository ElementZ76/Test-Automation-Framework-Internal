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
	
	@FindBy(xpath = "//h3[@data-test='error']")
	private WebElement errorMessage;
	
	public LoginPage() {
		PageFactory.initElements(driver, this);
		log.info("LoginPage initialized");
	}
	
	/** 
	 * Login with credentials
	 * @param username
	 * @param password
	 * @return 
	 */
	
	public ProductListPage loginFunction(String username, String password) {
		sendText(userNameInput, username);
		log.info("Input Username successful");
		sendText(passwordInput, password);
		log.info("Input Password successful");
		clickLoginBtn();
		return new ProductListPage();
	}
	
	/**
	 * Click login button
	 * @return ProductListPage
	 */
	public ProductListPage clickLoginBtn() {
		waitForClickability(loginBtn);
		clickOn(loginBtn);
		log.info("Loggin button clicked");
		return new ProductListPage();
	}
	
    /**
     * verify if we are on login page
     * @return true if we are on login page, if not then false
     */
    public boolean isOnLoginPage() {
        boolean isDisplayed = loginBtn.isDisplayed();
        log.debug("Is on login page: {}", isDisplayed);
        return isDisplayed;
    }
    
    public boolean isErrorDisplayed() {
		try {
			waitForVisibility(errorMessage);
			return errorMessage.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
    public String getErrorMessage() {
		return isErrorDisplayed() ? errorMessage.getText() : "";
	}

}
