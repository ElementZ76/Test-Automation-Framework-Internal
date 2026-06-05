# One-time: create and switch to branch
git checkout -b feature/flipkart-onboarding

# Verify you're on it
git branch

# After making changes, stage and commit to THIS branch only
git add src/test/java/com/automation/pages/flipkart/
git add src/test/java/com/automation/stepdef/flipkart/
git add src/test/resources/features/flipkart/
git add src/test/resources/testdata/flipkartData.json
git add src/test/resources/flipkart.properties
git add src/test/resources/testng-flipkart.xml

# Commit
git commit -m "feat: onboard Flipkart application to framework"

# Push branch to remote (first time)
git push -u origin feature/flipkart-onboarding

# Subsequent pushes (same branch)
git push

# AI COMMANDS
## To generate the Page Objects from DOM scripts
Instead of manually typing out dozens of @FindBy annotations or By locators, inspect the webpage, copy the HTML snippet of the elements you need, and feed it to the AI.
Your Prompt to AI:
"Act as a QA automation engineer. Based on this HTML snippet, generate Page Object Model locators using
Selenium By selectors in Java. Provide clean, atomic wrapper methods for clicking or entering text for each element."

## Test data Generation including edge cases
"Generate a list of 10 edge-case inputs for a user registration form 
password field, testing boundary values, special characters, and length restrictions, formatted as a Cucumber Data Table."

## Extracting a particular element from DOM
"I am writing a Selenium Java automation script. Below is the HTML snippet for a login popup modal. I need to click the 'close' or 'X' button to dismiss it, but the DOM is obfuscated. The button likely does not have clear text.

Based on this HTML structure, give me three robust XPath or CSS Selector options to locate the close button. Avoid using dynamic class names like '_2doB4z'. Prioritize structural relationships, ARIA attributes, or SVG targeting."

[Paste your HTML snippet here]

# ALL PAGE TO METHOD MAPPING

STEP                                                        PAGE                    METHOD
─────────────────────────────────────────────────────────────────────────────────────────────
user navigates to Flipkart                                  (hook/step)             driver.get(url)
user dismisses login popup if present                       HomePage                dismissLoginPopup()
user should see Flipkart homepage                           HomePage                isOnHomePage()
search bar should be visible                                HomePage                isSearchBarVisible()
user opens the login modal                                  HomePage                openLoginModal() → LoginModal
user logs in with credentials                               LoginModal              loginWith(u, p) → HomePage
user should be logged in successfully                       HomePage                isLoggedIn()
user attempts login with invalid creds                      LoginModal              loginWith(u, p)
login error message should be displayed                     LoginModal              getErrorMessage()
user searches for product                                   HomePage                searchFor(String) → SearchResultsPage
search results page should load                             SearchResultsPage       isLoaded()
search results contain relevant products                    SearchResultsPage       getResultCount()
user types partial search term                              HomePage                typeInSearchBox(String)
search suggestions dropdown appears                         HomePage                isSuggestionsDropdownVisible()
user searches invalid term                                  HomePage                searchFor(String)
no results message shown                                    SearchResultsPage       isNoResultsMessageVisible()
user clicks first product                                   SearchResultsPage       clickFirstProduct() → ProductDetailPage
product detail page should load                             ProductDetailPage       isLoaded()
product name visible                                        ProductDetailPage       isProductNameVisible()
product price displayed                                     ProductDetailPage       getProductPrice()
product images visible                                      ProductDetailPage       areImagesVisible()
ratings and reviews visible                                 ProductDetailPage       isRatingsAndReviewsVisible()
user applies brand filter                                   SearchResultsPage       applyBrandFilter(String)
results filtered by brand                                   SearchResultsPage       areResultsFilteredBy(String)
user applies price range filter                             SearchResultsPage       applyPriceRangeFilter(int, int)
products within price range                                 SearchResultsPage       areAllProductsWithinPriceRange(int, int)
user sorts results                                          SearchResultsPage       sortBy(String)
products in ascending price order                           SearchResultsPage       areProductsSortedByPriceAscending()
user adds product to cart from PDP                          ProductDetailPage       addToCart() → ProductDetailPage
cart count should be 1                                      ProductDetailPage       getCartCount()
cart should contain added product                           CartPage                containsProduct(String)
user navigates to cart                                      (any page)              navigateToCart() → CartPage
user removes product from cart                              CartPage                removeProduct(String)
cart should be empty                                        CartPage                isEmpty()
user refreshes the page                                     (step)                  driver.navigate().refresh()
user adds product to wishlist                               ProductDetailPage       addToWishlist()
wishlist confirmation shown                                 ProductDetailPage       isWishlistConfirmationVisible()
login prompt appears for wishlist                           ProductDetailPage       isLoginPromptVisible()
user proceeds to checkout from cart                         CartPage                proceedToCheckout() → CheckoutPage
user should be on checkout or login page                    CheckoutPage            isOnPage()
user navigates to category from top nav                     HomePage                clickCategory(String) → CategoryPage
category page should load                                   CategoryPage            isLoaded(String)
user clicks Flipkart logo                                   (any page)              clickLogo() → HomePage
homepage banners visible                                    HomePage                arePromoBannersVisible()
deals of the day visible                                    HomePage                isDealsOfTheDayVisible()


PAGES TO CREATE
───────────────────────────────────────────
com.automation.pages.flipkart.HomePage
com.automation.pages.flipkart.LoginModal
com.automation.pages.flipkart.SearchResultsPage
com.automation.pages.flipkart.ProductDetailPage
com.automation.pages.flipkart.CartPage
com.automation.pages.flipkart.CheckoutPage
com.automation.pages.flipkart.CategoryPage


SKIP (tag @wip) — OTP-gated, cannot automate
───────────────────────────────────────────
Scenario: Login with valid credentials
Scenario: Login fails with invalid credentials
Scenario: Logged-in user can add product to wishlist