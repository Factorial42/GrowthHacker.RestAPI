package com.growthhacker.googleanalytics.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRecordCountsLastRefresh {

	/** The account record counts lastrefresh. */
	@JsonProperty("account_record_counts_lastrefresh_totals")
	private List<Map<String, Object>> accountRecordCountsLastrefreshTotals;

	/** The account record counts status. */
	@JsonProperty("account_record_counts_lastrefresh_status")
	private String accountRecordCountsLastrefreshStatus;

	/** The account record counts lastrefresh start timestamp. */
	@JsonProperty("account_record_counts_lastrefresh_start_timestamp")
	private Long accountRecordCountsLastrefreshStartTimestamp;

	/** The account record counts lastrefresh end timestamp. */
	@JsonProperty("account_record_counts_lastrefresh_end_timestamp")
	private Long accountRecordCountsLastrefreshEndTimestamp;

	@JsonProperty("account_record_counts_lastrefresh_start_date")
	private String accountRecordCountsLastrefreshStartDate;

	@JsonProperty("account_record_counts_lastrefresh_end_date")
	private String accountRecordCountsLastrefreshEndDate;

	/**
	 * @return the accountRecordCountsLastrefreshTotals
	 */
	public List<Map<String,Object>> getAccountRecordCountsLastrefreshTotals() {
		return accountRecordCountsLastrefreshTotals;
	}

	/**
	 * @param accountRecordCountsLastrefreshTotals the accountRecordCountsLastrefreshTotals to set
	 */
	public void setAccountRecordCountsLastrefreshTotals(
			List<Map<String, Object>> accountRecordCountsLastrefreshTotals) {
		this.accountRecordCountsLastrefreshTotals = accountRecordCountsLastrefreshTotals;
	}

	/**
	 * @return the accountRecordCountsLastrefreshStatus
	 */
	public String getAccountRecordCountsLastrefreshStatus() {
		return accountRecordCountsLastrefreshStatus;
	}

	/**
	 * @param accountRecordCountsLastrefreshStatus
	 *            the accountRecordCountsLastrefreshStatus to set
	 */
	public void setAccountRecordCountsLastrefreshStatus(
			String accountRecordCountsLastrefreshStatus) {
		this.accountRecordCountsLastrefreshStatus = accountRecordCountsLastrefreshStatus;
	}

	/**
	 * @return the accountRecordCountsLastrefreshStartTimestamp
	 */
	public Long getAccountRecordCountsLastrefreshStartTimestamp() {
		return accountRecordCountsLastrefreshStartTimestamp;
	}

	/**
	 * @param accountRecordCountsLastrefreshStartTimestamp
	 *            the accountRecordCountsLastrefreshStartTimestamp to set
	 */
	public void setAccountRecordCountsLastrefreshStartTimestamp(
			Long accountRecordCountsLastrefreshStartTimestamp) {
		this.accountRecordCountsLastrefreshStartTimestamp = accountRecordCountsLastrefreshStartTimestamp;
	}

	/**
	 * @return the accountRecordCountsLastrefreshEndTimestamp
	 */
	public Long getAccountRecordCountsLastrefreshEndTimestamp() {
		return accountRecordCountsLastrefreshEndTimestamp;
	}

	/**
	 * @param accountRecordCountsLastrefreshEndTimestamp
	 *            the accountRecordCountsLastrefreshEndTimestamp to set
	 */
	public void setAccountRecordCountsLastrefreshEndTimestamp(
			Long accountRecordCountsLastrefreshEndTimestamp) {
		this.accountRecordCountsLastrefreshEndTimestamp = accountRecordCountsLastrefreshEndTimestamp;
	}

	/**
	 * @return the accountRecordCountsLastrefreshStartDate
	 */
	public String getAccountRecordCountsLastrefreshStartDate() {
		return accountRecordCountsLastrefreshStartDate;
	}

	/**
	 * @param accountRecordCountsLastrefreshStartDate the accountRecordCountsLastrefreshStartDate to set
	 */
	public void setAccountRecordCountsLastrefreshStartDate(
			String accountRecordCountsLastrefreshStartDate) {
		this.accountRecordCountsLastrefreshStartDate = accountRecordCountsLastrefreshStartDate;
	}

	/**
	 * @return the accountRecordCountsLastrefreshEndDate
	 */
	public String getAccountRecordCountsLastrefreshEndDate() {
		return accountRecordCountsLastrefreshEndDate;
	}

	/**
	 * @param accountRecordCountsLastrefreshEndDate the accountRecordCountsLastrefreshEndDate to set
	 */
	public void setAccountRecordCountsLastrefreshEndDate(
			String accountRecordCountsLastrefreshEndDate) {
		this.accountRecordCountsLastrefreshEndDate = accountRecordCountsLastrefreshEndDate;
	}
}
