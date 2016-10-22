package com.growthhacker.googleanalytics;

import io.dropwizard.Application;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.growthhacker.googleanalytics.resources.GoogleAnalyticsResource;

public class GoogleAnalyticsApplication extends
		Application<GoogleAnalyticsConfiguration> {

	public static void main(final String[] args) throws Exception {
		new GoogleAnalyticsApplication().run(args);
	}

	@Override
	public String getName() {
		return "Google Analytics";
	}

	@Override
	public void initialize(
			final Bootstrap<GoogleAnalyticsConfiguration> bootstrap) {
		// TODO: application initialization
	}

	@Override
	public void run(final GoogleAnalyticsConfiguration configuration,
			final Environment environment) throws IOException,
			GeneralSecurityException {
		// Create Elasticsearch managed Client
		final ManagedEsClient managedClient = new ManagedEsClient(
				configuration.getEsConfiguration());
		environment.lifecycle().manage(managedClient);

		// health check
		environment.healthChecks().register("ES cluster health",
				new EsClusterHealthCheck(managedClient.getClient()));

		// Create Resources
		final GoogleAnalyticsResource googleAnalyticsResource = new GoogleAnalyticsResource(
				managedClient.getClient(), configuration);

		environment.jersey().register(googleAnalyticsResource);
	}

}
