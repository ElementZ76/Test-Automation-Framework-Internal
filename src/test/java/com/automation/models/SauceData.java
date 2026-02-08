package com.automation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Single POJO model class for all test data
 * Contains nested static classes for structure
 */
public class SauceData {
    
    @JsonProperty("user")
    private User user;
    
    @JsonProperty("checkoutInfo")
    private CheckoutInfo checkoutInfo;
    
    @JsonProperty("productsToAdd")
    private List<Product> productsToAdd;
    
    @JsonProperty("expectedMessages")
    private ExpectedMessages expectedMessages;
    
    @JsonProperty("urls")
    private Urls urls;
    
    // Main Getters and Setters
    	// User
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    	// CheckoutInfo
    public CheckoutInfo getCheckoutInfo() {
        return checkoutInfo;
    }
    
    public void setCheckoutInfo(CheckoutInfo checkoutInfo) {
        this.checkoutInfo = checkoutInfo;
    }
    
    	
    	// Products
    public List<Product> getProductsToAdd() {
        return productsToAdd;
    }
    
    public void setProductsToAdd(List<Product> productsToAdd) {
        this.productsToAdd = productsToAdd;
    }
    
    	// Expected messages
    public ExpectedMessages getExpectedMessages() {
        return expectedMessages;
    }
    
    public void setExpectedMessages(ExpectedMessages expectedMessages) {
        this.expectedMessages = expectedMessages;
    }
    
    
    	// URLs
    public Urls getUrls() {
        return urls;
    }
    
    public void setUrls(Urls urls) {
        this.urls = urls;
    }
    
    // ==================== NESTED CLASSES ====================
    
    /**
     * User credentials
     */
    public static class User {
        @JsonProperty("username")
        private String username;
        
        @JsonProperty("password")
        private String password;
        
        public User() {
        }
        
        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        @Override
        public String toString() {
            return "User{username='" + username + "'}";
        }
    }
    
    /**
     * Checkout information
     */
    public static class CheckoutInfo {
        @JsonProperty("firstName")
        private String firstName;
        
        @JsonProperty("lastName")
        private String lastName;
        
        @JsonProperty("postalCode")
        private String postalCode;
        
        public CheckoutInfo() {
        }
        
        public CheckoutInfo(String firstName, String lastName, String postalCode) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.postalCode = postalCode;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        public String getPostalCode() {
            return postalCode;
        }
        
        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
        
        @Override
        public String toString() {
            return "CheckoutInfo{firstName='" + firstName + "', lastName='" + lastName + "', postalCode='" + postalCode + "'}";
        }
    }
    
    /**
     * Product details
     */
    public static class Product {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("price")
        private double price;
        
        public Product() {
        }
        
        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public double getPrice() {
            return price;
        }
        
        public void setPrice(double price) {
            this.price = price;
        }
        
        @Override
        public String toString() {
            return "Product{name='" + name + "', price=" + price + "}";
        }
    }
    
    /**
     * Expected messages for assertions
     */
    public static class ExpectedMessages {
        @JsonProperty("orderConfirmation")
        private String orderConfirmation;
        
        @JsonProperty("checkoutComplete")
        private String checkoutComplete;
        
        public String getOrderConfirmation() {
            return orderConfirmation;
        }
        
        public void setOrderConfirmation(String orderConfirmation) {
            this.orderConfirmation = orderConfirmation;
        }
        
        public String getCheckoutComplete() {
            return checkoutComplete;
        }
        
        public void setCheckoutComplete(String checkoutComplete) {
            this.checkoutComplete = checkoutComplete;
        }
    }
    
    /**
     * Application URLs
     */
    public static class Urls {
        @JsonProperty("baseUrl")
        private String baseUrl;
        
        @JsonProperty("inventoryUrl")
        private String inventoryUrl;
        
        @JsonProperty("cartUrl")
        private String cartUrl;
        
        @JsonProperty("checkoutCompleteUrl")
        private String checkoutCompleteUrl;
        
        public String getBaseUrl() {
            return baseUrl;
        }
        
        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
        
        public String getInventoryUrl() {
            return inventoryUrl;
        }
        
        public void setInventoryUrl(String inventoryUrl) {
            this.inventoryUrl = inventoryUrl;
        }
        
        public String getCartUrl() {
            return cartUrl;
        }
        
        public void setCartUrl(String cartUrl) {
            this.cartUrl = cartUrl;
        }
        
        public String getCheckoutCompleteUrl() {
            return checkoutCompleteUrl;
        }
        
        public void setCheckoutCompleteUrl(String checkoutCompleteUrl) {
            this.checkoutCompleteUrl = checkoutCompleteUrl;
        }
    }
}