/**
 * 
 */
package com.dev.appx.sns.data;

/**
 * The push message will be broadcast
 * to all relevant topic endpoint.
 * All subscribers in various platforms (GCM, APNS)
 * will get the PushMessage content.
 * @author nthusitha
 *
 */
public class PushMessage {

	private String message;
	private String topicArn;
	//extra value (extra1 : "12345")
	/*
	 * Client can interpret extra1 value as unique identifier to
	 * fetch data from the backend. 
	 * */
	private String extra1;
	
	
	public boolean isValid(){
		return message != null && !message.isEmpty() && topicArn != null && !topicArn.isEmpty();
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getExtra1() {
		return extra1;
	}
	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}


	public String getTopicArn() {
		return topicArn;
	}


	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}


	@Override
	public String toString() {
		return "PushMessage [message=" + message + ", topicArn=" + topicArn
				+ ", extra1=" + extra1 + "]";
	}
	
	
	
	
	
	
}
