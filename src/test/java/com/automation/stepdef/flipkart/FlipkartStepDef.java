package com.automation.stepdef.flipkart;

import com.automation.models.FlipkartData;
import com.automation.pages.flipkart.CartPage;
import com.automation.pages.flipkart.HomePage;
import com.automation.pages.flipkart.ProductDetailsPage;
import com.automation.pages.flipkart.SearchResultsPage;
import com.automation.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

import static com.automation.driver.DriverManager.getDriver;

public class FlipkartStepDef {

    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    // Product title captured from PDP; used later for cart product assertion
    private String addedProductTitle;

    // Cart item count captured before refresh; compared against post-refresh count
    private int cartItemCountBeforeRefresh;

    // Lazily loaded, file-scoped test data cache — avoids re-reading the JSON on every step
    private static List<FlipkartData> flipkartData;

    /**
     * Loads flipkartData.json once per suite run and returns the entry at the given index.
     * Uses JsonUtils.getTestData() with a TypeReference so Jackson can deserialize the
     * flat JSON array into a typed List<FlipkartData>.
     */
    private FlipkartData getData(String fileName, int index) {
        try {
            if (flipkartData == null) {
                flipkartData = JsonUtils.getTestData(fileName, new TypeReference<List<FlipkartData>>() {});
            }
            return flipkartData.get(index);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data from file: " + fileName, e);
        }
    }

    // ─── BACKGROUND ───────────────────────────────────────────────────────────

    @Given("user navigates to Flipkart application")
    public void userNavigatesToFlipkartApplication() {
        getDriver().get("https://www.flipkart.com");
        homePage = new HomePage();
    }

    @And("user dismisses login popup if present")
    public void userDismissesLoginPopupIfPresent() {
        try {
            homePage.dismissLoginPopup();
        } catch (Exception e) {
            // Popup is not guaranteed on every page load; safe to swallow and continue
        }
    }

    // ─── AUTHENTICATION ───────────────────────────────────────────────────────

    @Then("user should see the Flipkart homepage")
    public void userShouldSeeTheFlipkartHomepage() {
        Assert.assertTrue(homePage.isOnHomePage(),
                "Expected to be on the Flipkart homepage, but the homepage indicator was not found.");
    }

    @And("search bar should be visible")
    public void searchBarShouldBeVisible() {
        Assert.assertTrue(homePage.isSearchBarVisible(),
                "Expected the search bar to be visible on the homepage.");
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

    @When("user searches for product from {string} using index {int}")
    public void userSearchesForProductFromFileUsingIndex(String fileName, int index) {
        String searchTerm = getData(fileName, index).getSearchTerm();
        searchResultsPage = homePage.searchFor(searchTerm);
    }

    @Then("search results page should load")
    public void searchResultsPageShouldLoad() {
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(),
                "Expected the search results page to load, but URL validation failed.");
    }

    @And("search results should contain relevant products")
    public void searchResultsShouldContainRelevantProducts() {
        Assert.assertTrue(searchResultsPage.getResultsCount() > 0,
                "Expected search results to return at least one product, but result count was 0.");
    }

    @When("user types partial search term {string}")
    public void userTypesPartialSearchTerm(String partialTerm) {
        homePage.enterPartialTerm(partialTerm);
    }

    @Then("search suggestions dropdown should appear")
    public void searchSuggestionsDropdownShouldAppear() {
        Assert.assertTrue(homePage.isSearchDropdownVisible(),
                "Expected the search suggestions dropdown to be visible after partial input.");
    }

    @Then("no results message should be shown")
    public void noResultsMessageShouldBeShown() {
        Assert.assertTrue(searchResultsPage.isNoResultsMessageDisplayed(),
                "Expected a 'no results' message to be displayed for the invalid search term.");
        Assert.assertEquals(searchResultsPage.getNoResultsMessageText(), "Sorry, no results found!",
                "The no-results message text did not match the expected value.");
    }

    // ─── PRODUCT DISCOVERY (PDP) ──────────────────────────────────────────────

    @And("user clicks on the first product in search results")
    public void userClicksOnTheFirstProductInSearchResults() {
        productDetailsPage = searchResultsPage.clickFirstProduct();
    }

    @Then("product detail page should load")
    public void productDetailPageShouldLoad() {
        Assert.assertTrue(productDetailsPage.isProductNameVisible(),
                "Expected the product detail page to load, but product title verification failed in PDP.");
    }

    @And("product name should be visible")
    public void productNameShouldBeVisible() {
        Assert.assertTrue(productDetailsPage.isProductNameVisible(),
                "Expected the product name to be visible on the PDP.");
    }

    @And("product price should be displayed")
    public void productPriceShouldBeDisplayed() {
        Assert.assertTrue(productDetailsPage.isProductPriceVisible(),
                "Expected the product price to be visible on the PDP.");
    }

    @Then("product images should be visible on PDP")
    public void productImagesShouldBeVisibleOnPDP() {
        Assert.assertTrue(productDetailsPage.isProductImageVisible(),
                "Expected the product image to be visible and fully loaded on the PDP.");
    }

    @Then("ratings and reviews section should be visible")
    public void ratingsAndReviewsSectionShouldBeVisible() {
        Assert.assertTrue(productDetailsPage.isProductRatingsVisible(),
                "Expected the ratings and reviews section to be visible on the PDP.");
    }

    // ─── FILTERS & SORTING ────────────────────────────────────────────────────

    @And("user applies brand filter from {string} using index {int}")
    public void userAppliesBrandFilterFromFileUsingIndex(String fileName, int index) {
        String brandName = getData(fileName, index).getBrandFilter();
        searchResultsPage.applyBrandFilter(brandName);
    }

    @Then("search results should be filtered by selected brand")
    public void searchResultsShouldBeFilteredBySelectedBrand() {
        // Re-reads index 0 (same row used by the When step) to retrieve the expected brand chip value
        String brandName = getData("flipkartData.json", 0).getBrandFilter();
        Assert.assertTrue(searchResultsPage.isBrandFilterApplied(brandName),
                "Expected brand filter chip '" + brandName + "' to be active, but it was not found.");
    }

    @And("user sorts results by {string}")
    public void userSortsResultsBy(String sortOption) {
        searchResultsPage.selectSortOption(sortOption);
    }

    // ─── CART ─────────────────────────────────────────────────────────────────

    @And("user adds the product to cart from PDP")
    public void userAddsTheProductToCartFromPDP() {
        addedProductTitle = productDetailsPage.getProductTitleHeader();
        productDetailsPage.addToCart();
    }

    @Then("cart count should be {int}")
    public void cartCountShouldBe(int expectedCount) {
        Assert.assertTrue(productDetailsPage.verifyIfCartIconHasProduct(),
                "Expected the cart icon badge to show a non-zero item count after adding to cart.");
    }

    @And("cart should contain the added product")
    public void cartShouldContainTheAddedProduct() {
        String searchTerm = getData("flipkartData.json", 0).getSearchTerm();
        cartPage = productDetailsPage.goToCartPage();
        Assert.assertTrue(cartPage.isProductPresentInCart(searchTerm),
                "Expected the added product to be present in the cart, but it was not found.");
    }

    @And("user navigates to cart")
    public void userNavigatesToCart() {
        cartPage = productDetailsPage.goToCartPage();
    }

    @And("user removes the product from cart")
    public void userRemovesTheProductFromCart() {
        cartPage.removeProductFromCart();
    }

    @Then("cart should be empty or show empty cart message")
    public void cartShouldBeEmptyOrShowEmptyCartMessage() {
        Assert.assertTrue(cartPage.isCartEmptyMessageDisplayed(),
                "Expected the empty cart message ('Missing Cart items?') to be displayed after product removal.");
    }

    @And("user refreshes the page")
    public void userRefreshesThePage() {
        // Navigate to cart, read the item count label, store it, then refresh
        cartPage = productDetailsPage.goToCartPage();
        String rawLabel = cartPage.getPriceItemCountLabelText();
        cartItemCountBeforeRefresh = cartPage.extractItemCount(rawLabel);
        cartPage.refreshCartPage();
    }

    @Then("cart count should be {int} after refresh")
    public void cartCountShouldBeAfterRefresh(int expectedCount) {
        String rawLabelAfter = cartPage.getPriceItemCountLabelText();
        int cartItemCountAfterRefresh = cartPage.extractItemCount(rawLabelAfter);
        Assert.assertTrue(cartPage.compareItemCount(cartItemCountBeforeRefresh, cartItemCountAfterRefresh),
                "Expected cart item count to persist after refresh. Before: "
                        + cartItemCountBeforeRefresh + ", After: " + cartItemCountAfterRefresh);
    }

    @And("user proceeds to checkout from cart")
    public void userProceedsToCheckoutFromCart() {
        cartPage.clickPlaceOrder();
    }

    @Then("user should be on the login page")
    public void userShouldBeOnTheLoginPage() {
        Assert.assertTrue(cartPage.isLoginPromptHeaderPresent(),
                "Expected the login prompt to appear after clicking 'Place Order', but it was not found.");
    }

    // ─── NAVIGATION ───────────────────────────────────────────────────────────

    @When("user navigates to category {string} from top nav")
    public void userNavigatesToCategoryFromTopNav(String category) {
        if (category.equalsIgnoreCase("Electronics")) {
            homePage.clickElectronicsCategory();
        } else {
            throw new UnsupportedOperationException(
                    "Navigation to category '" + category + "' is not yet implemented in HomePage.");
        }
    }

    @And("user clicks on the Flipkart logo")
    public void userClicksOnTheFlipkartLogo() {
        homePage = searchResultsPage.clickOnFlipkartLogo();
    }

    @Then("user should be on the Flipkart homepage")
    public void userShouldBeOnTheFlipkartHomepage() {
        Assert.assertTrue(homePage.isOnHomePage(),
                "Expected to be on the Flipkart homepage after clicking the logo, but the indicator was not found.");
    }

    // ─── OFFERS & BANNERS ─────────────────────────────────────────────────────

    @Then("homepage promotional banners should be visible")
    public void homepagePromotionalBannersShouldBeVisible() {
        Assert.assertTrue(homePage.isOffersBannerDisplayed(),
                "Expected the homepage promotional offers banner to be visible.");
    }
}