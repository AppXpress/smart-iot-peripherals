/**
 * 
 */
package com.dev.appx.sns.tools;

import spark.ResponseTransformer;

import com.google.gson.Gson;

/**
 * @author nthusitha
 *
 */
public class JSONUtil {

	public static String toJson(Object obj) {
		return obj != null ? new Gson().toJson(obj) : "{}";
	}

	public static ResponseTransformer json() {
		return JSONUtil::toJson;
	}
}
