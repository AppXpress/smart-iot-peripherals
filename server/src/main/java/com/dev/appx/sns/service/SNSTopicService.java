package com.dev.appx.sns.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.dev.appx.sns.AmazonClientProvider;
import com.dev.appx.sns.config.SNSConfigurator;
import com.dev.appx.sns.data.PushMessage;
import com.dev.appx.sns.data.TopicSubscriptionRequestBody.TopicSubscription;

/**
 * SNS Topic related service.
 * 
 * @author nthusitha
 *
 */
public class SNSTopicService {

	AmazonSNS client = null;
	private static final String defaultMsg = "You have a notification";
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private SNSConfigurator snsConfig = new SNSConfigurator();
	private static final String MODE_STRING = "app.mode";
	
	/*
	private static String APPLICATION_SUCCESS_ATTRIBUTE_NAME = "ApplicationSuccessFeedbackRoleArn";
	private static String APPLICATION_SUCCESS_ATTRIBUTE_VALUE = "arn:aws:iam::033174993170:role/SNSSuccessFeedback";
	private static String APPLICATION_FAILURE_ATTRIBUTE_NAME = "ApplicationFailureFeedbackRoleArn";
	private static String APPLICATION_FAILURE_ATTRIBUTE_VALUE = "arn:aws:iam::033174993170:role/SNSFailureFeedback";
	*/
	public SNSTopicService() throws IOException {
		client = AmazonClientProvider.getClientInstance().getClient();
	}

	public static String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}

	public void createTopic(String topic) throws IOException {
		// create a new SNS client and set endpoint

		client.setRegion(Region.getRegion(Regions.US_EAST_1));
		// create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(topic);
		CreateTopicResult createTopicResult = client
				.createTopic(createTopicRequest);
		// print TopicArn
		System.out.println(createTopicResult);
		// get request id for CreateTopicRequest from SNS metadata
		System.out.println("CreateTopicRequest - "
				+ client.getCachedResponseMetadata(createTopicRequest));
	}

	public void deleteTopic(String topicArn) throws IOException {

		// delete an SNS topic
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		client.deleteTopic(deleteTopicRequest);
		// get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - "
				+ client.getCachedResponseMetadata(deleteTopicRequest));

	}

	public void subscribeToTopic(List<TopicSubscription> topics) {

		for (TopicSubscription subs : topics) {
			if (subs.isValid()) {
				// subscribe to an SNS topic
				SubscribeRequest subRequest = new SubscribeRequest(
						subs.getTopicArn(), "application",
						subs.getEndpointArn());
				
				//setting topic attributes
				/*
				System.out.println("Setting topic attributes for enabling CloudWatch monitoring");
				SetTopicAttributesRequest setTopicAttributesRequest = new SetTopicAttributesRequest();
				
				setTopicAttributesRequest.setAttributeName(APPLICATION_SUCCESS_ATTRIBUTE_NAME);
				setTopicAttributesRequest.setAttributeValue(APPLICATION_SUCCESS_ATTRIBUTE_VALUE);
				
				System.out.println(APPLICATION_SUCCESS_ATTRIBUTE_NAME + "=" + APPLICATION_SUCCESS_ATTRIBUTE_VALUE);
				setTopicAttributesRequest.setAttributeName(APPLICATION_FAILURE_ATTRIBUTE_NAME);
				setTopicAttributesRequest.setAttributeValue(APPLICATION_FAILURE_ATTRIBUTE_VALUE);
				
				System.out.println(APPLICATION_FAILURE_ATTRIBUTE_NAME + "=" + APPLICATION_FAILURE_ATTRIBUTE_VALUE);
				
				setTopicAttributesRequest.setTopicArn(subs.getTopicArn());
				client.setTopicAttributes(setTopicAttributesRequest);
				*/
				
				
				SubscribeResult result = client.subscribe(subRequest);
				// get request id for SubscribeRequest from SNS metadata
				System.out.println("SubscribeRequest - "
						+ client.getCachedResponseMetadata(subRequest));
				System.out
						.println("Check your email and confirm subscription.");
				
				System.out.print("subscriptionArn: " + result.getSubscriptionArn());
				subs.setSubscriptionArn(result.getSubscriptionArn());
			}
		}

	}

	/**
	 * Un-subscribe from a topic.
	 * @param subscriptionArn
	 */
	public void unSubscribeToTopic(String subscriptionArn){
		
		if(subscriptionArn != null && !subscriptionArn.isEmpty()){
			
			UnsubscribeRequest unsub = new UnsubscribeRequest();
			
			unsub.withSubscriptionArn(subscriptionArn);
			
			client.unsubscribe(unsub);
		}

	}
	

	public String publishToTopic(PushMessage pMsg) {
		PublishRequest publishRequest = new PublishRequest();

		publishRequest.setMessageStructure("json");
		// If the message attributes are not set in the requisite method,
		// notification is sent with default attributes
		String message = getSynergyMsg(pMsg);
		System.out.print("Publishing message : " + "\n" + message);
		// For direct publish to mobile end points, topicArn is not relevant.
		publishRequest.setTopicArn(pMsg.getTopicArn());

		publishRequest.setMessage(message);
		PublishResult result = client.publish(publishRequest);
		System.out.println("Published MessageId - " + result.getMessageId());
		return result.getMessageId();
	}

	public String getSynergyMsg(PushMessage pMsg) {

		Map<String, Object> droidMsg = getDroidMsg(pMsg);
		Map<String, Object> appleMsg = getAppleMsg(pMsg);
		String defaultMsg = getDefaultMsg();
		Map<String, Object> synergyMsg = new HashMap<String, Object>();
		
		synergyMsg.put("default", defaultMsg);
		synergyMsg.put("GCM", droidMsg);
		try {
			if(snsConfig.getValue(MODE_STRING).equals("dev")){
				synergyMsg.put("APNS_SANDBOX", appleMsg);
			}else if(snsConfig.getValue(MODE_STRING).equals("prod")){
				synergyMsg.put("APNS", appleMsg);
			}
		} catch (IOException e) {
			System.out.println("Error loading mode settings from properties file.");
		}
		
		
		
		//return jsonify(synergyMsg);
		return toSNSJson(synergyMsg);

	}
	
	@SuppressWarnings("unchecked")
	private String toSNSJson(Map<String, Object> data){
		StringBuilder json = new StringBuilder();
		json.append("{");
		for(Map.Entry<String, Object> entry : data.entrySet()){
			
			if(entry.getKey().equals("default")){
				json.append("\"").append("default").append("\"").append(":").append("\"").append(entry.getValue()).append("\",");
			}else{
				
				json.append("\"").append(entry.getKey()).append("\"").append(":");
				json.append("\"").append("{");
				if(entry.getValue() instanceof Map){
					
					for(Map.Entry<String, Object> subEntry : ((Map<String, Object>) entry.getValue()).entrySet()){
						json.append("\\").append("\"").append(subEntry.getKey()).append("\\").append("\"").append(":");
						
						
						if(subEntry.getValue() instanceof Map){
							json.append("{");
							for(Map.Entry<String, Object> subEntry1 : ((Map<String, Object>) subEntry.getValue()).entrySet()){
								
								json.append("\\").append("\"").append(subEntry1.getKey()).append("\\").append("\"").append(":");
								
								json.append("\\").append("\"").append(subEntry1.getValue()).append("\\").append("\"").append(",");
								
								
							}
							//remove last comma character
							json.deleteCharAt(json.length() - 1);
							json.append("}").append(",");
							
						}else{
							json.append("\\").append("\"").append(subEntry.getValue()).append("\\").append("\"").append(",");
						}
						
						
					}				
					//remove last comma character
					///json.
					json.deleteCharAt(json.length() - 1);
					json.append("}").append("\"").append(",");
						
				}else{
					json.append("\\").append("\"").append(entry.getValue()).append("\\").append("\"").append(",");
				}
				
				//json.deleteCharAt(json.length() - 1);
				//json.append("}").append(",");
			}

			
		}
		json.deleteCharAt(json.length() - 1);
		json.append("}");
		return json.toString();
		
	}
	

	private String getDefaultMsg() {
		return defaultMsg;
	}

	private Map<String, Object> getDroidMsg(PushMessage pMsg) {
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		androidMessageMap.put("collapse_key", "Welcome");
		androidMessageMap.put("data", getData(pMsg));
		androidMessageMap.put("delay_while_idle", true);
		androidMessageMap.put("time_to_live", 125);
		androidMessageMap.put("dry_run", false);

		return androidMessageMap;
	}

	private static Map<String, String> getData(PushMessage pMsg) {
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("message", pMsg.getMessage());
		payload.put("extra1", pMsg.getExtra1());
		return payload;
	}

	private Map<String, Object> getAppleMsg(PushMessage pMsg) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("alert", pMsg.getMessage());
		appMessageMap.put("badge", 9);
		appMessageMap.put("sound", "default");
		appMessageMap.put("extra1", pMsg.getExtra1());
		appleMessageMap.put("aps", appMessageMap);
	//	appleMessageMap.put("extra1", pMsg.getExtra1());
		return appleMessageMap;
	}

}
