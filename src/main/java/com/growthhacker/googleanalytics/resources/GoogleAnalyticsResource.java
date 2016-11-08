package com.growthhacker.googleanalytics.resources;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
import com.growthhacker.googleanalytics.util.Brand;
import com.growthhacker.googleanalytics.util.Brand.BrandIngestRunUpdateView;
import com.growthhacker.googleanalytics.util.View;

// TODO: Auto-generated Javadoc
/**
 * The Class GoogleAnalyticsResource.
 */
@Path("/googleAnalytics")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class GoogleAnalyticsResource {

	/** The Constant logger. */
	final static Logger logger = LoggerFactory
			.getLogger(GoogleAnalyticsResource.class);

	/** The Constant ACCOUNT_ID_REQUIRED. */
	public final static String ACCOUNT_ID_REQUIRED = "account_id is a required field for this API call";

	/** The Constant START_DATE_REQUIRED. */
	public final static String START_DATE_REQUIRED = "startDate is a required field if forceStartDate is true for this API call";

	/** The application name. */
	private static String APPLICATION_NAME = "GrowthHacker Analytics Resource";

	/** The status success. */
	private static String STATUS_SUCCESS = "PROCESSED";

	/** The status failure. */
	private static String STATUS_FAILURE = "FAILED";

	/** The brand index. */
	private static String BRAND_INDEX = "brands";

	/** The brand type. */
	private static String BRAND_TYPE = "brand";

	/** The analytics search template. */
	private static String ANALYTICS_SEARCH_TEMPLATE = "st.analytics";

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

	/** The credential. */
	private GoogleCredential credential;

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
		this.mapper = new ObjectMapper();
		this.esClient = client;

		this.clientSecretResourceConfiguration = configuration
				.getClientSecretResourceConfiguration();
		this.ingestorConfiguration = configuration.getIngestorConfiguration();

		this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		// GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
		// JSON_FACTORY, new InputStreamReader(new ByteArrayInputStream(
		// this.clientSecretResourceConfiguration.toJsonString()
		// .getBytes())));

		this.credential = new GoogleCredential.Builder()
				.setClientSecrets(
						this.clientSecretResourceConfiguration.getInstalled()
								.getClientId(),
						this.clientSecretResourceConfiguration.getInstalled()
								.getClientSecret()).setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).build();
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
		BrandIngestRunUpdateView brandIngestRunUpdateView = brand.new BrandIngestRunUpdateView();
		brandIngestRunUpdateView.setAccountId(brand.getAccountId());
		brandIngestRunUpdateView.setAccountOauthtoken(brand
				.getAccountOauthtoken());
		brandIngestRunUpdateView.setAccountRefreshOauthtoken(brand
				.getAccountRefreshOauthtoken());

		this.credential.setAccessToken(brand.getAccountOauthtoken());
		this.credential.setRefreshToken(brand.getAccountRefreshOauthtoken());
		AnalyticsReporting analyticsReportingService = new AnalyticsReporting.Builder(
				httpTransport, JSON_FACTORY, this.credential)
				.setApplicationName(APPLICATION_NAME).build();
		try {
			brandIngestRunUpdateView
					.setAccountRecordLastrefreshStartTimestamp(Instant.now()
							.toEpochMilli());
			// get data and persist
			Integer numberOfRowsCreated = getAndPersistReports(
					analyticsReportingService, brand, startDate, endDate,
					forceStartDate);
			brandIngestRunUpdateView
					.setAccountRecordLastrefreshEndTimestamp(Instant.now()
							.toEpochMilli());
			brandIngestRunUpdateView.setUpdatedAt(String.valueOf(Instant.now()
					.toEpochMilli()));
			brandIngestRunUpdateView.setTimestamp(String.valueOf(Instant.now()
					.toEpochMilli()));
			brandIngestRunUpdateView
					.setAccountRecordLastrefresh(numberOfRowsCreated);

			brandIngestRunUpdateView.setAccountRecordStatus(STATUS_SUCCESS);
			// update oauth token
			if (brandIngestRunUpdateView.getAccountOauthtoken() != null
					&& this.credential.getAccessToken() != null
					&& !brandIngestRunUpdateView.getAccountOauthtoken().equals(
							this.credential.getAccessToken())) {
				brandIngestRunUpdateView.setAccountOauthtoken(this.credential
						.getAccessToken());
			}
			if (brandIngestRunUpdateView.getAccountRefreshOauthtoken() != null
					&& this.credential.getRefreshToken() != null
					&& !brandIngestRunUpdateView.getAccountRefreshOauthtoken()
							.equals(this.credential.getRefreshToken())) {
				brandIngestRunUpdateView
						.setAccountRefreshOauthtoken(this.credential
								.getRefreshToken());
			}

			// update Brand record in ES
			try {
				UpdateResponse updateResponse = esClient
						.prepareUpdate(BRAND_INDEX, BRAND_TYPE,
								brand.getAccountId())
						.setDoc(this.mapper
								.writeValueAsString(brandIngestRunUpdateView))
						.execute().get();
			} catch (InterruptedException | ExecutionException e) {
				logger.error("Could not update BrandId:{} for Ingest status:",
						brand.getAccountId(), e);
			}

			return Response.status(Response.Status.OK)
					.entity(brandIngestRunUpdateView).build();
		} catch (IOException e) {
			logger.error("Error in getting Data from Google Analytics:", e);
			brandIngestRunUpdateView.setAccountRecordStatus(STATUS_FAILURE);
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error in getting Data from Google Analytics:"
							+ e.getMessage()).build();
		}
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
	private Integer getAndPersistReports(
			AnalyticsReporting analyticsReportingService, Brand brand,
			String startDate, String endDate, Boolean forceStartDate)
			throws IOException {
		int numberOfRowsCreated = 0;
		List<JsonObject> results = new ArrayList<>();
		List<JsonObject> tempResults = null;
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
							report.getWriteToIndex(), report.getWriteToType());
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

				ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
				requests.add(request);

				// Create the GetReportsRequest object.
				GetReportsRequest getReport = new GetReportsRequest()
						.setReportRequests(requests);

				// Call the batchGet method.
				GetReportsResponse response = requestWithExponentialBackoff(
						analyticsReportingService, getReport, brand);

				if ((tempResults = printResponse(response)) != null) {
					if (report.getEnrichGeo() != null
							&& report.getEnrichGeo().getEnable()) {
						enrichGeoData(tempResults, report);
					}
					if (report.getComputedMetrics() != null
							&& report.getComputedMetrics().size() > 0) {
						addComputedMetrics(tempResults, report);
					}
					results.addAll(tempResults);

					boolean success = persistReports(results, report,
							brand.getAccountId(), view.getViewId(),
							view.getViewNativeId());
					if (success) {
						numberOfRowsCreated += results.size();
					}
					results = new ArrayList<>();
					String nextPageToken = response.getReports().get(0)
							.getNextPageToken();
					while (nextPageToken != null
							&& Integer.valueOf(nextPageToken) != null
							&& Integer.valueOf(nextPageToken) > 0) {
						request.setPageToken(nextPageToken);
						response = requestWithExponentialBackoff(
								analyticsReportingService, getReport, brand);
						if ((tempResults = printResponse(response)) != null) {
							results.addAll(tempResults);
						}
						nextPageToken = response.getReports().get(0)
								.getNextPageToken();
						success = persistReports(results, report,
								brand.getAccountId(), view.getViewId(),
								view.getViewNativeId());
						if (success) {
							numberOfRowsCreated += results.size();
						}
						results = new ArrayList<>();
					}
				}
			}
		}
		return numberOfRowsCreated;
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
	private static List<JsonObject> printResponse(GetReportsResponse response) {
		List<JsonObject> results = new ArrayList<>();
		JsonObject resultNode = new JsonObject();
		for (com.google.api.services.analyticsreporting.v4.model.Report report : response
				.getReports()) {
			ColumnHeader header = report.getColumnHeader();
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader()
					.getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();

			if (rows == null) {
				return null;
			}

			for (ReportRow row : rows) {
				resultNode = new JsonObject();
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();
				for (int i = 0; i < dimensionHeaders.size()
						&& i < dimensions.size(); i++) {
					resultNode.addProperty(dimensionHeaders.get(i),
							dimensions.get(i));
				}

				for (int j = 0; j < metrics.size(); j++) {
					DateRangeValues values = metrics.get(j);
					for (int k = 0; k < values.getValues().size()
							&& k < metricHeaders.size(); k++) {
						resultNode.addProperty(metricHeaders.get(k).getName(),
								values.getValues().get(k));
					}
				}
				results.add(resultNode);
			}
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
	 * @return the string
	 */
	private String findLastRecordDate(String index, String type) {
		Map<String, Object> templateParams = new HashMap<>();
		templateParams.put("type", type);

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
		for (int noOftries = 0; noOftries < this.ingestorConfiguration
				.getExponentianBackoffAttemptsOnRetriableErrors(); noOftries++) {
			try {
				response = analyticsReportingService.reports()
						.batchGet(getReport).setQuotaUser(brand.getAccountId())
						.execute();
			} catch (IOException e) {
				// handle retriable errors
				logger.error("Google Analytics call failed with error:", e);

				if (e.getClass().getName()
						.equalsIgnoreCase("GoogleJsonResponseException")) {
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
												"AnalyticsDefaultGroupCLIENT_PROJECT-100s")) {
							retriableError = true;
						}
					}
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
			}
		}
		return response;
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
