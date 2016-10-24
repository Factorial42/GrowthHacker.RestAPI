package com.growthhacker.googleanalytics.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brand {

	@JsonProperty("_id")
	private String id;
	@JsonProperty("updatedAt")
	private String updatedAt;
	@JsonProperty("createdAt")
	private String createdAt;
	@JsonProperty("account_tetherer_email")
	private String accountTethererEmail;
	@JsonProperty("account_native_id")
	private String accountNativeId;
	@JsonProperty("account_default_profile_id")
	private String accountDefaultProfileId;
	@JsonProperty("account_website_url")
	private String accountWebsiteUrl;
	@JsonProperty("account_industry_vertical")
	private String accountIndustryVertical;
	@JsonProperty("account_refresh_oauthtoken")
	private String accountRefreshOauthtoken;
	@JsonProperty("account_oauthtoken")
	private String accountOauthtoken;
	@JsonProperty("account_updated")
	private String accountUpdated;
	@JsonProperty("account_created")
	private String accountCreated;
	@JsonProperty("account_source")
	private String accountSource;
	@JsonProperty("account_id")
	private String accountId;
	@JsonProperty("account_name")
	private String accountName;
	@JsonProperty("views")
	private List<View> views = new ArrayList<View>();
	@JsonProperty("account_record_lastrefresh")
	private Integer accountRecordLastrefresh;
	@JsonProperty("account_record_total")
	private Integer accountRecordTotal;
	@JsonProperty("account_tags")
	private List<String> accountTags = new ArrayList<String>();
	@JsonProperty("__v")
	private Integer v;
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
	/**
	 * @return the updatedAt
	 */
	public String getUpdatedAt() {
		return updatedAt;
	}
	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	/**
	 * @return the accountTethererEmail
	 */
	public String getAccountTethererEmail() {
		return accountTethererEmail;
	}
	/**
	 * @param accountTethererEmail the accountTethererEmail to set
	 */
	public void setAccountTethererEmail(String accountTethererEmail) {
		this.accountTethererEmail = accountTethererEmail;
	}
	/**
	 * @return the accountNativeId
	 */
	public String getAccountNativeId() {
		return accountNativeId;
	}
	/**
	 * @param accountNativeId the accountNativeId to set
	 */
	public void setAccountNativeId(String accountNativeId) {
		this.accountNativeId = accountNativeId;
	}
	/**
	 * @return the accountDefaultProfileId
	 */
	public String getAccountDefaultProfileId() {
		return accountDefaultProfileId;
	}
	/**
	 * @param accountDefaultProfileId the accountDefaultProfileId to set
	 */
	public void setAccountDefaultProfileId(String accountDefaultProfileId) {
		this.accountDefaultProfileId = accountDefaultProfileId;
	}
	/**
	 * @return the accountWebsiteUrl
	 */
	public String getAccountWebsiteUrl() {
		return accountWebsiteUrl;
	}
	/**
	 * @param accountWebsiteUrl the accountWebsiteUrl to set
	 */
	public void setAccountWebsiteUrl(String accountWebsiteUrl) {
		this.accountWebsiteUrl = accountWebsiteUrl;
	}
	/**
	 * @return the accountIndustryVertical
	 */
	public String getAccountIndustryVertical() {
		return accountIndustryVertical;
	}
	/**
	 * @param accountIndustryVertical the accountIndustryVertical to set
	 */
	public void setAccountIndustryVertical(String accountIndustryVertical) {
		this.accountIndustryVertical = accountIndustryVertical;
	}
	/**
	 * @return the accountRefreshOauthtoken
	 */
	public String getAccountRefreshOauthtoken() {
		return accountRefreshOauthtoken;
	}
	/**
	 * @param accountRefreshOauthtoken the accountRefreshOauthtoken to set
	 */
	public void setAccountRefreshOauthtoken(String accountRefreshOauthtoken) {
		this.accountRefreshOauthtoken = accountRefreshOauthtoken;
	}
	/**
	 * @return the accountOauthtoken
	 */
	public String getAccountOauthtoken() {
		return accountOauthtoken;
	}
	/**
	 * @param accountOauthtoken the accountOauthtoken to set
	 */
	public void setAccountOauthtoken(String accountOauthtoken) {
		this.accountOauthtoken = accountOauthtoken;
	}
	/**
	 * @return the accountUpdated
	 */
	public String getAccountUpdated() {
		return accountUpdated;
	}
	/**
	 * @param accountUpdated the accountUpdated to set
	 */
	public void setAccountUpdated(String accountUpdated) {
		this.accountUpdated = accountUpdated;
	}
	/**
	 * @return the accountCreated
	 */
	public String getAccountCreated() {
		return accountCreated;
	}
	/**
	 * @param accountCreated the accountCreated to set
	 */
	public void setAccountCreated(String accountCreated) {
		this.accountCreated = accountCreated;
	}
	/**
	 * @return the accountSource
	 */
	public String getAccountSource() {
		return accountSource;
	}
	/**
	 * @param accountSource the accountSource to set
	 */
	public void setAccountSource(String accountSource) {
		this.accountSource = accountSource;
	}
	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	/**
	 * @return the views
	 */
	public List<View> getViews() {
		return views;
	}
	/**
	 * @param views the views to set
	 */
	public void setViews(List<View> views) {
		this.views = views;
	}
	/**
	 * @return the accountTags
	 */
	public List<String> getAccountTags() {
		return accountTags;
	}
	/**
	 * @param accountTags the accountTags to set
	 */
	public void setAccountTags(List<String> accountTags) {
		this.accountTags = accountTags;
	}
	/**
	 * @return the v
	 */
	public Integer getV() {
		return v;
	}
	/**
	 * @param v the v to set
	 */
	public void setV(Integer v) {
		this.v = v;
	}	
}
