package com.growthhacker.googleanalytics;

import java.util.Collections;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IngestorConfiguration {

	@JsonProperty("historicStartDate")
	@NotEmpty
	private String historicStartDate;

	@JsonProperty("historicEndDate")
	@NotEmpty
	private String historicEndDate;

	@JsonProperty("samplingLevel")
	@NotEmpty
	private String samplingLevel;

	@JsonProperty("reports")
	@NotEmpty
	private List<Report> reports = Collections.emptyList();

	/**
	 * @return the historicStartDate
	 */
	public String getHistoricStartDate() {
		return historicStartDate;
	}

	/**
	 * @param historicStartDate
	 *            the historicStartDate to set
	 */
	public void setHistoricStartDate(String historicStartDate) {
		this.historicStartDate = historicStartDate;
	}

	/**
	 * @return the historicEndDate
	 */
	public String getHistoricEndDate() {
		return historicEndDate;
	}

	/**
	 * @param historicEndDate
	 *            the historicEndDate to set
	 */
	public void setHistoricEndDate(String historicEndDate) {
		this.historicEndDate = historicEndDate;
	}

	/**
	 * @return the samplingLevel
	 */
	public String getSamplingLevel() {
		return samplingLevel;
	}

	/**
	 * @param samplingLevel
	 *            the samplingLevel to set
	 */
	public void setSamplingLevel(String samplingLevel) {
		this.samplingLevel = samplingLevel;
	}

	/**
	 * @return the reports
	 */
	public List<Report> getReports() {
		return reports;
	}

	/**
	 * @param reports
	 *            the reports to set
	 */
	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
}