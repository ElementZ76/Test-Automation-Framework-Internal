package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.driver.DriverManager.*;

import static com.automation.driver.DriverManager.getDriver;

public class SearchResultsPage {





    public SearchResultsPage() {PageFactory.initElements(getDriver(), this);}
}
