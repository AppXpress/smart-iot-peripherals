package com.dev.appx.sns.server;

import static spark.Spark.post;
import static spark.Spark.after;
import static com.dev.appx.sns.tools.JSONUtil.*;

import com.dev.appx.sns.data.PushMessage;
import com.dev.appx.sns.data.RegisterRequestBody;
import com.dev.appx.sns.data.TopicSubscriptionRequestBody;
import com.dev.appx.sns.data.TopicUnSubscriptionRequestBody_;
import com.dev.appx.sns.data.TopicUnSubscriptionRequestBody_.TopicUnsubscriber;
import com.dev.appx.sns.service.SNSFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Default listening port is 4567
 * 
 * @author nthusitha
 *
 */
public class HTTPEndpoint {

	private static final int HTTP_BAD_REQUEST = 400;
	private static final int HTTP_INTERNAL_ERROR = 500;
	private SNSFactory sns = new SNSFactory();

	public void listen() {

		/*
		 * Return endPointArn value in the response.
		 * 
		 * Sample Request
		 * 
		 * { "token" :
		 * "APA91bG8SVJ685S6Xi1JNaVM3pLTDXV0NsEjGXTSqrbVlF6FrdbopT46Q45Hq7hQ8Q_WPDCcZygI0ToMSJt6u_F6qayFbW6vY-W5Fk3DjDf69JM_uuG7h1eJsKalGY4A4e23bu3Y"
		 * , "platform" : "GCM", "appName" : "EPOD" }
		 */
		post("/register",
				(req, res) -> {

					try {
						ObjectMapper mapper = new ObjectMapper();
						RegisterRequestBody regReqBody = mapper.readValue(
								req.body(), RegisterRequestBody.class);
						
						System.out.println(regReqBody.toString());
						
						if (!regReqBody.isValid()) {
							res.status(HTTP_BAD_REQUEST);
							return "";
						}

						String endpointArn = sns.getRegistrationService()
								.registerWithSNS(regReqBody.getEndPointArn(),
										regReqBody.getToken(),
										regReqBody.getPlatform(),
										regReqBody.getAppName().name());

						res.status(200);
						// res.type("application/json");
						return new RegisterResponse()
								.setEndpointArn(endpointArn);
					} catch (JsonParseException jpe) {
						res.status(HTTP_BAD_REQUEST);
						return "";
					} catch (Exception e) {
						printErr(e);
						res.status(HTTP_INTERNAL_ERROR);
						return "";
					}
				}, json());

		after((req, res) -> {
			res.type("application/json");
		});

		/*
		 * Sample subscription request
		 * 
		 * { "topicSubscriptions" : [ { "topicArn" :
		 * "arn:aws:sns:us-west-2:033174993170:epod-test", "endpointArn" :
		 * "arn:aws:sns:us-west-2:033174993170:endpoint/GCM/EPOD/78172b44-ef20-3639-ae1c-abe0fed6c9f0"
		 * }
		 * 
		 * ] }
		 */
		post("/subscribe",
				(req, res) -> {

					try {
						ObjectMapper mapper = new ObjectMapper();
						TopicSubscriptionRequestBody subsc = mapper.readValue(
								req.body(), TopicSubscriptionRequestBody.class);

						System.out.println("Topic subscription request body >>");
						System.out.println(subsc.toString());
						
						if (!subsc.isValid()) {
							res.status(HTTP_BAD_REQUEST);
							return "";
						}

						sns.getTopicService().subscribeToTopic(
								subsc.getTopicSubscriptions());

						res.status(200);
						// res.type("application/json");
						return subsc.getTopicSubscriptions();
					} catch (Exception e) {
						printErr(e);
						res.status(HTTP_INTERNAL_ERROR);
						return "";
					}
				}, json());

		/*
		 * 
		 * Sample Un-Subscribe request
		 * 
		 * { "subscriptionArns" : [{ "subscriptionArn" :
		 * "arn:aws:sns:us-west-2:033174993170:epod-test" } ] } 
		 */
		post("/unsubscribe",
				(req, res) -> {

					try {
						ObjectMapper mapper = new ObjectMapper();
						TopicUnSubscriptionRequestBody_ subsc = mapper
								.readValue(req.body(),
										TopicUnSubscriptionRequestBody_.class);

						System.out.println("Topic un-subscription request body >>");
						System.out.println(subsc.toString());
						
						if (!subsc.isValid()) {
							res.status(HTTP_BAD_REQUEST);
							return "";
						}

						for (TopicUnsubscriber unsub : subsc.getSubscriptionArns()) {
							sns.getTopicService().unSubscribeToTopic(
									unsub.getSubscriptionArn());
						}

						res.status(200);
						// res.type("application/json");
						return new UnSubscribeTopicResponse().setMsg("Ok");
					} catch (Exception e) {
						printErr(e);
						res.status(HTTP_INTERNAL_ERROR);
						return "";
					}

				}, json());

		/**
		 * Returns msgId value in the response.
		 * */
		post("/publishTopic",
				(req, res) -> {

					try {
						ObjectMapper mapper = new ObjectMapper();
						PushMessage pMsg = mapper.readValue(req.body(),
								PushMessage.class);
						
						System.out.println(pMsg.toString());
						
						if (!pMsg.isValid()) {
							res.status(HTTP_BAD_REQUEST);
							return "";
						}

						String msgId = sns.getTopicService().publishToTopic(
								pMsg);
						res.status(200);
						// res.type("application/json");
						return msgId;
					} catch (Exception e) {
						printErr(e);
						res.status(HTTP_INTERNAL_ERROR);
						return "";
					}
				}, json());

	}

	private void printErr(Object o) {
		System.out.println("error occured" + "\n");
		System.out.print(o);
	}

	public static class RegisterResponse {

		private String endpointArn;

		public String getEndpointArn() {
			return endpointArn;
		}

		public RegisterResponse setEndpointArn(String endpointArn) {
			this.endpointArn = endpointArn;
			return this;
		}

	}

	public static class UnSubscribeTopicResponse {
		private String msg;

		public String getMsg() {
			return msg;
		}

		public UnSubscribeTopicResponse setMsg(String msg) {
			this.msg = msg;

			return this;
		}

	}

}
