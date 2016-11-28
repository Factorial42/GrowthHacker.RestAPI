package com.growthhacker.googleanalytics.resources;

import io.interact.sqsdw.MessageHandler;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.script.Template;
import org.elasticsearch.script.mustache.MustacheScriptEngineService;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.Message;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError.ErrorInfo;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.growthhacker.googleanalytics.GoogleAnalyticsConfiguration;
import com.growthhacker.googleanalytics.IngestorConfiguration;
import com.growthhacker.googleanalytics.Report;
import com.growthhacker.googleanalytics.model.Brand;
import com.growthhacker.googleanalytics.model.Brand.BrandCountsRunUpdateView;
import com.growthhacker.googleanalytics.model.Brand.BrandIngestRunUpdateView;
import com.growthhacker.googleanalytics.model.IngestRequestMessage;
import com.growthhacker.googleanalytics.model.View;
import com.growthhacker.googleanalytics.util.StringUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class GoogleAnalyticsResource.
 */
@Path("/googleAnalytics")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class GoogleAnalyticsResource extends MessageHandler {

	/** The Constant logger. */
	final static Logger logger = LoggerFactory
			.getLogger(GoogleAnalyticsResource.class);

	/** The Constant ACCOUNT_ID_REQUIRED. */
	public final static String ACCOUNT_ID_REQUIRED = "account_id is a required field for this API call";

	/** The Constant START_DATE_REQUIRED. */
	public final static String START_DATE_REQUIRED = "startDate is a required field if forceStartDate is true for this API call";

	/** The application name. */
	private static String APPLICATION_NAME = "GrowthHacker Analytics Resource";

	/** The brand index. */
	private static String BRAND_INDEX = "brands";

	/** The brand type. */
	private static String BRAND_TYPE = "brand";

	/** The analytics search template. */
	private static String ANALYTICS_SEARCH_TEMPLATE = "st.analytics";

	private static String TOTAL_COUNT = "ingested_total";
	private static String REPORTS_ROWS_COUNT = "report_rows_count";
	private static String REPORTS_ROWS_DATA = "report_rows_data";
	private static Integer REPORT_REQUEST_PAGE_SIZE = 10000;

	/** The Constant JSON_FACTORY. */
	private static final JsonFactory JSON_FACTORY = GsonFactory
			.getDefaultInstance();

	/** The http transport. */
	private static NetHttpTransport httpTransport;

	/** The es client. */
	private Client esClient;

	/** The client secret resource configuration. */
	private GoogleAnalyticsConfiguration.ClientSecretResourceConfiguration clientSecretResourceConfiguration;

	/** The ingestor configuration. */
	private IngestorConfiguration ingestorConfiguration;

	/** The google client secrets. */
	private GoogleClientSecrets googleClientSecrets;

	/** The mapper. */
	private ObjectMapper mapper;

	/**
	 * Instantiates a new google analytics resource.
	 *
	 * @param client
	 *            the client
	 * @param configuration
	 *            the configuration
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws GeneralSecurityException
	 *             the general security exception
	 */
	public GoogleAnalyticsResource(Client client,
			GoogleAnalyticsConfiguration configuration) throws IOException,
			GeneralSecurityException {
		super("GHBrandIngestType");
		this.mapper = new ObjectMapper();
		this.esClient = client;

		this.clientSecretResourceConfiguration = configuration
				.getClientSecretResourceConfiguration();
		this.ingestorConfiguration = configuration.getIngestorConfiguration();

		this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.interact.sqsdw.MessageHandler#handle(com.amazonaws.services.sqs.model
	 * .Message)
	 */
	public void handle(Message message) {
		try {
			logger.info("Message received:",
					this.mapper.writeValueAsString(message));
			IngestRequestMessage ingestRequestMessage = null;
			Brand brand = null;
			String startDate = null, endDate = null;
			Boolean forceStartDate = false, justCounts = false;

			String body = message.getBody();
			if (body == null && body.isEmpty()) {
				logger.error("Bad Message, missing body", brand.toString());
				return;
			}
			ingestRequestMessage = mapper.readValue(body,
					IngestRequestMessage.class);
			if (ingestRequestMessage == null) {
				logger.error("Bad Request, null message");
			}
			brand = ingestRequestMessage.getBrand();
			if (brand == null) {
				logger.error("Bad Request, null brand");
			}
			if (brand.getAccountId() == null && brand.getAccountId().isEmpty()) {
				logger.error("Bad Request, missing account_id",
						brand.toString());
				return;
			}

			if (ingestRequestMessage.getForceStartDate() != null) {
				forceStartDate = ingestRequestMessage.getForceStartDate();
			}
			if (forceStartDate
					&& (ingestRequestMessage.getStartDate() == null || ingestRequestMessage
							.getStartDate().isEmpty())) {
				logger.error("Bad Request, missing startDate");
				return;
			}

			if (ingestRequestMessage.getJustCounts() != null) {
				justCounts = ingestRequestMessage.getJustCounts();
			}
			startDate = ingestRequestMessage.getStartDate();
			endDate = ingestRequestMessage.getEndDate();
			if (justCounts) {
				BrandCountsRunUpdateView brandCountsRunUpdateView = handleCountRequest(
						brand, startDate, endDate, forceStartDate);
				logger.debug("Processed message:"
						+ brandCountsRunUpdateView.toString());
			} else {
				BrandIngestRunUpdateView brandIngestRunUpdateView = handleIngestRequest(
						brand, startDate, endDate, forceStartDate);
				logger.debug("Processed message:"
						+ brandIngestRunUpdateView.toString());
			}

		} catch (IOException e) {
			logger.error("Could not process message:", message, e);
		}
	}

	/**
	 * Ingest data.
	 *
	 * @param brand
	 *            the brand
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param forceStartDate
	 *            the force start date
	 * @return the response
	 */
	@POST
	@Timed
	@Path("/ingestData")
	public Response ingestData(
			Brand brand,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate,
			@QueryParam("forceStartDate") @DefaultValue("false") Boolean forceStartDate) {
		if (brand.getAccountId() == null && brand.getAccountId().isEmpty())
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(ACCOUNT_ID_REQUIRED).build();
		if (forceStartDate && (startDate == null || startDate.isEmpty()))
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(START_DATE_REQUIRED).build();

		BrandIngestRunUpdateView brandIngestRunUpdateView = null;

		try {
			brandIngestRunUpdateView = handleIngestRequest(brand, startDate,
					endDate, forceStartDate);
		} catch (IOException e) {
			logger.error("Error in getting Data from Google Analytics:", e);
			brandIngestRunUpdateView
					.setAccountRecordStatus(Brand.STATUS_FAILURE);
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error in getting Data from Google Analytics:"
							+ e.getMessage()).build();
		}
		return Response.status(Response.Status.OK)
				.entity(brandIngestRunUpdateView).build();
	}

	@POST
	@Timed
	@Path("/updateCounts")
	public Response updateCounts(Brand brand,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {
		if (brand.getAccountId() == null && brand.getAccountId().isEmpty())
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(ACCOUNT_ID_REQUIRED).build();

		BrandCountsRunUpdateView brandCountsRunUpdateView = null;

		try {
			brandCountsRunUpdateView = handleCountRequest(brand, startDate,
					endDate, false);
		} catch (IOException e) {
			logger.error("Error in getting Data from Google Analytics:", e);
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error in getting Data from Google Analytics:"
							+ e.getMessage()).build();
		}
		return Response.status(Response.Status.OK)
				.entity(brandCountsRunUpdateView).build();
	}

	/**
	 * Handle ingest request.
	 *
	 * @param brand
	 *            the brand
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param forceStartDate
	 *            the force start date
	 * @return the brand ingest run update view
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private BrandIngestRunUpdateView handleIngestRequest(Brand brand,
			String startDate, String endDate, Boolean forceStartDate)
			throws IOException {
		BrandIngestRunUpdateView brandIngestRunUpdateView = Brand
				.createBrandIngestRunUpdateView(brand);

		GoogleCredential credential = buildCredentials(brand);

		AnalyticsReporting analyticsReportingService = new AnalyticsReporting.Builder(
				httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();

		// get data and persist
		Map<String, Integer> numberOfRowsCreated = getAndPersistReports(
				analyticsReportingService, brand, startDate, endDate,
				forceStartDate, false);

		Brand.updateBrandIngestRunUpdateViewWithTimestamp(
				brandIngestRunUpdateView, numberOfRowsCreated.get(TOTAL_COUNT),
				credential);

		// update Brand record in ES
		try {
			Thread.sleep(ingestorConfiguration
					.getSleepBetweenRequestsInMillis());

			UpdateResponse updateResponse = esClient
					.prepareUpdate(BRAND_INDEX, BRAND_TYPE,
							brand.getAccountId())
					.setDoc(this.mapper
							.writeValueAsString(brandIngestRunUpdateView))
					.setUpsert(
							this.mapper
									.writeValueAsString(brandIngestRunUpdateView))
					.execute().get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Could not update BrandId:{} for Ingest status:",
					brand.getAccountId(), e);
		}

		return brandIngestRunUpdateView;
	}

	private BrandCountsRunUpdateView handleCountRequest(Brand brand,
			String startDate, String endDate, Boolean forceStartDate)
			throws IOException {
		BrandCountsRunUpdateView brandCountsRunUpdateView = Brand
				.createBrandCountsRunUpdateView(brand);

		GoogleCredential credential = buildCredentials(brand);

		AnalyticsReporting analyticsReportingService = new AnalyticsReporting.Builder(
				httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();

		// get data and persist
		Map<String, Integer> numberOfRowsCreated = getAndPersistReports(
				analyticsReportingService, brand, startDate, endDate,
				forceStartDate, true);

		Brand.updateBrandCountsRunUpdateViewWithTimestamp(
				brandCountsRunUpdateView, numberOfRowsCreated, credential,
				startDate, endDate);

		// update Brand record in ES
		try {
			Thread.sleep(ingestorConfiguration
					.getSleepBetweenRequestsInMillis());

			UpdateResponse updateResponse = esClient
					.prepareUpdate(BRAND_INDEX, BRAND_TYPE,
							brand.getAccountId())
					.setDoc(this.mapper
							.writeValueAsString(brandCountsRunUpdateView))
					.setUpsert(
							this.mapper
									.writeValueAsString(brandCountsRunUpdateView))
					.execute().get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Could not update BrandId:{} for Ingest status:",
					brand.getAccountId(), e);
		}

		return brandCountsRunUpdateView;
	}

	/**
	 * Gets the and persist reports.
	 *
	 * @param analyticsReportingService
	 *            the analytics reporting service
	 * @param brand
	 *            the brand
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param forceStartDate
	 *            the force start date
	 * @return the and persist reports
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Map<String, Integer> getAndPersistReports(
			AnalyticsReporting analyticsReportingService, Brand brand,
			String startDate, String endDate, Boolean forceStartDate,
			Boolean justCounts) throws IOException {
		int numberOfRowsCreated = 0;
		List<JsonObject> results = new ArrayList<>();
		List<Map<String, Object>> tempResults = null;
		Map<String, Integer> counts = new HashMap<>();
		DateRange dateRange = new DateRange();
		dateRange
				.setStartDate((startDate == null || startDate.isEmpty()) ? ingestorConfiguration
						.getHistoricStartDate() : startDate);
		dateRange
				.setEndDate((endDate == null || endDate.isEmpty()) ? ingestorConfiguration
						.getHistoricEndDate() : endDate);
		List<String> views = ingestorConfiguration.getViews();

		for (View view : brand.getViews()) {
			if (!views.contains(view.getViewName())) {
				continue;
			}

			// for each view, get all reports configured
			for (Report report : ingestorConfiguration.getReports()) {
				if (!forceStartDate) {
					// change start date based on last record of each report
					// type
					String lastRecordDateHour = findLastRecordDate(
							report.getWriteToIndex(), report.getWriteToType(),
							brand.getAccountId(), view.getId(),
							view.getViewNativeId());
					if (lastRecordDateHour != null
							&& !lastRecordDateHour.isEmpty()) {
						dateRange.setStartDate(lastRecordDateHour);
					}
				}

				ReportRequest request = new ReportRequest().setDateRanges(
						Arrays.asList(dateRange)).setSamplingLevel(
						ingestorConfiguration.getSamplingLevel());
				List<Dimension> dimensions = new ArrayList<>();
				List<Metric> metrics = new ArrayList<>();
				List<String> computedMetrics = new ArrayList<>();
				results = new ArrayList<>();
				for (String dimensionName : report.getDimensions()) {
					dimensions.add(new Dimension().setName(dimensionName));
				}
				for (String metricName : report.getMetrics()) {
					metrics.add(new Metric().setExpression(metricName));
				}
				for (String computedMetricName : report.getComputedMetrics()) {
					computedMetrics.add(computedMetricName);
				}

				// the same set of dimensions and metrics are called for all
				// views. so
				// set 'em once before looping viewids
				request.setDimensions(dimensions);
				request.setMetrics(metrics);
				request.setViewId(view.getViewId());
				if (justCounts) {
					request.setPageSize(1);
				} else {
					request.setPageSize(REPORT_REQUEST_PAGE_SIZE);
				}

				ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
				requests.add(request);

				// Create the GetReportsRequest object.
				GetReportsRequest getReport = new GetReportsRequest()
						.setReportRequests(requests);

				// Call the batchGet method.
				GetReportsResponse response = requestWithExponentialBackoff(
						analyticsReportingService, getReport, brand);
				if (response != null
						&& (tempResults = parseResponse(response)) != null) {
					logger.info("Google Analytics query: {} and Response: {}",
							getReport.toString(), response.toString());
					int rowsCount = tempResults.get(0) != null ? (int) tempResults
							.get(0).get(REPORTS_ROWS_COUNT) : 0;
					List<JsonObject> rowsList = tempResults.get(0) != null ? (ArrayList<JsonObject>) tempResults
							.get(0).get(REPORTS_ROWS_DATA)
							: new ArrayList<JsonObject>();
					if (justCounts) {
						counts.put(
								StringUtil.camelCaseToUnderscore(report
										.getName()) + "_total", rowsCount);
					} else {
						if (report.getEnrichGeo() != null
								&& report.getEnrichGeo().getEnable()) {
							enrichGeoData(rowsList, report);
						}
						if (report.getComputedMetrics() != null
								&& report.getComputedMetrics().size() > 0) {
							addComputedMetrics(rowsList, report);
						}
						results.addAll(rowsList);

						boolean success = persistReports(results, report,
								brand.getAccountId(), view.getViewId(),
								view.getViewNativeId());
						if (success) {
							numberOfRowsCreated += results.size();
							try {
								Thread.sleep(ingestorConfiguration
										.getSleepBetweenRequestsInMillis());
							} catch (InterruptedException e) {
								logger.error(
										"Sleep between requests interrupted", e);
							}
						}
						results = new ArrayList<>();
						String nextPageToken = response != null ? response
								.getReports().get(0).getNextPageToken() : null;
						while (nextPageToken != null
								&& Integer.valueOf(nextPageToken) != null
								&& Integer.valueOf(nextPageToken) > 0) {
							rowsCount = 0;
							rowsList = new ArrayList<>();
							request.setPageToken(nextPageToken);
							response = requestWithExponentialBackoff(
									analyticsReportingService, getReport, brand);
							if ((tempResults = parseResponse(response)) != null) {
								rowsCount = tempResults.get(0) != null ? (int) tempResults
										.get(0).get(REPORTS_ROWS_COUNT) : 0;
								rowsList = tempResults.get(0) != null ? (ArrayList<JsonObject>) tempResults
										.get(0).get(REPORTS_ROWS_DATA)
										: new ArrayList<JsonObject>();
								results.addAll(rowsList);
							}
							nextPageToken = response != null ? response
									.getReports().get(0).getNextPageToken()
									: null;
							success = persistReports(results, report,
									brand.getAccountId(), view.getViewId(),
									view.getViewNativeId());
							if (success) {
								numberOfRowsCreated += results.size();
							}
							results = new ArrayList<>();
							try {
								Thread.sleep(ingestorConfiguration
										.getSleepBetweenRequestsInMillis());
							} catch (InterruptedException e) {
								logger.error(
										"Sleep between requests interrupted", e);
							}
						}
					}
				}
			}
		}
		counts.put(TOTAL_COUNT, numberOfRowsCreated);
		return counts;
	}

	/**
	 * Persist reports.
	 *
	 * @param results
	 *            the results
	 * @param report
	 *            the report
	 * @param accountId
	 *            the account id
	 * @param viewId
	 *            the view id
	 * @param viewNativeId
	 *            the view native id
	 * @return true, if successful
	 */
	private boolean persistReports(List<JsonObject> results, Report report,
			String accountId, String viewId, String viewNativeId) {
		// persist reports
		logger.info("attempting to persist {} records for report {} ",
				results.size(), report.getName());
		int persistedRecordsCount = 0;
		for (JsonObject result : results) {
			// prefix each element and trim "ga:" with the respective
			// stat's prefix
			JsonObject prefixedObject = new JsonObject();
			for (Entry<String, JsonElement> element : result.entrySet()) {
				if (!element.getKey().equalsIgnoreCase("ga:dateHour")) {
					prefixedObject.add(report.getPrefix()
							+ element.getKey().replaceAll("ga:", ""),
							element.getValue());
				} else {
					prefixedObject.add(element.getKey().replaceAll("ga:", ""),
							element.getValue());
				}
			}
			prefixedObject.addProperty("accountId", accountId);
			prefixedObject.addProperty("viewId", viewId);
			prefixedObject.addProperty("viewNativeId", viewNativeId);
			// add timestamp2015042212
			DateFormat readFormat = new SimpleDateFormat("yyyyMMddhh");
			try {
				prefixedObject.addProperty(
						"@timestamp",
						readFormat.parse(
								prefixedObject.get("dateHour").getAsString())
								.getTime());
			} catch (ParseException e) {
				logger.error("Could not parse dateHour:"
						+ prefixedObject.getAsString());
			}
			// index on ES
			try {
				esClient.prepareIndex(report.getWriteToIndex(),
						report.getWriteToType())
						.setSource(prefixedObject.toString()).execute().get();
				persistedRecordsCount++;
			} catch (InterruptedException | ExecutionException e) {
				logger.error(
						"Could not persist stat:"
								+ prefixedObject.getAsString(), e);
			}
		}
		logger.info("Persisted {} records for report {}",
				persistedRecordsCount, report.getName());
		return true;
	}

	/**
	 * Prints the response.
	 *
	 * @param response
	 *            the response
	 * @return the list
	 */
	private static List<Map<String, Object>> parseResponse(
			GetReportsResponse response) {
		List<Map<String, Object>> results = new ArrayList<>();
		List<JsonObject> reportResults = null;
		JsonObject reportRowNode = null;
		Map<String, Object> reportResultsMap = null;
		;
		for (com.google.api.services.analyticsreporting.v4.model.Report report : (response != null) ? response
				.getReports()
				: new ArrayList<com.google.api.services.analyticsreporting.v4.model.Report>()) {
			reportResults = new ArrayList<>();
			reportResultsMap = new HashMap<>();
			ColumnHeader header = report.getColumnHeader();
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader()
					.getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();

			if (rows == null) {
				return null;
			}

			for (ReportRow row : rows) {
				reportRowNode = new JsonObject();
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();
				for (int i = 0; i < dimensionHeaders.size()
						&& i < dimensions.size(); i++) {
					reportRowNode.addProperty(dimensionHeaders.get(i),
							dimensions.get(i));
				}

				for (int j = 0; j < metrics.size(); j++) {
					DateRangeValues values = metrics.get(j);
					for (int k = 0; k < values.getValues().size()
							&& k < metricHeaders.size(); k++) {
						reportRowNode.addProperty(metricHeaders.get(k)
								.getName(), values.getValues().get(k));
					}
				}
				reportResults.add(reportRowNode);
			}
			reportResultsMap.put(REPORTS_ROWS_COUNT, report.getData()
					.getRowCount());
			reportResultsMap.put(REPORTS_ROWS_DATA, reportResults);
			results.add(reportResultsMap);
		}
		return results;
	}

	/**
	 * Enrich geo data.
	 *
	 * @param tempResults
	 *            the temp results
	 * @param report
	 *            the report
	 */
	private static void enrichGeoData(List<JsonObject> tempResults,
			Report report) {
		JsonElement latElement = null, lonElement = null;
		for (JsonObject resultNode : tempResults) {
			latElement = resultNode.get("ga:latitude");
			lonElement = resultNode.get("ga:longitude");
			if (report.getEnrichGeo().getType().equalsIgnoreCase("Point")) {
				JsonObject geoPointWrapper = new JsonObject();
				geoPointWrapper.add("lon", lonElement);
				geoPointWrapper.add("lat", latElement);
				resultNode.add("location_point", geoPointWrapper);

				JsonArray geoPointArray = new JsonArray();
				geoPointArray.add(lonElement);
				geoPointArray.add(latElement);
				JsonObject geoShapeWrapper = new JsonObject();
				geoShapeWrapper.addProperty("type", "Point");
				geoShapeWrapper.add("coordinates", geoPointArray);
				resultNode.add("location", geoShapeWrapper);
			}
		}
	}

	/**
	 * Adds the computed metrics.
	 *
	 * @param tempResults
	 *            the temp results
	 * @param report
	 *            the report
	 */
	private void addComputedMetrics(List<JsonObject> tempResults, Report report) {
		JsonElement sessionsCount = null, transactionsCount = null;
		// handle trafficSourceReport report computed metrics
		if (report.getName().equalsIgnoreCase("trafficSourceReport")) {
			for (JsonObject resultNode : tempResults) {
				sessionsCount = resultNode.get("ga:sessions");
				transactionsCount = resultNode.get("ga:transactions");
				if (sessionsCount != null && sessionsCount.getAsDouble() > 0
						&& transactionsCount != null) {
					resultNode.addProperty("ecommerceConversionRate",
							(transactionsCount.getAsDouble() / sessionsCount
									.getAsDouble()));
				}
			}
		}
	}

	/**
	 * Find last record date.
	 *
	 * @param index
	 *            the index
	 * @param type
	 *            the type
	 * @param viewNativeId
	 * @param viewId
	 * @param accountId
	 * @return the string
	 */
	private String findLastRecordDate(String index, String type,
			String accountId, String viewId, String viewNativeId) {
		Map<String, Object> templateParams = new HashMap<>();
		templateParams.put("type", type);
		templateParams.put("accountId", accountId);
		templateParams.put("viewId", viewId);
		templateParams.put("viewNativeId", viewNativeId);

		Template template = new Template(ANALYTICS_SEARCH_TEMPLATE,
				ScriptService.ScriptType.INDEXED,
				MustacheScriptEngineService.NAME, null, templateParams);
		SearchRequestBuilder request = esClient.prepareSearch(index)
				.setTemplate(template);
		SearchResponse response = request.execute().actionGet();
		InternalMax maxMetric = response.getAggregations() != null ? response
				.getAggregations().get("latest_dateHour") : null;

		String lastRecordDate = (response.getHits().getTotalHits() > 0 && (maxMetric != null)) ? maxMetric
				.getValueAsString() != null ? maxMetric.getValueAsString() : ""
				: "";

		DateFormat readFormat = new SimpleDateFormat("yyyyMMddhh");
		if (lastRecordDate != null && !lastRecordDate.isEmpty()) {
			try {
				Date parsedFormat = readFormat.parse(lastRecordDate);
				DateFormat destDateFormat = new SimpleDateFormat("yyyy-MM-dd");

				lastRecordDate = destDateFormat.format(parsedFormat);
			} catch (ParseException e) {
				logger.error(
						"Could not read last record date-formatting issues for {}",
						lastRecordDate, e);
			}
		}
		return lastRecordDate;
	}

	/**
	 * Request with exponential backoff.
	 *
	 * @param analyticsReportingService
	 *            the analytics reporting service
	 * @param getReport
	 *            the get report
	 * @param brand
	 *            the brand
	 * @return the gets the reports response
	 */
	public GetReportsResponse requestWithExponentialBackoff(
			AnalyticsReporting analyticsReportingService,
			GetReportsRequest getReport, Brand brand) {
		boolean retriableError = false;
		GetReportsResponse response = null;
		int noOftries = 0;
		for (noOftries = 0; noOftries < this.ingestorConfiguration
				.getExponentianBackoffAttemptsOnRetriableErrors()
				&& response == null; noOftries++) {
			try {
				retriableError = false;
				response = analyticsReportingService.reports()
						.batchGet(getReport)
						.setQuotaUser(brand.getAccountId() + Math.random())
						.execute();
			} catch (IOException e) {
				// handle retriable errors
				logger.error(
						"Google Analytics call failed with error for brandId:{}",
						brand.getId(), e);

				if (e.getClass()
						.getName()
						.equalsIgnoreCase(
								"com.google.api.client.googleapis.json.GoogleJsonResponseException")) {
					GoogleJsonResponseException ge = (GoogleJsonResponseException) e;
					List<ErrorInfo> errorInfos = ge.getDetails().getErrors();
					for (ErrorInfo errorInfo : errorInfos) {
						if (errorInfo.getReason().equalsIgnoreCase(
								"userRateLimitExceeded")
								|| errorInfo.getReason().equalsIgnoreCase(
										"rateLimitExceeded")
								|| errorInfo.getReason().equalsIgnoreCase(
										"quotaExceeded")
								|| errorInfo.getMessage().contains(
										"AnalyticsDefaultGroupUSER-100s")
								|| errorInfo
										.getMessage()
										.contains(
												"AnalyticsDefaultGroupCLIENT_PROJECT-100s")
								|| errorInfo
										.getMessage()
										.contains(
												"AnalyticsDefaultGroupCLIENT_PROJECT-100s")
								|| errorInfo
										.getMessage()
										.contains(
												"AnalyticsDefaultGroupCLIENT_PROJECT-100s")
								|| errorInfo
										.getMessage()
										.contains(
												"AnalyticsDefaultGroupCLIENT_PROJECT-100s")
								|| errorInfo
										.getMessage()
										.contains(
												"The service is currently unavailable.")) {
							retriableError = true;
						}
					}
				}
				if (e.getClass().getName()
						.equalsIgnoreCase("java.net.SocketTimeoutException")) {
					retriableError = true;
				}

				if (retriableError) {
					try {
						logger.info("Applying Retrying logic-Exponential Backoff");
						Thread.sleep((long) (Math.pow(2, noOftries) * 1000 + Math
								.random() * 1000));
					} catch (InterruptedException e1) {
						logger.error(
								"Google Analytics call retry attemp, sleep interrupted:",
								e1);
					}
				} else {
					logger.info("NOT Applying Retrying logic-Exponential Backoff. Probably not a retriable error?");
					break;
				}
			} catch (Exception e) {
				logger.error(
						"Google Analytics call failed with error for brandId:{}",
						brand.getId(), e);
			}
		}
		if (noOftries > 4 && retriableError) {
			logger.error("Failed to get Data after {} tries", noOftries);
		}
		return response;
	}

	private GoogleCredential buildCredentials(Brand brand) {
		return new GoogleCredential.Builder()
				.setClientSecrets(
						this.clientSecretResourceConfiguration.getInstalled()
								.getClientId(),
						this.clientSecretResourceConfiguration.getInstalled()
								.getClientSecret()).setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).build()
				.setAccessToken(brand.getAccountOauthtoken())
				.setRefreshToken(brand.getAccountRefreshOauthtoken());
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

	}
}
