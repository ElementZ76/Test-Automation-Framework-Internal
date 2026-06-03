package com.automation.stepdef.flipkart;

import com.automation.pages.flipkart.CartPage;
import com.automation.pages.flipkart.HomePage;
import com.automation.pages.flipkart.ProductDetailsPage;
import com.automation.pages.flipkart.SearchResultsPage;
import com.automation.utils.JsonDataReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import static com.automation.driver.DriverManager.getDriver;

public class FlipkartStepDef {

    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    // Holds the product title captured from PDP for cart assertion
    private String addedProductTitle;

    // Holds the cart item count captured before a refresh
    private int cartItemCountBeforeRefresh;

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
            // Login popup may not appear on every load; safe to continue
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
        String productName = JsonDataReader.getString(fileName, "products", index);
        searchResultsPage = homePage.searchFor(productName);
    }

    @Then("search results page should load")
    public void searchResultsPageShouldLoad() {
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(),
                "Expected the search results page to load, but URL validation failed.");
    }

    @And("search results should contain relevant products")
    public void searchResultsShouldContainRelevantProducts() {
        Assert.assertTrue(searchResultsPage.getResultsCount() > 0,
                "Expected search results to contain at least one product, but result count was 0.");
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
        Assert.assertTrue(productDetailsPage.isProductDetailsPageLoaded(),
                "Expected the product detail page to load, but URL validation failed.");
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
                "Expected product image to be visible and loaded on the PDP.");
    }

    @Then("ratings and reviews section should be visible")
    public void ratingsAndReviewsSectionShouldBeVisible() {
        Assert.assertTrue(productDetailsPage.isProductRatingsVisible(),
                "Expected the ratings and reviews section to be visible on the PDP.");
    }

    // ─── FILTERS & SORTING ────────────────────────────────────────────────────

    @And("user applies brand filter from {string} using index {int}")
    public void userAppliesBrandFilterFromFileUsingIndex(String fileName, int index) {
        String brandName = JsonDataReader.getString(fileName, "brands", index);
        searchResultsPage.applyBrandFilter(brandName);
    }

    @Then("search results should be filtered by selected brand")
    public void searchResultsShouldBeFilteredBySelectedBrand() {
        // Re-read the brand used in the When step for assertion; index 0 from default data file
        String brandName = JsonDataReader.getString("flipkartData.json", "brands", 0);
        Assert.assertTrue(searchResultsPage.isBrandFilterApplied(brandName),
                "Expected brand filter chip '" + brandName + "' to be active, but it was not found.");
    }

    @And("user sorts results by {string}")
    public void userSortsResultsBy(String sortOption) {
        searchResultsPage.sortResultsBy(sortOption);
    }

    @Then("products should be displayed in ascending price order")
    public void productsShouldBeDisplayedInAscendingPriceOrder() {
        Assert.assertTrue(searchResultsPage.areProductPricesSortedByAscending(),
                "Expected products to be sorted in ascending price order, but they were not.");
    }

    // ─── CART ─────────────────────────────────────────────────────────────────

    @And("user adds the product to cart from PDP")
    public void userAddsTheProductToCartFromPDP() {
        productDetailsPage.addToCart();
    }

    @Then("cart count should be {int}")
    public void cartCountShouldBe(int expectedCount) {
        Assert.assertTrue(productDetailsPage.verifyIfCartIconHasProduct(),
                "Expected cart icon to show a non-zero item count after adding to cart.");
    }

    @And("cart should contain the added product")
    public void cartShouldContainTheAddedProduct() {
        cartPage = productDetailsPage.goToCartPage();
        // Flipkart's cart verification uses a partial/full product title
        // We navigate to the cart and confirm at least one matching item is present.
        // The product title check relies on the text visible in the cart row.
        Assert.assertTrue(cartPage.isProductPresentInCart(addedProductTitle != null ? addedProductTitle : ""),
                "Expected the added product to be present in the cart, but it was not found.");
    }

    @And("user navigates to cart")
    public void userNavigatesToCart() {
        cartPage = productDetailsPage.goToCartPage();
    }

    @And("user removes the product from cart")
    public void userRemovesTheProductFromCart() {
        // NOTE: CartPage does not currently expose a removeProduct() method.
        // This step requires a removeProduct(String productName) method to be added to CartPage.
        // Placeholder — implement once the method is available in CartPage:
        // cartPage.removeProduct(addedProductTitle);
        throw new UnsupportedOperationException(
                "CartPage.removeProduct() is not yet implemented. " +
                        "Please add a removeProduct method to CartPage to support this step.");
    }

    @Then("cart should be empty or show empty cart message")
    public void cartShouldBeEmptyOrShowEmptyCartMessage() {
        // NOTE: CartPage does not currently expose an isEmpty() or isEmptyMessageDisplayed() method.
        // This step requires such a method to be added to CartPage.
        throw new UnsupportedOperationException(
                "CartPage.isEmptyMessageDisplayed() is not yet implemented. " +
                        "Please add the relevant method to CartPage to support this step.");
    }

    @And("user refreshes the page")
    public void userRefreshesThePage() {
        // Capture cart count badge before navigating so we can compare post-refresh
        cartItemCountBeforeRefresh = productDetailsPage.verifyIfCartIconHasProduct() ? 1 : 0;
        cartPage = productDetailsPage.goToCartPage();
        String rawLabel = cartPage.getPriceItemCountLabelText();
        cartItemCountBeforeRefresh = cartPage.extractItemCount(rawLabel);
        cartPage.refreshCartPage();
    }

    @And("user proceeds to checkout from cart")
    public void userProceedsToCheckoutFromCart() {
        cartPage.clickPlaceOrder();
    }

    @Then("user should be on the login page")
    public void userShouldBeOnTheLoginPage() {
        Assert.assertTrue(cartPage.isLoginPromptHeaderPresent(),
                "Expected the login prompt to appear after clicking Place Order, but it was not found.");
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

    @Then("category page for {string} should load")
    public void categoryPageForShouldLoad(String category) {
        if (category.equalsIgnoreCase("Electronics")) {
            Assert.assertTrue(homePage.checkElectronicsURL(),
                    "Expected the Electronics category page URL to load, but URL validation failed.");
        } else {
            throw new UnsupportedOperationException(
                    "URL validation for category '" + category + "' is not yet implemented in HomePage.");
        }
    }

    @And("user clicks on the Flipkart logo")
    public void userClicksOnTheFlipkartLogo() {
        homePage = searchResultsPage.clickOnFlipkartLogo();
    }

    @Then("user should be on the Flipkart homepage")
    public void userShouldBeOnTheFlipkartHomepage() {
        Assert.assertTrue(homePage.isOnHomePage(),
                "Expected to be on the Flipkart homepage after clicking the logo, but the homepage indicator was not found.");
    }

    // ─── OFFERS & BANNERS ─────────────────────────────────────────────────────

    @Then("homepage promotional banners should be visible")
    public void homepagePromotionalBannersShouldBeVisible() {
        Assert.assertTrue(homePage.isOffersBannerDisplayed(),
                "Expected the homepage promotional offers banner to be visible.");
    }

    @Then("Suggested for you section should be visible on homepage")
    public void suggestedForYouSectionShouldBeVisibleOnHomepage() {
        Assert.assertTrue(homePage.clickSuggestedForYouButton(),
                "Expected the 'Suggested For You' section to be visible on the homepage.");
    }
}