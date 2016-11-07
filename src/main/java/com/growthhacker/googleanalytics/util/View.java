package com.growthhacker.googleanalytics.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class View {
	
	/** The account native id. */
	@JsonProperty("view_native_id")
	private String viewNativeId;
	@JsonProperty("view_id")
	private String viewId;
	@JsonProperty("view_name")
	private String viewName;
	@JsonProperty("view_tethered_user_email")
	private String viewTetheredUserEmail;
	@JsonProperty("view_currency")
	private String viewCurrency;
	@JsonProperty("view_timezone")
	private String viewTimezone;
	@JsonProperty("view_channel_type")
	private String viewChannelType;
	@JsonProperty("view_ecommerce_tracking")
	private Boolean viewEcommerceTracking;
	@JsonProperty("view_enhanced_ecommerce_tracking")
	private Boolean viewEnhancedEcommerceTracking;
	@JsonProperty("_id")
	private String id;

	/**
	 * @return the viewNativeId
	 */
	public String getViewNativeId() {
		return viewNativeId;
	}
	/**
	 * @param viewNativeId the viewNativeId to set
	 */
	public void setViewNativeId(String viewNativeId) {
		this.viewNativeId = viewNativeId;
	}
	/**
	 * @return the viewId
	 */
	public String getViewId() {
		return viewId;
	}
	/**
	 * @param viewId the viewId to set
	 */
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return the viewTetheredUserEmail
	 */
	public String getViewTetheredUserEmail() {
		return viewTetheredUserEmail;
	}
	/**
	 * @param viewTetheredUserEmail the viewTetheredUserEmail to set
	 */
	public void setViewTetheredUserEmail(String viewTetheredUserEmail) {
		this.viewTetheredUserEmail = viewTetheredUserEmail;
	}
	/**
	 * @return the viewCurrency
	 */
	public String getViewCurrency() {
		return viewCurrency;
	}
	/**
	 * @param viewCurrency the viewCurrency to set
	 */
	public void setViewCurrency(String viewCurrency) {
		this.viewCurrency = viewCurrency;
	}
	/**
	 * @return the viewTimezone
	 */
	public String getViewTimezone() {
		return viewTimezone;
	}
	/**
	 * @param viewTimezone the viewTimezone to set
	 */
	public void setViewTimezone(String viewTimezone) {
		this.viewTimezone = viewTimezone;
	}
	/**
	 * @return the viewChannelType
	 */
	public String getViewChannelType() {
		return viewChannelType;
	}
	/**
	 * @param viewChannelType the viewChannelType to set
	 */
	public void setViewChannelType(String viewChannelType) {
		this.viewChannelType = viewChannelType;
	}
	/**
	 * @return the viewEcommerceTracking
	 */
	public Boolean getViewEcommerceTracking() {
		return viewEcommerceTracking;
	}
	/**
	 * @param viewEcommerceTracking the viewEcommerceTracking to set
	 */
	public void setViewEcommerceTracking(Boolean viewEcommerceTracking) {
		this.viewEcommerceTracking = viewEcommerceTracking;
	}	
	/**
	 * @return the viewEnhancedEcommerceTracking
	 */
	public Boolean getViewEnhancedEcommerceTracking() {
		return viewEnhancedEcommerceTracking;
	}
	/**
	 * @param viewEnhancedEcommerceTracking the viewEnhancedEcommerceTracking to set
	 */
	public void setViewEnhancedEcommerceTracking(
			Boolean viewEnhancedEcommerceTracking) {
		this.viewEnhancedEcommerceTracking = viewEnhancedEcommerceTracking;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
