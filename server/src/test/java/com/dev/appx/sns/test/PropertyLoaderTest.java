package com.dev.appx.sns.test;

import java.io.IOException;

import org.junit.Test;

import com.dev.appx.sns.config.SNSConfigurator;

/**
 * @author nthusitha
 *
 */
public class PropertyLoaderTest {

	private final static String PROP_KEY = "app.mode";
	
	@Test
	public void testPropertyLoader(){
		
		SNSConfigurator config = new SNSConfigurator();
		
		try {
			String val = config.getValue(PROP_KEY);
			System.out.println("Property key " + PROP_KEY + "value " + val);
		} catch (IOException e) {

			e.printStackTrace();
			
			org.junit.Assert.fail(e.getMessage());
		}
	}
}
