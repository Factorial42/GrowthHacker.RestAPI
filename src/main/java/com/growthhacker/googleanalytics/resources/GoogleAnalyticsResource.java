package com.growthhacker.googleanalytics.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.growthhacker.googleanalytics.GoogleAnalyticsConfiguration;
import com.growthhacker.googleanalytics.IngestorConfiguration;
import com.growthhacker.googleanalytics.Report;
import com.growthhacker.googleanalytics.util.Brand;
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

	/** The Constant BRAND_ID_REQUIRED. */
	public final static String BRAND_ID_REQUIRED = "Brand Id is a required field for this API call";

	/** The application name. */
	private static String APPLICATION_NAME = "GrowthHacker Analytics Resource";

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
		this.esClient = client;

		this.clientSecretResourceConfiguration = configuration
				.getClientSecretResourceConfiguration();
		this.ingestorConfiguration = configuration.getIngestorConfiguration();

		this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, new InputStreamReader(new ByteArrayInputStream(
						this.clientSecretResourceConfiguration.toJsonString()
								.getBytes())));

		this.credential = new GoogleCredential.Builder()
				.setTransport(httpTransport).setJsonFactory(JSON_FACTORY)
				.build();
	}

	/**
	 * Ingest historic data.
	 *
	 * @param brand
	 *            the brand
	 * @return the response
	 */
	@POST
	@Timed
	@Path("/ingestHistoricData")
	public Response ingestHistoricData(Brand brand) {
		if (brand.getId() == null && brand.getId().isEmpty())
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(BRAND_ID_REQUIRED).build();
		credential.setAccessToken(brand.getAccountOauthtoken());
		// credential.setRefreshToken(brand.getAccountRefreshOauthtoken());
		AnalyticsReporting analyticsReportingService = new AnalyticsReporting.Builder(
				httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();
		try {
			boolean success = getAndPersistReports(analyticsReportingService,
					brand.getAccountId(), brand.getViews(), null, null);
			return Response.status(Response.Status.OK).entity(success).build();
		} catch (IOException e) {
			logger.error("Error in getting Data from Google Analytics:", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error in getting Data from Google Analytics:" + e.getMessage())
					.build();
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
	 * @return the response
	 */
	@POST
	@Timed
	@Path("/ingestData")
	public Response ingestData(Brand brand,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {
		if (brand.getId() == null && brand.getId().isEmpty())
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(BRAND_ID_REQUIRED).build();
		credential.setAccessToken(brand.getAccountOauthtoken());
		credential.setRefreshToken(brand.getAccountRefreshOauthtoken());
		AnalyticsReporting analyticsReportingService = new AnalyticsReporting.Builder(
				httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();
		try {
			boolean success = getAndPersistReports(analyticsReportingService,
					brand.getAccountId(), brand.getViews(), startDate, endDate);
			return Response.status(Response.Status.OK).entity(success).build();
		} catch (IOException e) {
			logger.error("Error in getting Data from Google Analytics:", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error in getting Data from Google Analytics:" + e.getMessage())
					.build();
		}
	}

	/**
	 * Gets the and persist reports.
	 *
	 * @param analyticsReportingService
	 *            the analytics reporting service
	 * @param accountId
	 *            the account id
	 * @param views
	 *            the views
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @return the and persist reports
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private boolean getAndPersistReports(
			AnalyticsReporting analyticsReportingService, String accountId,
			List<View> views, String startDate, String endDate)
			throws IOException {
		List<JsonObject> results = new ArrayList<>();
		List<JsonObject> tempResults = null;
		DateRange dateRange = new DateRange();
		dateRange
				.setStartDate((startDate == null || startDate.isEmpty()) ? ingestorConfiguration
						.getHistoricStartDate() : startDate);
		dateRange
				.setEndDate((endDate == null || endDate.isEmpty()) ? ingestorConfiguration
						.getHistoricEndDate() : endDate);

		for (View view : views) {
			ReportRequest request = new ReportRequest().setDateRanges(
					Arrays.asList(dateRange)).setSamplingLevel(
					ingestorConfiguration.getSamplingLevel());

			// for each view, get all reports configured
			for (Report report : ingestorConfiguration.getReports()) {
				List<Dimension> dimensions = new ArrayList<>();
				List<Metric> metrics = new ArrayList<>();
				results = new ArrayList<>();
				for (String dimensionName : report.getDimensions()) {
					dimensions.add(new Dimension().setName(dimensionName));
				}
				for (String metricName : report.getMetrics()) {
					metrics.add(new Metric().setExpression(metricName));
				}

				// the same set of dimensions and metrics are called for all
				// views. so
				// set 'em once before looping viewids
				request.setDimensions(dimensions);
				request.setMetrics(metrics);
				request.setViewId(view.getId());

				ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
				requests.add(request);

				// Create the GetReportsRequest object.
				GetReportsRequest getReport = new GetReportsRequest()
						.setReportRequests(requests);

				// Call the batchGet method.
				GetReportsResponse response = analyticsReportingService
						.reports().batchGet(getReport).execute();

				if ((tempResults = printResponse(response)) != null) {
					results.addAll(tempResults);

					String nextPageToken = response.getReports().get(0)
							.getNextPageToken();
					while (nextPageToken != null
							&& Integer.valueOf(nextPageToken) != null
							&& Integer.valueOf(nextPageToken) > 0) {
						request.setPageToken(nextPageToken);
						response = analyticsReportingService.reports()
								.batchGet(getReport).execute();
						if ((tempResults = printResponse(response)) != null) {
							results.addAll(tempResults);
						}
						nextPageToken = response.getReports().get(0)
								.getNextPageToken();
					}
				}
				boolean success = persistReports(results, report);
			}
		}
		return true;
	}

	private boolean persistReports(List<JsonObject> results, Report report) {
		// persist reports
		for (JsonObject result : results) {
			// prefix each element and trim "ga:" with the respective
			// stat's prefix
			JsonObject prefixedObject = new JsonObject();
			for (Entry<String, JsonElement> element : result.entrySet()) {
				prefixedObject.add(report.getPrefix()
						+ element.getKey().replaceAll("ga:", ""),
						element.getValue());
			}
			// index on ES
			esClient.prepareIndex(report.getWriteToIndex(), report
					.getWriteToType(),
					prefixedObject.get(report.getPrefix() + "dateHour")
							.getAsString());
		}
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
					// System.out.println(dimensionHeaders.get(i) + ": "
					// + dimensions.get(i));
					resultNode.addProperty(dimensionHeaders.get(i),
							dimensions.get(i));
				}

				for (int j = 0; j < metrics.size(); j++) {
					// System.out.print("Date Range (" + j + "): ");
					DateRangeValues values = metrics.get(j);
					for (int k = 0; k < values.getValues().size()
							&& k < metricHeaders.size(); k++) {
						// System.out.println(metricHeaders.get(k).getName()
						// + ": " + values.getValues().get(k));
						resultNode.addProperty(metricHeaders.get(k).getName(),
								values.getValues().get(k));
					}
				}
				results.add(resultNode);
			}
		}
		return results;
	}
	
	public static void main(String[] args) {
		
	}
}
