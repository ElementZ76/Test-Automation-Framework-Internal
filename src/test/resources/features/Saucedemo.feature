Feature: SauceDemo E2E Purchase Flow

Background:
    Given user navigates to SauceDemo application
    And user is on the login page
    
  @smoke @regression
  Scenario: Successful product purchase with valid credentials
    When user logs in with valid credentials
    Then user should be redirected to the products page
    When user adds multiple products to cart
    And user clicks on the shopping cart icon
    Then user should see all added products in the cart
    When user proceeds to checkout
    And user fills in checkout information
    And user continues to checkout overview
    Then user should see order summary with correct items
    When user completes the purchase
    Then user should see order confirmation message
    And user should see "Thank you for your order!" message