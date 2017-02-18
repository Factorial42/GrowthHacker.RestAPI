package com.growthhacker.googleanalytics.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyStatsResponse {

	@JsonProperty("new_brands")
	private Map<String, String> newBrands;
	
	@JsonProperty("brands_updated_count")
	private Long brandsUpdatedCount;
	
	@JsonProperty("brands_updated_data_count")
	private Long brandsUpdatedDataCount;

	/**
	 * @return the newBrands
	 */
	public Map<String, String> getNewBrands() {
		return newBrands;
	}

	/**
	 * @param newBrands the newBrands to set
	 */
	public void setNewBrands(Map<String, String> newBrands) {
		this.newBrands = newBrands;
	}

	/**
	 * @return the brandsUpdatedCount
	 */
	public Long getBrandsUpdatedCount() {
		return brandsUpdatedCount;
	}

	/**
	 * @param brandsUpdatedCount the brandsUpdatedCount to set
	 */
	public void setBrandsUpdatedCount(Long brandsUpdatedCount) {
		this.brandsUpdatedCount = brandsUpdatedCount;
	}

	/**
	 * @return the brandsUpdatedDataCount
	 */
	public Long getBrandsUpdatedDataCount() {
		return brandsUpdatedDataCount;
	}

	/**
	 * @param brandsUpdatedDataCount the brandsUpdatedDataCount to set
	 */
	public void setBrandsUpdatedDataCount(Long brandsUpdatedDataCount) {
		this.brandsUpdatedDataCount = brandsUpdatedDataCount;
	}
	
}
