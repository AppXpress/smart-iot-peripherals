/**
 * 
 */
package com.dev.appx.sns.data;

import java.util.List;

import com.dev.appx.sns.data.TopicUnSubscriptionRequestBody_.TopicUnsubscriber;

/**
 * @author nthusitha
 *
 */
public class TopicSubscriptionRequestBody {

	private List<TopicSubscription> topicSubscriptions;

	public List<TopicSubscription> getTopicSubscriptions() {
		return topicSubscriptions;
	}

	public void setTopicSubscriptions(List<TopicSubscription> topicSubscriptions) {
		this.topicSubscriptions = topicSubscriptions;
	}
	
	public boolean isValid(){
		return topicSubscriptions != null && !topicSubscriptions.isEmpty();
	}
	


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(
				"TopicSubscriptionRequestBody [topicSubscriptions=");


		if (!getTopicSubscriptions().isEmpty()) {
			for (TopicSubscription sub : getTopicSubscriptions()) {
				sb.append(sub.toString());
			}
		}
		sb.append("]");

		return sb.toString();
	}





	public static class TopicSubscription {
		
		private String topicArn;
		private String endpointArn;
		private String subscriptionArn;

		public boolean isValid(){
			return topicArn != null && !topicArn.isEmpty() && endpointArn != null && !endpointArn.isEmpty();
		}

		public String getTopicArn() {
			return topicArn;
		}

		public void setTopicArn(String topicArn) {
			this.topicArn = topicArn;
		}

		public String getEndpointArn() {
			return endpointArn;
		}

		public void setEndpointArn(String endpointArn) {
			this.endpointArn = endpointArn;
		}

		public String getSubscriptionArn() {
			return subscriptionArn;
		}

		public void setSubscriptionArn(String subscriptionArn) {
			this.subscriptionArn = subscriptionArn;
		}

		@Override
		public String toString() {
			return "TopicSubscription [topicArn=" + topicArn + ", endpointArn="
					+ endpointArn + ", subscriptionArn=" + subscriptionArn
					+ "]";
		}
		
		
		
		
		
	}

}
