package com.growthhacker.googleanalytics.resources;

import io.interact.sqsdw.MessageHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BrandIngestHandlerImpl extends MessageHandler {

	/** The Constant logger. */
	final static Logger logger = LoggerFactory
			.getLogger(BrandIngestHandlerImpl.class);

	/** The mapper. */
	private static ObjectMapper mapper=new ObjectMapper();
	
	public BrandIngestHandlerImpl() {
		super("GHBrandIngestType");
	}

	public void handle(Message message) {
		try {
			logger.info("Message received:", this.mapper
					.writeValueAsString(message));
			String body = message.getBody();
		} catch (JsonProcessingException e) {
			logger.error("Could not process message:", message);
		}
	}

}