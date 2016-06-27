/**
 * 
 */
package com.dev.appx.sns.auth;

/**
 * 
 * SNS access manager for various push notification providers;
 * GCM, APNS.
 * @author nthusitha
 *
 */
public class ServerKeys {

	private String principal;
	private String credential;
	
	
	public ServerKeys(String principal, String credential) {
		super();
		this.principal = principal;
		this.credential = credential;
	}


	public String getPrincipal() {
		return principal;
	}




	public String getCredential() {
		return credential;
	}


}
