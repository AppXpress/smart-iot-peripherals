package com.dev.appx.sns;

import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

/**
 * Singleton Client Provider
 * 
 * @author nthusitha
 *
 */
public class AmazonClientProvider {

	private AmazonSNS client = null;
	private static AmazonClientProvider clientProvider = null;

	private AmazonClientProvider() throws IOException {
		boolean useClasspath = false;

		InputStream is = SNSMobilePush.class
				.getResourceAsStream("/AwsCredentials.properties");

		useClasspath = is == null;

		if (useClasspath) {
			client = new AmazonSNSClient(
					new ClasspathPropertiesFileCredentialsProvider());
		} else {
			client = new AmazonSNSClient(new PropertiesCredentials(is));
		}
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

	public AmazonSNS getClient() {
		return this.client;

	}

	public static AmazonClientProvider getClientInstance() throws IOException {
		if (clientProvider == null) {
			clientProvider = new AmazonClientProvider();
		}
		return clientProvider;
	}

}
