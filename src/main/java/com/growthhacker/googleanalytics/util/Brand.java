package com.growthhacker.googleanalytics.util;

import java.util.ArrayList;
import java.util.List;

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
public class Brand {

	/** The id. */
	@JsonProperty("_id")
	private String id;
	
	/** The updated at. */
	@JsonProperty("updatedAt")
	private String updatedAt;
	
	/** The created at. */
	@JsonProperty("createdAt")
	private String createdAt;
	
	/** The timestamp. */
	@JsonProperty("@timestamp")
	private String timestamp;
	
	/** The account tetherer email. */
	@JsonProperty("account_tetherer_email")
	private String accountTethererEmail;
	
	/** The account native id. */
	@JsonProperty("account_native_id")
	private String accountNativeId;
	
	/** The account default profile id. */
	@JsonProperty("account_default_profile_id")
	private String accountDefaultProfileId;
	
	/** The account website url. */
	@JsonProperty("account_website_url")
	private String accountWebsiteUrl;
	
	/** The account industry vertical. */
	@JsonProperty("account_industry_vertical")
	private String accountIndustryVertical;
	
	/** The account refresh oauthtoken. */
	@JsonProperty("account_refresh_oauthtoken")
	private String accountRefreshOauthtoken;
	
	/** The account oauthtoken. */
	@JsonProperty("account_oauthtoken")
	private String accountOauthtoken;
	
	/** The account updated. */
	@JsonProperty("account_updated")
	private String accountUpdated;
	
	/** The account created. */
	@JsonProperty("account_created")
	private String accountCreated;
	
	/** The account source. */
	@JsonProperty("account_source")
	private String accountSource;
	
	/** The account id. */
	@JsonProperty("account_id")
	private String accountId;
	
	/** The account name. */
	@JsonProperty("account_name")
	private String accountName;
	
	/** The views. */
	@JsonProperty("views")
	private List<View> views = new ArrayList<View>();
	
	/** The account record lastrefresh. */
	@JsonProperty("account_record_lastrefresh")
	private Integer accountRecordLastrefresh;
	
	/** The account record total. */
	@JsonProperty("account_record_total")
	private Integer accountRecordTotal;
	
	/** The account record status. */
	@JsonProperty("account_record_lastrefresh_status")
	private String accountRecordStatus;
	
	/** The account record lastrefresh start timestamp. */
	@JsonProperty("account_record_lastrefresh_start_timestamp")
	private Long accountRecordLastrefreshStartTimestamp;
	
	/** The account record lastrefresh end timestamp. */
	@JsonProperty("account_record_lastrefresh_end_timestamp")
	private Long accountRecordLastrefreshEndTimestamp;
	
	/** The account tags. */
	@JsonProperty("account_tags")
	private List<String> accountTags = new ArrayList<String>();
	
	/** The v. */
	@JsonProperty("__v")
	private Integer v;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the updated at.
	 *
	 * @return the updated at
	 */
	public String getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Sets the updated at.
	 *
	 * @param updatedAt the new updated at
	 */
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * Gets the created at.
	 *
	 * @return the created at
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the created at.
	 *
	 * @param createdAt the new created at
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the account tetherer email.
	 *
	 * @return the account tetherer email
	 */
	public String getAccountTethererEmail() {
		return accountTethererEmail;
	}

	/**
	 * Sets the account tetherer email.
	 *
	 * @param accountTethererEmail the new account tetherer email
	 */
	public void setAccountTethererEmail(String accountTethererEmail) {
		this.accountTethererEmail = accountTethererEmail;
	}

	/**
	 * Gets the account native id.
	 *
	 * @return the account native id
	 */
	public String getAccountNativeId() {
		return accountNativeId;
	}

	/**
	 * Sets the account native id.
	 *
	 * @param accountNativeId the new account native id
	 */
	public void setAccountNativeId(String accountNativeId) {
		this.accountNativeId = accountNativeId;
	}

	/**
	 * Gets the account default profile id.
	 *
	 * @return the account default profile id
	 */
	public String getAccountDefaultProfileId() {
		return accountDefaultProfileId;
	}

	/**
	 * Sets the account default profile id.
	 *
	 * @param accountDefaultProfileId the new account default profile id
	 */
	public void setAccountDefaultProfileId(String accountDefaultProfileId) {
		this.accountDefaultProfileId = accountDefaultProfileId;
	}

	/**
	 * Gets the account website url.
	 *
	 * @return the account website url
	 */
	public String getAccountWebsiteUrl() {
		return accountWebsiteUrl;
	}

	/**
	 * Sets the account website url.
	 *
	 * @param accountWebsiteUrl the new account website url
	 */
	public void setAccountWebsiteUrl(String accountWebsiteUrl) {
		this.accountWebsiteUrl = accountWebsiteUrl;
	}

	/**
	 * Gets the account industry vertical.
	 *
	 * @return the account industry vertical
	 */
	public String getAccountIndustryVertical() {
		return accountIndustryVertical;
	}

	/**
	 * Sets the account industry vertical.
	 *
	 * @param accountIndustryVertical the new account industry vertical
	 */
	public void setAccountIndustryVertical(String accountIndustryVertical) {
		this.accountIndustryVertical = accountIndustryVertical;
	}

	/**
	 * Gets the account refresh oauthtoken.
	 *
	 * @return the account refresh oauthtoken
	 */
	public String getAccountRefreshOauthtoken() {
		return accountRefreshOauthtoken;
	}

	/**
	 * Sets the account refresh oauthtoken.
	 *
	 * @param accountRefreshOauthtoken the new account refresh oauthtoken
	 */
	public void setAccountRefreshOauthtoken(String accountRefreshOauthtoken) {
		this.accountRefreshOauthtoken = accountRefreshOauthtoken;
	}

	/**
	 * Gets the account oauthtoken.
	 *
	 * @return the account oauthtoken
	 */
	public String getAccountOauthtoken() {
		return accountOauthtoken;
	}

	/**
	 * Sets the account oauthtoken.
	 *
	 * @param accountOauthtoken the new account oauthtoken
	 */
	public void setAccountOauthtoken(String accountOauthtoken) {
		this.accountOauthtoken = accountOauthtoken;
	}

	/**
	 * Gets the account updated.
	 *
	 * @return the account updated
	 */
	public String getAccountUpdated() {
		return accountUpdated;
	}

	/**
	 * Sets the account updated.
	 *
	 * @param accountUpdated the new account updated
	 */
	public void setAccountUpdated(String accountUpdated) {
		this.accountUpdated = accountUpdated;
	}

	/**
	 * Gets the account created.
	 *
	 * @return the account created
	 */
	public String getAccountCreated() {
		return accountCreated;
	}

	/**
	 * Sets the account created.
	 *
	 * @param accountCreated the new account created
	 */
	public void setAccountCreated(String accountCreated) {
		this.accountCreated = accountCreated;
	}

	/**
	 * Gets the account source.
	 *
	 * @return the account source
	 */
	public String getAccountSource() {
		return accountSource;
	}

	/**
	 * Sets the account source.
	 *
	 * @param accountSource the new account source
	 */
	public void setAccountSource(String accountSource) {
		this.accountSource = accountSource;
	}

	/**
	 * Gets the account id.
	 *
	 * @return the account id
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * Sets the account id.
	 *
	 * @param accountId the new account id
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * Gets the account name.
	 *
	 * @return the account name
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * Sets the account name.
	 *
	 * @param accountName the new account name
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * Gets the views.
	 *
	 * @return the views
	 */
	public List<View> getViews() {
		return views;
	}

	/**
	 * Sets the views.
	 *
	 * @param views the new views
	 */
	public void setViews(List<View> views) {
		this.views = views;
	}

	/**
	 * Gets the account tags.
	 *
	 * @return the account tags
	 */
	public List<String> getAccountTags() {
		return accountTags;
	}

	/**
	 * Sets the account tags.
	 *
	 * @param accountTags the new account tags
	 */
	public void setAccountTags(List<String> accountTags) {
		this.accountTags = accountTags;
	}

	/**
	 * Gets the v.
	 *
	 * @return the v
	 */
	public Integer getV() {
		return v;
	}

	/**
	 * Sets the v.
	 *
	 * @param v the new v
	 */
	public void setV(Integer v) {
		this.v = v;
	}

	/**
	 * Gets the account record lastrefresh.
	 *
	 * @return the account record lastrefresh
	 */
	public Integer getAccountRecordLastrefresh() {
		return accountRecordLastrefresh;
	}

	/**
	 * Sets the account record lastrefresh.
	 *
	 * @param accountRecordLastrefresh the new account record lastrefresh
	 */
	public void setAccountRecordLastrefresh(Integer accountRecordLastrefresh) {
		this.accountRecordLastrefresh = accountRecordLastrefresh;
	}

	/**
	 * Gets the account record total.
	 *
	 * @return the account record total
	 */
	public Integer getAccountRecordTotal() {
		return accountRecordTotal;
	}

	/**
	 * Sets the account record total.
	 *
	 * @param accountRecordTotal the new account record total
	 */
	public void setAccountRecordTotal(Integer accountRecordTotal) {
		this.accountRecordTotal = accountRecordTotal;
	}

	/**
	 * Gets the account record status.
	 *
	 * @return the account record status
	 */
	public String getAccountRecordStatus() {
		return accountRecordStatus;
	}

	/**
	 * Sets the account record status.
	 *
	 * @param accountRecordStatus the new account record status
	 */
	public void setAccountRecordStatus(String accountRecordStatus) {
		this.accountRecordStatus = accountRecordStatus;
	}

	/**
	 * Gets the account record lastrefresh start timestamp.
	 *
	 * @return the account record lastrefresh start timestamp
	 */
	public Long getAccountRecordLastrefreshStartTimestamp() {
		return accountRecordLastrefreshStartTimestamp;
	}

	/**
	 * Sets the account record lastrefresh start timestamp.
	 *
	 * @param accountRecordLastrefreshStartTimestamp the new account record lastrefresh start timestamp
	 */
	public void setAccountRecordLastrefreshStartTimestamp(
			Long accountRecordLastrefreshStartTimestamp) {
		this.accountRecordLastrefreshStartTimestamp = accountRecordLastrefreshStartTimestamp;
	}

	/**
	 * Gets the account record lastrefresh end timestamp.
	 *
	 * @return the account record lastrefresh end timestamp
	 */
	public Long getAccountRecordLastrefreshEndTimestamp() {
		return accountRecordLastrefreshEndTimestamp;
	}

	/**
	 * Sets the account record lastrefresh end timestamp.
	 *
	 * @param accountRecordLastrefreshEndTimestamp the new account record lastrefresh end timestamp
	 */
	public void setAccountRecordLastrefreshEndTimestamp(
			Long accountRecordLastrefreshEndTimestamp) {
		this.accountRecordLastrefreshEndTimestamp = accountRecordLastrefreshEndTimestamp;
	}

	/**
	 * The Class BrandIngestRunUpdateView.
	 */
	public class BrandIngestRunUpdateView {
		
		/** The updated at. */
		@JsonProperty("updatedAt")
		private String updatedAt;
		
		/** The timestamp. */
		@JsonProperty("@timestamp")
		private String timestamp;
		
		/** The account id. */
		@JsonProperty("account_id")
		private String accountId;
		
		/** The account record lastrefresh. */
		@JsonProperty("account_record_lastrefresh")
		private Integer accountRecordLastrefresh;
		
		/** The account record status. */
		@JsonProperty("account_record_lastrefresh_status")
		private String accountRecordStatus;
		
		/** The account record lastrefresh start timestamp. */
		@JsonProperty("account_record_lastrefresh_start_timestamp")
		private Long accountRecordLastrefreshStartTimestamp;
		
		/** The account record lastrefresh end timestamp. */
		@JsonProperty("account_record_lastrefresh_end_timestamp")
		private Long accountRecordLastrefreshEndTimestamp;
		
		/** The account refresh oauthtoken. */
		@JsonProperty("account_refresh_oauthtoken")
		private String accountRefreshOauthtoken;
		
		/** The account oauthtoken. */
		@JsonProperty("account_oauthtoken")
		private String accountOauthtoken;

		/**
		 * Gets the account id.
		 *
		 * @return the account id
		 */
		public String getAccountId() {
			return this.accountId;
		}

		/**
		 * Sets the account id.
		 *
		 * @param accountId the new account id
		 */
		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		/**
		 * Gets the account record lastrefresh.
		 *
		 * @return the account record lastrefresh
		 */
		public Integer getAccountRecordLastrefresh() {
			return this.accountRecordLastrefresh;
		}

		/**
		 * Sets the account record lastrefresh.
		 *
		 * @param accountRecordLastrefresh the new account record lastrefresh
		 */
		public void setAccountRecordLastrefresh(Integer accountRecordLastrefresh) {
			this.accountRecordLastrefresh = accountRecordLastrefresh;
		}

		/**
		 * Gets the account record status.
		 *
		 * @return the account record status
		 */
		public String getAccountRecordStatus() {
			return this.accountRecordStatus;
		}

		/**
		 * Sets the account record status.
		 *
		 * @param accountRecordStatus the new account record status
		 */
		public void setAccountRecordStatus(String accountRecordStatus) {
			this.accountRecordStatus = accountRecordStatus;
		}

		/**
		 * Gets the account record lastrefresh start timestamp.
		 *
		 * @return the account record lastrefresh start timestamp
		 */
		public Long getAccountRecordLastrefreshStartTimestamp() {
			return this.accountRecordLastrefreshStartTimestamp;
		}

		/**
		 * Sets the account record lastrefresh start timestamp.
		 *
		 * @param accountRecordLastrefreshStartTimestamp the new account record lastrefresh start timestamp
		 */
		public void setAccountRecordLastrefreshStartTimestamp(
				Long accountRecordLastrefreshStartTimestamp) {
			this.accountRecordLastrefreshStartTimestamp = accountRecordLastrefreshStartTimestamp;
		}

		/**
		 * Gets the account record lastrefresh end timestamp.
		 *
		 * @return the account record lastrefresh end timestamp
		 */
		public Long getAccountRecordLastrefreshEndTimestamp() {
			return this.accountRecordLastrefreshEndTimestamp;
		}

		/**
		 * Sets the account record lastrefresh end timestamp.
		 *
		 * @param accountRecordLastrefreshEndTimestamp the new account record lastrefresh end timestamp
		 */
		public void setAccountRecordLastrefreshEndTimestamp(
				Long accountRecordLastrefreshEndTimestamp) {
			this.accountRecordLastrefreshEndTimestamp = accountRecordLastrefreshEndTimestamp;
		}

		/**
		 * Gets the account refresh oauthtoken.
		 *
		 * @return the account refresh oauthtoken
		 */
		public String getAccountRefreshOauthtoken() {
			return this.accountRefreshOauthtoken;
		}

		/**
		 * Sets the account refresh oauthtoken.
		 *
		 * @param accountRefreshOauthtoken the new account refresh oauthtoken
		 */
		public void setAccountRefreshOauthtoken(String accountRefreshOauthtoken) {
			this.accountRefreshOauthtoken = accountRefreshOauthtoken;
		}

		/**
		 * Gets the account oauthtoken.
		 *
		 * @return the account oauthtoken
		 */
		public String getAccountOauthtoken() {
			return this.accountOauthtoken;
		}

		/**
		 * Sets the account oauthtoken.
		 *
		 * @param accountOauthtoken the new account oauthtoken
		 */
		public void setAccountOauthtoken(String accountOauthtoken) {
			this.accountOauthtoken = accountOauthtoken;
		}

		/**
		 * Gets the updated at.
		 *
		 * @return the updated at
		 */
		public String getUpdatedAt() {
			return this.updatedAt;
		}

		/**
		 * Sets the updated at.
		 *
		 * @param updatedAt the new updated at
		 */
		public void setUpdatedAt(String updatedAt) {
			this.updatedAt = updatedAt;
		}

		/**
		 * Gets the timestamp.
		 *
		 * @return the timestamp
		 */
		public String getTimestamp() {
			return this.timestamp;
		}

		/**
		 * Sets the timestamp.
		 *
		 * @param timestamp the new timestamp
		 */
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
	}
}
