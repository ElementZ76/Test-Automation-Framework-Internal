package com.automation.stepdef;

import com.automation.base.TestBase;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class ApplicationHooks extends TestBase {
	@Before
	public void initialization() {
		initialization();
	}
	
	@After
	public void quitBrowser() {
		tearDown();
	}
}
