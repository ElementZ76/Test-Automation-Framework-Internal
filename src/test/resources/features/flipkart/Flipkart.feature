Feature: Flipkart E2E Flows

  Background:
    Given user navigates to Flipkart application
    And user dismisses login popup if present

  # ─── AUTHENTICATION ───────────────────────────────────────────────

  @smoke @flipkart @auth
  Scenario: Guest user can browse without login - DONE
    Then user should see the Flipkart homepage
    And search bar should be visible

  # ─── SEARCH ───────────────────────────────────────────────────────

  @smoke @flipkart @search
  Scenario: Search for a product returns results - DONE
    When user searches for product from "flipkartData.json" using index 0
    Then search results page should load
    And search results should contain relevant products

  @regression @flipkart @search
  Scenario: Search suggestions appear on partial input - DONE
    When user types partial search term "iPhone"
    Then search suggestions dropdown should appear

  @regression @flipkart @search @negative
  Scenario: Search with invalid term shows no results message - DONE
    When user searches for product from "flipkartData.json" using index 9
    Then no results message should be shown

  # ─── PRODUCT DISCOVERY ────────────────────────────────────────────

  @smoke @flipkart @pdp
  Scenario: User can open a product detail page - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    Then product detail page should load
    And product name should be visible
    And product price should be displayed

  @regression @flipkart @pdp
  Scenario: Product images are displayed on PDP - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    Then product images should be visible on PDP

  @regression @flipkart @pdp
  Scenario: Product ratings and reviews section is visible - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    Then ratings and reviews section should be visible

  # ─── FILTERS & SORTING ────────────────────────────────────────────

  @regression @flipkart @filter
  Scenario: User can filter search results by brand - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user applies brand filter from "flipkartData.json" using index 0
    Then search results should be filtered by selected brand

  @regression @flipkart @sort
  Scenario: User can sort results by price low to high - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user sorts results by "Price -- Low to High"
    Then products should be displayed in ascending price order

  @regression @flipkart @sort
  Scenario: User can sort results by popularity - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user sorts results by "Popularity"
    Then search results page should load

  # ─── CART ─────────────────────────────────────────────────────────

  @smoke @flipkart @cart
  Scenario: User can add a product to cart from PDP - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    Then cart count should be 1
    And cart should contain the added product

  @regression @flipkart @cart
  Scenario: User can remove a product from cart - DONE
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    And user navigates to cart
    And user removes the product from cart
    Then cart should be empty or show empty cart message

  @regression @flipkart @cart
  Scenario: Cart persists product after page refresh
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    And user refreshes the page
    Then cart count should be 1

  # ─── WISHLIST ─────────────────────────────────────────────────────

  @regression @flipkart @wishlist @negative
  Scenario: Guest user is prompted to login when adding to wishlist
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to wishlist
    Then login prompt should appear for wishlist action

  # ─── CHECKOUT (GUEST) ─────────────────────────────────────────────

  @smoke @flipkart @checkout
  Scenario: User can proceed to checkout from cart
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    And user navigates to cart
    And user proceeds to checkout from cart
    Then user should be on the login page

  # ─── NAVIGATION ───────────────────────────────────────────────────

  @regression @flipkart @navigation
  Scenario: Category navigation loads correct page
    When user navigates to category "Electronics" from top nav
    Then category page for "Electronics" should load

  @regression @flipkart @navigation
  Scenario: User can navigate to Flipkart home via logo
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the Flipkart logo
    Then user should be on the Flipkart homepage

  # ─── OFFERS & BANNERS ─────────────────────────────────────────────

  @regression @flipkart @offers
  Scenario: Homepage banners are displayed
    Then homepage promotional banners should be visible

  @regression @flipkart @offers
  Scenario: Deals of the Day section is visible
    Then deals of the day section should be visible on homepage