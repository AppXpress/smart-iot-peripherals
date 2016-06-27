/**
 * 
 */
package com.dev.appx.sns.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dev.appx.sns.data.PushMessage;
import com.dev.appx.sns.data.TopicSubscriptionRequestBody.TopicSubscription;
import com.dev.appx.sns.service.SNSFactory;

/**
 * @author nthusitha
 *
 */
public class SNSTopicTest {

	private SNSFactory sns;
	private String endPointErn;
	private String topicArn;
	private String topicArn1;
	private String iosEndpointArn;

	@Before
	public void setup() {
		sns = new SNSFactory();
		endPointErn = "arn:aws:sns:us-west-2:033174993170:endpoint/GCM/EPOD/78172b44-ef20-3639-ae1c-abe0fed6c9f0";
		topicArn = "arn:aws:sns:us-west-2:033174993170:epod-test";
		topicArn1 = "arn:aws:sns:us-west-2:033174993170:epod-arrivedAtPickup";
		iosEndpointArn = "arn:aws:sns:us-west-2:033174993170:endpoint/APNS_SANDBOX/EPOD/19a9b117-fcf3-3083-8869-06169e2b9493";
	}

	//@Test
	public void testTopicSubscription() {
		// TODO: test topic subscription

		List<TopicSubscription> topicSubscriptions = new ArrayList<TopicSubscription>();
		TopicSubscription ts1 = new TopicSubscription();
		ts1.setEndpointArn(endPointErn);
		ts1.setTopicArn(topicArn);

		topicSubscriptions.add(ts1);
		try {
			sns.getTopicService().subscribeToTopic(topicSubscriptions);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}
	//@Test
	public void testIosTopicSubscription(){
		List<TopicSubscription> topicSubscriptions = new ArrayList<TopicSubscription>();
		TopicSubscription ts1 = new TopicSubscription();
		ts1.setEndpointArn(iosEndpointArn);
		ts1.setTopicArn(topicArn1);

		topicSubscriptions.add(ts1);
		try {
			sns.getTopicService().subscribeToTopic(topicSubscriptions);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	//@Test
	public void testTopicPublish() {

		PushMessage msg = new PushMessage();
		msg.setTopicArn(topicArn1);
		msg.setMessage("Test Msg");
		try {

			sns.getTopicService().publishToTopic(msg);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	@Test
	public void topicUnsubscribe(){
		sns.getTopicService().unSubscribeToTopic("arn:aws:sns:us-west-2:033174993170:epod-test");
	}
	

}
