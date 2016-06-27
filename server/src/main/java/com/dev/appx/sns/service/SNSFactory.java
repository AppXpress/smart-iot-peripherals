/**
 * 
 */
package com.dev.appx.sns.service;

import java.io.IOException;

/**
 * SNS Factory.
 * Use this class for accessing services.
 * @author nthusitha
 *
 */
public class SNSFactory {

	private SNSRegistrationService regService;
	private SNSTopicService topicService;
	
	public SNSFactory(){
		
			try {
				regService = new SNSRegistrationService();

				topicService = new SNSTopicService();
			} catch (IOException e) {
				System.out.print(e);
			}
		
		
	}
	
	public SNSRegistrationService getRegistrationService(){
		return regService;
	}
	
	public SNSTopicService getTopicService(){
		return topicService;
	}
}
