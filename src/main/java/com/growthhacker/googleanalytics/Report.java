package com.growthhacker.googleanalytics;

import java.util.Collections;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Report {

	@JsonProperty("name")
	@NotEmpty
	private String name;

	@JsonProperty("writeToIndex")
	@NotEmpty
	private String writeToIndex;

	@JsonProperty("writeToType")
	@NotEmpty
	private String writeToType;

	@JsonProperty("prefix")
	@NotEmpty
	private String prefix;

	@JsonProperty("dimensions")
	@NotEmpty
	private List<String> dimensions = Collections.emptyList();

	@JsonProperty("metrics")
	@NotEmpty
	private List<String> metrics = Collections.emptyList();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the dimensions
	 */
	public List<String> getDimensions() {
		return dimensions;
	}

	/**
	 * @param dimensions
	 *            the dimensions to set
	 */
	public void setDimensions(List<String> dimensions) {
		this.dimensions = dimensions;
	}

	/**
	 * @return the metrics
	 */
	public List<String> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics
	 *            the metrics to set
	 */
	public void setMetrics(List<String> metrics) {
		this.metrics = metrics;
	}

	/**
	 * @return the writeToIndex
	 */
	public String getWriteToIndex() {
		return writeToIndex;
	}

	/**
	 * @param writeToIndex the writeToIndex to set
	 */
	public void setWriteToIndex(String writeToIndex) {
		this.writeToIndex = writeToIndex;
	}

	/**
	 * @return the writeToType
	 */
	public String getWriteToType() {
		return writeToType;
	}

	/**
	 * @param writeToType the writeToType to set
	 */
	public void setWriteToType(String writeToType) {
		this.writeToType = writeToType;
	}
}
