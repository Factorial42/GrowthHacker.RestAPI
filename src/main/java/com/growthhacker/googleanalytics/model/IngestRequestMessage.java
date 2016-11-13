package com.growthhacker.googleanalytics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class Brand.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestRequestMessage {

	@JsonProperty("brand")
	private Brand brand;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("endDate")
	private String endDate;

	@JsonProperty("forceStartDate")
	private Boolean forceStartDate;

	/**
	 * @return the brand
	 */
	public Brand getBrand() {
		return brand;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the forceStartDate
	 */
	public Boolean getForceStartDate() {
		return forceStartDate;
	}

	/**
	 * Sets the force start date.
	 *
	 * @param forceStartDate the new force start date
	 */
	public void setForceStartDate(Boolean forceStartDate) {
		this.forceStartDate = forceStartDate;
	}
}
