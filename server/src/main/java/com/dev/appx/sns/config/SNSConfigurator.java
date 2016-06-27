/**
 * 
 */
package com.dev.appx.sns.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Properties;

import spark.utils.Assert;

import com.dev.appx.sns.auth.ServerKeys;
import com.dev.appx.sns.tools.SampleMessageGenerator.Platform;

/**
 * SNS Configurator.
 * 
 * @author nthusitha
 *
 */
public class SNSConfigurator {

/*	private static final EnumMap<SNSApp, String> SERVER_KEYS = new EnumMap<SNSApp, String>(
			SNSApp.class);*/

	private static final String DEFAULT_SERVER_KEY_FILE = "/server.keys.properties";
	private static final String DEFAULT_SETTINGS_FILE = "/settings.properties";


	public enum SNSApp {
		EPOD;
	}

	/**
	 * Load server keys from the classpath.
	 * @param appName - app name for which serverkeys are loaded.
	 * @param p - platform (GCM, APNS
	 * @return
	 * @throws IOException
	 */
	public ServerKeys loadServerKeys(String appName, Platform p) throws IOException {
		
		Assert.hasLength(appName, "appName can't empty");
		Assert.notNull(p);
		InputStream stream = getClass().getResourceAsStream(
				DEFAULT_SERVER_KEY_FILE);
		StringBuilder principalPropKey = new StringBuilder();
		StringBuilder credentialPropKey = new StringBuilder();
		
		Properties props = new Properties();


		props.load(stream);

		principalPropKey.append(appName.toLowerCase()).append(".")
				.append(p.name().toLowerCase()).append(".principal");
		credentialPropKey.append(appName.toLowerCase()).append(".")
				.append(p.name().toLowerCase()).append(".credential");
		
		String principal = props.getProperty(principalPropKey.toString());
		String credential = props.getProperty(credentialPropKey.toString());
		
		
		//clear the property key builders
		principalPropKey.delete(0, principalPropKey.length());
		credentialPropKey.delete(0, credentialPropKey.length());
		
		System.out.print(p.name()+ " principal " + principal);
		System.out.print(p.name() + " credential " + credential);
		
		//replace null values with ""
		return new ServerKeys(principal == null ? "" : principal, credential == null ? "" : credential);
		
	}
	
	/**
	 * Load corresponding value for a given key from settings.properties.
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getValue(String key) throws IOException{
		
		Assert.hasLength(key, "key must have charachter length > 0");
		
		Properties props = load(DEFAULT_SETTINGS_FILE);
		String val = props.getProperty(key);
		System.out.println("loading property " + key + "value is " + val);
		
		return val;
		
	}
	
	public Properties load(String file) throws IOException{
		
		Assert.notNull(file, "can't load properties from null file");
		InputStream stream = getClass().getResourceAsStream(
				file);

		
		Properties props = new Properties();


		props.load(stream);
		
		return props;
		
	}

/*	static {
		SERVER_KEYS.put(SNSApp.EPOD, "AIzaSyABzDSISGL8MJBZb7mcOO8514CGo3fDLDQ");
	}

	public static String getServerKey(String appName) {

		String serverKey = "";
		for (SNSApp app : SNSApp.values()) {
			serverKey = app.name().equals(appName) ? SERVER_KEYS.get(app) : "";
		}
		return serverKey;
	}*/

}
