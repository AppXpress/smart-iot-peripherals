/**
 * 
 */
package com.dev.appx.sns.test;



import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import spark.utils.Assert;

import com.dev.appx.sns.service.SNSFactory;
import com.dev.appx.sns.tools.SampleMessageGenerator.Platform;

/**
 * @author nthusitha
 *
 */

public class SNSRegisterTest {

	private SNSFactory sns;
	private String endPointErn;
	private String deviceToken;
	private String topicArn;
	private String iosDeviceToken;
	
	
	@Before
	public void setup(){
		sns = new SNSFactory();
		endPointErn = "arn:aws:sns:us-west-2:033174993170:endpoint/GCM/EPOD/78172b44-ef20-3639-ae1c-abe0fed6c9f0";
		deviceToken = "APA91bG8SVJ685S6Xi1JNaVM3pLTDXV0NsEjGXTSqrbVlF6FrdbopT46Q45Hq7hQ8Q_WPDCcZygI0ToMSJt6u_F6qayFbW6vYrq_b-W5Fk3DjDf69JM_uuG7h1eJsKalGY4A4e23bu3Y";
		topicArn = "arn:aws:sns:us-west-2:033174993170:epod-test";
		iosDeviceToken = "08dab9c5483f7902fd309782ab2ec33ff3c076f3bd3548a7a790f54b04343d18";
	}
	
	
	//@Test
	public void testRegistration(){
		
		String endPointArn = "undefined";
		try {
			endPointArn = sns.getRegistrationService().registerWithSNS(null, deviceToken, Platform.GCM, "EPOD");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			org.junit.Assert.fail(e.getMessage());
		}
		System.out.print(endPointArn);
		Assert.isTrue(endPointArn != "undefined", "Didn't get endPointArn from SNS");;
	}
	
	@Test
	public void testAPNSReg(){
		
		String endPointArn = "undefined";
		try {
			endPointArn = sns.getRegistrationService().registerWithSNS(null, iosDeviceToken, Platform.APNS_SANDBOX, "EPOD");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			org.junit.Assert.fail(e.getMessage());
		}
		System.out.print(endPointArn);
		Assert.isTrue(endPointArn != "undefined", "Didn't get endPointArn from SNS");;
	}
	

}
