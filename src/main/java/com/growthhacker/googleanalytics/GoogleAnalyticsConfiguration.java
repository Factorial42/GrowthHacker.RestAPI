package com.growthhacker.googleanalytics;

import io.dropwizard.Configuration;
import io.dropwizard.elasticsearch.config.EsConfiguration;
import io.dropwizard.validation.ValidationMethod;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.interact.sqsdw.SqsFactory;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleAnalyticsConfiguration extends Configuration {

	/** The es configuration. */
	@Valid
	@NotNull
	public EsConfiguration esConfiguration;

	@JsonProperty("swagger")
	public SwaggerBundleConfiguration swaggerBundleConfiguration;

	@JsonProperty("clientSecretResourceConfiguration")
	public ClientSecretResourceConfiguration clientSecretResourceConfiguration;

	@JsonProperty("ingestorConfiguration")
	public IngestorConfiguration ingestorConfiguration;

	@Valid
    @NotNull
    @JsonProperty
    private SqsFactory sqsFactory;

    @NotNull
    @JsonProperty
    private String sqsListenQueueUrl;

	/**
	 * Gets the es configuration.
	 *
	 * @return the es configuration
	 */
	@JsonProperty
	public EsConfiguration getEsConfiguration() {
		return esConfiguration;
	}

	/**
	 * @return the swaggerBundleConfiguration
	 */
	public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
		return swaggerBundleConfiguration;
	}

	/**
	 * @return the clientSecretResourceConfiguration
	 */
	public ClientSecretResourceConfiguration getClientSecretResourceConfiguration() {
		return clientSecretResourceConfiguration;
	}

	/**
	 * @return the ingestorConfiguration
	 */
	public IngestorConfiguration getIngestorConfiguration() {
		return ingestorConfiguration;
	}

	/**
	 * @return the sqsFactory
	 */
	public SqsFactory getSqsFactory() {
		return sqsFactory;
	}

	/**
	 * @return the sqsListenQueueUrl
	 */
	public String getSqsListenQueueUrl() {
		return sqsListenQueueUrl;
	}

	public class ClientSecretResourceConfiguration {

		@JsonProperty
		@NotNull
		private Installed installed;

		@ValidationMethod
		@JsonIgnore
		public boolean isValidConfig() {
			return installed.isValidConfig();
		}

		/**
		 * @return the installed
		 */
		public Installed getInstalled() {
			return installed;
		}

		public String toJsonString() throws JsonProcessingException {
			return new ObjectMapper().writeValueAsString(this);
		}
		public class Installed {
			@JsonProperty("client_id")
			@NotEmpty
			private String clientId;

			@JsonProperty("project_id")
			@NotEmpty
			private String projectId;

			@JsonProperty("auth_uri")
			@NotEmpty
			private String authUri;

			@JsonProperty("token_uri")
			@NotEmpty
			private String tokenUri;

			@JsonProperty("auth_provider_x509_cert_url")
			@NotEmpty
			private String authZroviderX509CertUrl;

			@JsonProperty("client_secret")
			@NotEmpty
			private String clientSecret;

			@JsonProperty("redirect_uris")
			@NotNull
			private List<String> redirectUris = Collections.emptyList();

			/**
			 * @return the project_id
			 */
			public String getClientId() {
				return clientId;
			}

			/**
			 * @return the projectId
			 */
			public String getProjectId() {
				return projectId;
			}

			/**
			 * @return the authUri
			 */
			public String getAuthUri() {
				return authUri;
			}

			/**
			 * @return the tokenUri
			 */
			public String getTokenUri() {
				return tokenUri;
			}

			/**
			 * @return the authZroviderX509CertUrl
			 */
			public String getAuthZroviderX509CertUrl() {
				return authZroviderX509CertUrl;
			}

			/**
			 * @return the clientSecret
			 */
			public String getClientSecret() {
				return clientSecret;
			}

			/**
			 * @return the redirectUris
			 */
			public List<String> getRedirectUris() {
				return redirectUris;
			}

			public boolean isValidConfig() {
				return !clientId.isEmpty() || !projectId.isEmpty()
						|| !authUri.isEmpty() || !tokenUri.isEmpty()
						|| !authZroviderX509CertUrl.isEmpty()
						|| !clientSecret.isEmpty() || redirectUris.size() < 2;
			}
		}
	}
}
