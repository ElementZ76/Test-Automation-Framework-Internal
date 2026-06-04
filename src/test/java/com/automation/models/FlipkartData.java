package com.automation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlipkartData {

    @JsonProperty("caseId")
    private int caseId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("searchTerm")
    private String searchTerm;

    @JsonProperty("brandFilter")
    private String brandFilter;

    @JsonProperty("priceMin")
    private Integer priceMin;

    @JsonProperty("priceMax")
    private long priceMax;

    @JsonProperty("sortOption")
    private String sortOption;

    @JsonProperty("expectedBehavior")
    private String expectedBehavior;

    public int getCaseId()                  { return caseId; }
    public String getDescription()          { return description; }
    public String getSearchTerm()           { return searchTerm; }
    public String getBrandFilter()          { return brandFilter; }
    public Integer getPriceMin()            { return priceMin; }
    public long getPriceMax()            { return priceMax; }
    public String getSortOption()           { return sortOption; }
    public String getExpectedBehavior()     { return expectedBehavior; }

    public void setCaseId(int caseId)                       { this.caseId = caseId; }
    public void setDescription(String description)          { this.description = description; }
    public void setSearchTerm(String searchTerm)            { this.searchTerm = searchTerm; }
    public void setBrandFilter(String brandFilter)          { this.brandFilter = brandFilter; }
    public void setPriceMin(Integer priceMin)               { this.priceMin = priceMin; }
    public void setPriceMax(long priceMax)               { this.priceMax = priceMax; }
    public void setSortOption(String sortOption)            { this.sortOption = sortOption; }
    public void setExpectedBehavior(String expectedBehavior){ this.expectedBehavior = expectedBehavior; }

    @Override
    public String toString() {
        return "FlipkartData{caseId=" + caseId + ", searchTerm='" + searchTerm + "'}";
    }
}