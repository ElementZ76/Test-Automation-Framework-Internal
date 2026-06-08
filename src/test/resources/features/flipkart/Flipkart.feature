Feature: Flipkart E2E Flows

  Background:
    Given user navigates to Flipkart application
    And user dismisses login popup if present

  # ─── AUTHENTICATION ───────────────────────────────────────────────

  @smoke @flipkart @auth @test
  Scenario: Guest user can browse without login
    Then user should see the Flipkart homepage
    And search bar should be visible

  # ─── SEARCH ───────────────────────────────────────────────────────

  @smoke @flipkart @search @test
  Scenario: Search for a product returns results
    When user searches for product from "flipkartData.json" using index 0
    Then search results page should load
    And search results should contain relevant products

  @regression @flipkart @search @test
  Scenario: Search suggestions appear on partial input
    When user types partial search term "iPhone"
    Then search suggestions dropdown should appear

  @regression @flipkart @search @negative @test
  Scenario: Search with invalid term shows no results message
    When user searches for product from "flipkartData.json" using index 9
    Then no results message should be shown

  # ─── PRODUCT DISCOVERY ────────────────────────────────────────────

  @smoke @flipkart @pdp @test
  Scenario: User can open a product detail page
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    Then product detail page should load
    And product name should be visible
    And product price should be displayed

  @regression @flipkart @pdp @test
  Scenario: Product images are displayed on PDP
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    Then product images should be visible on PDP

  @regression @flipkart @pdp @test
  Scenario: Product ratings and reviews section is visible
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    Then ratings and reviews section should be visible

  # ─── FILTERS & SORTING ────────────────────────────────────────────

  @regression @flipkart @filter @test
  Scenario: User can filter search results by brand
    When user searches for product from "flipkartData.json" using index 0
    And user applies brand filter from "flipkartData.json" using index 0
    Then search results should be filtered by selected brand

  @regression @flipkart @sort @test
  Scenario: User can sort results by price low to high
    When user searches for product from "flipkartData.json" using index 6
    And user sorts results by "Price -- Low to High"

  @regression @flipkart @sort @test
  Scenario: User can sort results by popularity
    When user searches for product from "flipkartData.json" using index 0
    And user sorts results by "Popularity"
    Then search results page should load

  # ─── CART ─────────────────────────────────────────────────────────

  @smoke @flipkart @cart @test
  Scenario: User can add a product to cart from PDP
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    Then cart count should be 1
    And cart should contain the added product

  @regression @flipkart @cart @test
  Scenario: User can remove a product from cart
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    And user navigates to cart
    And user removes the product from cart
    Then cart should be empty or show empty cart message

  @regression @flipkart @cart @test
  Scenario: Cart persists product after page refresh
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    And user refreshes the page
    Then cart count should be 1 after refresh

  # ─── CHECKOUT (GUEST) ─────────────────────────────────────────────

  @smoke @flipkart @checkout @test
  Scenario: User can proceed to checkout from cart
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the first product in search results
    And user adds the product to cart from PDP
    And user navigates to cart
    And user proceeds to checkout from cart
    Then user should be on the login page

  # ─── NAVIGATION ───────────────────────────────────────────────────

  @regression @flipkart @navigation @test
  Scenario: User can navigate to Flipkart home via logo
    When user searches for product from "flipkartData.json" using index 0
    And user clicks on the Flipkart logo
    Then user should be on the Flipkart homepage

  # ─── OFFERS & BANNERS ─────────────────────────────────────────────

  @regression @flipkart @offers @test
  Scenario: Homepage banners are displayed
    Then homepage promotional banners should be visible
