package com.growthhacker.googleanalytics;

import io.dropwizard.Application;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.interact.sqsdw.MessageHandler;
import io.interact.sqsdw.SqsListener;
import io.interact.sqsdw.SqsListenerHealthCheck;
import io.interact.sqsdw.SqsListenerImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.amazonaws.services.sqs.AmazonSQS;
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
//		final ManagedEsClient managedClient = new ManagedEsClient(
//				configuration.getEsConfiguration());
		Settings settings = Settings
				.builder()
				.put("cluster.name",
						configuration.getEsConfiguration().getClusterName())
				.build();
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress
						.getByName(configuration.getEsConfiguration()
								.getServers().get(0).getHostText()), 9300));
//		 environment.lifecycle().manage(managedClient);

		// Create Resources
		final GoogleAnalyticsResource googleAnalyticsResource = new GoogleAnalyticsResource(
				client, configuration);

		environment.jersey().register(googleAnalyticsResource);

		// AWS sqs message handler
		final AmazonSQS sqs = configuration.getSqsFactory().build(environment);
		final MessageHandler handler = googleAnalyticsResource;
		final Set<MessageHandler> handlers = new HashSet<>();
		handlers.add(googleAnalyticsResource);

		final SqsListener sqsListener = new SqsListenerImpl(sqs,
				configuration.getSqsListenQueueUrl(), handlers);
		environment.lifecycle().manage(sqsListener);

		// health check
		// environment.healthChecks().register("ES cluster health",
		// new EsClusterHealthCheck(managedClient.getClient()));
		environment.healthChecks().register("SqsListener",
				new SqsListenerHealthCheck(sqsListener));
	}
}
