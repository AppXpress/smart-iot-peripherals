package com.dev.appx.sns.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.dev.appx.sns.AmazonClientProvider;
import com.dev.appx.sns.auth.ServerKeys;
import com.dev.appx.sns.config.SNSConfigurator;
import com.dev.appx.sns.tools.AmazonSNSClientWrapper;
import com.dev.appx.sns.tools.SampleMessageGenerator.Platform;

/**
 * 
 * Registration Service with SNS.
 * 
 * @author nthusitha
 *
 */
public class SNSRegistrationService {

	// in-memory platform application result
	// private static CreatePlatformApplicationResult platformApplication =
	// null;

	AmazonSNS client = null;
	AmazonSNSClientWrapper clientWrapper = null;

	public SNSRegistrationService() throws IOException {
		client = AmazonClientProvider.getClientInstance().getClient();
		clientWrapper = new AmazonSNSClientWrapper(client);
	}

	/**
	 * @param endPointArn_
	 * @param token_
	 * @param platform
	 * @param appName
	 * @return - EndPointArn
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String registerWithSNS(String endPointArn_, String token_,
			Platform platform, String appName) throws IOException {

		String endpointArn = endPointArn_;
		String token = token_;

		boolean updateNeeded = false;
		boolean createNeeded = (null == endpointArn);

		if (createNeeded) {
			// No endpoint ARN is stored; need to call CreateEndpoint
			endpointArn = createEndpoint(token_, platform, appName);
			createNeeded = false;
		}

		System.out.println("Retrieving endpoint data...");
		// Look up the endpoint and make sure the data in it is current, even if
		// it was just created
		try {
			GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest()
					.withEndpointArn(endpointArn);
			GetEndpointAttributesResult geaRes = client
					.getEndpointAttributes(geaReq);

			updateNeeded = !geaRes.getAttributes().get("Token").equals(token)
					|| !geaRes.getAttributes().get("Enabled")
							.equalsIgnoreCase("true");

		} catch (NotFoundException nfe) {
			// we had a stored ARN, but the endpoint associated with it
			// disappeared. Recreate it.
			createNeeded = true;
		}

		if (createNeeded) {
			endpointArn = createEndpoint(token_, platform, appName);
		}

		System.out.println("updateNeeded=" + updateNeeded);

		if (updateNeeded) {
			// endpoint is out of sync with the current data;
			// update the token and enable it.
			System.out.println("Updating endpoint " + endpointArn);
			@SuppressWarnings("rawtypes")
			Map attribs = new HashMap();
			attribs.put("Token", token);
			attribs.put("Enabled", "true");
			SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest()
					.withEndpointArn(endpointArn).withAttributes(attribs);
			client.setEndpointAttributes(saeReq);
		}
		return endpointArn != null ? endpointArn : "";
	}

	/**
	 * @return never null
	 * @throws IOException
	 * */
	private String createEndpoint(String token, Platform platform,
			String appName) throws IOException {

		String endpointArn = null;
		SNSConfigurator snsConfig = new SNSConfigurator();
		try {
			System.out.println("Creating endpoint with token " + token);

			// if (PLATFORM_APPLICATION == null) {
			CreatePlatformApplicationResult platformApplication = null;
			switch (platform) {
			case GCM: {
				ServerKeys gcmKeys = snsConfig
						.loadServerKeys(appName, platform);
				platformApplication = clientWrapper.createPlatformApplication(
						appName, platform, "", gcmKeys.getCredential());
				break;
			}
			case APNS_SANDBOX: {
				ServerKeys apnsSandbox = snsConfig.loadServerKeys(appName,
						platform);
				platformApplication = clientWrapper.createPlatformApplication(
						appName, platform, apnsSandbox.getPrincipal(),
						apnsSandbox.getCredential());
			}
			case APNS: {
				ServerKeys apnsProd = snsConfig.loadServerKeys(appName,
						platform);
				platformApplication = clientWrapper.createPlatformApplication(
						appName, platform, apnsProd.getPrincipal(),
						apnsProd.getCredential());
			}
			default: {
				// do nothing.
			}
			}
			// }
			if (platformApplication != null) {
				CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
						.withPlatformApplicationArn(
								platformApplication.getPlatformApplicationArn())
						.withToken(token);
				CreatePlatformEndpointResult cpeRes = client
						.createPlatformEndpoint(cpeReq);
				endpointArn = cpeRes.getEndpointArn();
			}else{
				System.out.print("platform application is null, most likely no response from the push service providers, try again");
			}

		} catch (InvalidParameterException ipe) {
			String message = ipe.getErrorMessage();
			System.out.println("Exception message: " + message);
			Pattern p = Pattern
					.compile(".*Endpoint (arn:aws:sns[^ ]+) already exists "
							+ "with the same Token.*");
			Matcher m = p.matcher(message);
			if (m.matches()) {
				// the endpoint already exists for this token, but with
				// additional custom data that
				// CreateEndpoint doesn't want to overwrite. Just use the
				// existing endpoint.
				endpointArn = m.group(1);
			} else {
				// rethrow exception, the input is actually bad
				throw ipe;
			}
		}
		// storeEndpointArn(endpointArn);
		return endpointArn;
	}

}
