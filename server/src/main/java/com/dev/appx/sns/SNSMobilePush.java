package com.dev.appx.sns;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.dev.appx.sns.tools.AmazonSNSClientWrapper;
import com.dev.appx.sns.tools.SampleMessageGenerator.Platform;

public class SNSMobilePush {

	private AmazonSNSClientWrapper snsClientWrapper;

	public SNSMobilePush(AmazonSNS snsClient) {
		this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
	}

	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
	static {
		attributesMap.put(Platform.ADM, null);
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
		attributesMap.put(Platform.BAIDU, addBaiduNotificationAttributes());
		attributesMap.put(Platform.WNS, addWNSNotificationAttributes());
		attributesMap.put(Platform.MPNS, addMPNSNotificationAttributes());
	}

	public static void igniteSNS() throws IOException {
		/*
		 * TODO: Be sure to fill in your AWS access credentials in the
		 * AwsCredentials.properties file before you try to run this sample.
		 * http://aws.amazon.com/security-credentials
		 */
		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(
				SNSMobilePush.class
						.getResourceAsStream("/AwsCredentials.properties")));

		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
		System.out.println("===========================================\n");
		System.out.println("Getting Started with Amazon SNS");
		System.out.println("===========================================\n");
		try {
			SNSMobilePush sample = new SNSMobilePush(sns);
			/* TODO: Uncomment the services you wish to use. */
			 //sample.demoAndroidAppNotification();
			// sample.demoKindleAppNotification();
			// sample.demoAppleAppNotification();
			// sample.demoAppleSandboxAppNotification();
			// sample.demoBaiduAppNotification();
			// sample.demoWNSAppNotification();
			// sample.demoMPNSAppNotification();
			sample.createTopic("test");
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon SNS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with SNS, such as not "
							+ "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	public void demoAndroidAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAndroidMessage()
		String serverAPIKey = "AIzaSyABzDSISGL8MJBZb7mcOO8514CGo3fDLDQ";
		String applicationName = "appxsnspush";
		String registrationId = "APA91bG8SVJ685S6Xi1JNaVM3pLTDXV0NsEjGXTSqrbVlF6FrdbopT46Q45Hq7hQ8Q_WPDCcZygI0ToMSJt6u_F6qayFbW6vYrq_b-W5Fk3DjDf69JM_uuG7h1eJsKalGY4A4e23bu3Y";
		snsClientWrapper.demoNotification(Platform.GCM, "", serverAPIKey,
				registrationId, applicationName, attributesMap);
	}

	public void demoKindleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleKindleMessage()
		String clientId = "";
		String clientSecret = "";
		String applicationName = "";

		String registrationId = "";
		snsClientWrapper.demoNotification(Platform.ADM, clientId, clientSecret,
				registrationId, applicationName, attributesMap);
	}

	public void demoAppleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
		String certificate = ""; // This should be in pem format with \n at the
									// end of each line.
		String privateKey = ""; // This should be in pem format with \n at the
								// end of each line.
		String applicationName = "";
		String deviceToken = ""; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS, certificate,
				privateKey, deviceToken, applicationName, attributesMap);
	}

	public void demoAppleSandboxAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
		String certificate = ""; // This should be in pem format with \n at the
									// end of each line.
		String privateKey = ""; // This should be in pem format with \n at the
								// end of each line.
		String applicationName = "";
		String deviceToken = ""; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS_SANDBOX, certificate,
				privateKey, deviceToken, applicationName, attributesMap);
	}

	public void demoBaiduAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your Baidu notification, you can
		 * do so by modifying the attribute values in the method
		 * addBaiduNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleBaiduMessage()
		 */
		String userId = "";
		String channelId = "";
		String apiKey = "";
		String secretKey = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.BAIDU, apiKey, secretKey,
				channelId + "|" + userId, applicationName, attributesMap);
	}

	public void demoWNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your WNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addWNSNotificationAttributes() . You can also change the notification
		 * payload as per your preferences using the method
		 * com.amazonaws.sns.samples
		 * .tools.SampleMessageGenerator.getSampleWNSMessage()
		 */
		String notificationChannelURI = "";
		String packageSecurityIdentifier = "";
		String secretKey = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.WNS,
				packageSecurityIdentifier, secretKey, notificationChannelURI,
				applicationName, attributesMap);
	}

	public void demoMPNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your MPNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addMPNSNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleMPNSMessage ()
		 */
		String notificationChannelURI = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.MPNS, "", "",
				notificationChannelURI, applicationName, attributesMap);
	}

	private static Map<String, MessageAttributeValue> addBaiduNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.DeployStatus",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("1"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageKey",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("default-channel-msg-key"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageType",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("0"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addWNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.CachePolicy",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("cache"));
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.Type",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("wns/badge"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addMPNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.Type",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("token")); // This attribute is required.
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.NotificationClass",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("realtime")); // This attribute is required.
														
		return notificationAttributes;
	}
	
	public void createTopic(String topic) throws IOException{
		//create a new SNS client and set endpoint
		AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());		                           
	
		/*AmazonSNS snsClient = new AmazonSNSClient(new PropertiesCredentials(
				SNSMobilePush.class
						.getResourceAsStream("AwsCredentials.properties")));*/
		
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

		//create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(topic);
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		//print TopicArn
		System.out.println(createTopicResult);
		//get request id for CreateTopicRequest from SNS metadata		
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
	}
	
	public void deleteTopic(String topicArn) throws IOException{
		
		//AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());	
		
		
		AmazonSNS snsClient = new AmazonSNSClient(new PropertiesCredentials(
				SNSMobilePush.class
						.getResourceAsStream("AwsCredentials.properties")));
		//delete an SNS topic
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		snsClient.deleteTopic(deleteTopicRequest);
		//get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - " + snsClient.getCachedResponseMetadata(deleteTopicRequest));


	}
	
	
	
	
	public void subscribeToTopic(String topicArn, SNSProtocol proto, String endPoint){
		
	}
	
	public static enum SNSProtocol {
		HTTP("http"),
		HTTPS("https"),
		EMAIL("email"),
		EMAIL_JSON("email-json"),
		SMS("sms"),
		APPLICATION("application");
		
		private String protocol;
		
		private SNSProtocol(String name){
			this.protocol = name;
		}
		
		public String proto(){
			return this.protocol;
		}
	}
}
