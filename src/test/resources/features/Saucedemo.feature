Feature: SauceDemo E2E Purchase Flow

  Background:
    Given user navigates to SauceDemo application
    And user is on the login page

  @smoke @regression
  Scenario: Complete E2E purchase flow with valid user
    When user logs in with valid test data from "data.json" using index 0
    Then user should be on the products page
    When user adds all products from test data to cart
    And user navigates to cart
    Then cart should contain all added products
    When user proceeds to checkout
    And user fills checkout information from test data
    And user continues to overview page
    Then order summary should show correct items
    And price calculation should be valid
    When user completes the purchase
    Then order confirmation should display "WRONG VERIFICATION"

  @regression @negative
  Scenario: Login fails with locked out user
    When user attempts login with test data from "data.json" using index 1
    Then user should see error message from test data
    And user should remain on login page

  @smoke
  Scenario: Quick smoke test - Add single product to cart
    When user logs in with valid test data from "data.json" using index 0
    And user adds product "Sauce Labs Backpack" to cart
    Then cart badge should show 1 item
