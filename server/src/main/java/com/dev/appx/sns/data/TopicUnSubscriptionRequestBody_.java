/**
 * 
 */
package com.dev.appx.sns.data;

import java.util.List;

/**
 * @author nthusitha
 *
 */
public class TopicUnSubscriptionRequestBody_ {

	private List<TopicUnsubscriber> subscriptionArns;

	public boolean isValid() {

		if (this.subscriptionArns != null & !this.subscriptionArns.isEmpty()) {
			return true;
		}

		return false;
	}

	public List<TopicUnsubscriber> getSubscriptionArns() {
		return subscriptionArns;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(
				"TopicUnSubscriptionRequestBody_ [subscriptionArns=[");


		if (!getSubscriptionArns().isEmpty()) {
			for (TopicUnsubscriber unSub : getSubscriptionArns()) {
				sb.append(unSub.toString());
			}
		}
		sb.append("]");

		return sb.toString();
	}

	public void setSubscriptionArns(List<TopicUnsubscriber> subscriptionArns) {
		this.subscriptionArns = subscriptionArns;
	}

	public static class TopicUnsubscriber {

		private String subscriptionArn;

		public String getSubscriptionArn() {
			return subscriptionArn;
		}

		public void setSubscriptionArn(String subscriptionArn) {
			this.subscriptionArn = subscriptionArn;
		}

		@Override
		public String toString() {
			return "TopicUnsubscriber [subscriptionArn=" + subscriptionArn
					+ "]";
		}

	}
}
