/**
 * 
 */
package com.dev.appx.sns.data;

import com.dev.appx.sns.config.SNSConfigurator;
import com.dev.appx.sns.tools.SampleMessageGenerator.Platform;

/**
 * 
 * Following are stages of SNS registrations;
 * 
 * If client is registering for the first time, following are mandatory;
 * token, platform, appName, endPointArn = null
 * If client has registered but need to update the token, following are required;
 * endPointArn, token, platform, appName (i.e.appName : EPOD, platform : GCM)
 *
 * @author nthusitha
 *
 */
public class RegisterRequestBody {

	private String endPointArn;
	private String token;
	private Platform platform;
	private SNSConfigurator.SNSApp appName;
	
	public boolean isValid(){
		return token != null && platform != null && appName != null;
	}
	
	
	public String getEndPointArn() {
		return endPointArn;
	}
	public void setEndPointArn(String endPointArn) {
		this.endPointArn = endPointArn;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public SNSConfigurator.SNSApp getAppName() {
		return appName;
	}
	public void setAppName(SNSConfigurator.SNSApp appName) {
		this.appName = appName;
	}


	@Override
	public String toString() {
		return "RegisterRequestBody [endPointArn=" + endPointArn + ", token="
				+ token + ", platform=" + platform + ", appName=" + appName
				+ "]";
	}
	
	
	
	
	
}
