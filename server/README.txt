Amazon SNS Mobile Push
----------------------------------------------------

This application will send a notification to an application on a mobile device. The following platforms are currently supported:
* GCM (Android), 
* APNS and APNS_SANDBOX (iOS)


Stand-alone Server Mode
----------------------------------------------------

Run, mvn clean package
Run, java -jar {path to jar}/sns-service-wrapper-jar-with-dependencies.jar

Interesting Java Classes
----------------------------------------------------
Please take a look at following two Java classes. It covers almost all functionalities we
perform with SNS API.

/sns-broker/src/main/java/com/dev/appx/sns/service/SNSRegistrationService.java

/sns-broker/src/main/java/com/dev/appx/sns/service/SNSTopicService.java


Sample Registration Request - Brand new registration
----------------------------------------------------
HTTP POST

{
    "token" : "APA91bG8SVJ685S6Xi1JNaVM3pLTDXV0NsEjGXTSqrbVlF6FrdbopT46Q45Hq7hQ8Q_WPDCcZygI0ToMSJt6u_F6qayFbW6vY-W5Fk3DjDf69JM_uuG7h1eJsKalGY4A4e23bu3Y",
    "platform" : "GCM",
    "appName" : "EPOD"
}

Response - Raw response extraced directly from the response body
arn:aws:sns:us-west-2:033174993170:endpoint/GCM/EPOD/27b55f95-a82b-389d-aafa-0f472cbab98c


Deployment mode selection
----------------------------------------------------
/sns-broker/src/main/resources/settings.properties
Change the mode to following
app.mode:{dev|prod}


Subscribe for a topic

HTTP POST

{
    "topicSubscriptions" : [
        {
            "topicArn" : "arn:aws:sns:us-west-2:033174993170:epod-test",
            "endpointArn" : "arn:aws:sns:us-west-2:033174993170:endpoint/GCM/EPOD/78172b44-ef20-3639-ae1c-abe0fed6c9f0"
        }
        
        ]
}

Run sample:

1- Specify your AWS Access Key and AWS Secret Key in AwsCredentials.properties.
2- Undo the comment for the relevant platform, e.g.
                sample.demoAndroidAppNotification(Platform.GCM);
                for android.
3- Enter the registration information for the platform e.g.
                registrationId
                ServerAPIKey
                applicationName
                for android.
4- *OPTIONAL* Comment out the line to delete the platform application to continue using the test platform application
i.e.
                //deletePlatformApplication(platformApplicationArn);
5- Make sure to have the AWS SDK for Java, found here http://aws.amazon.com/sdkforjava/
	and configure your build path.
	OR
	Use the included run.sh file found in the src folder.
    OR
    Use the included run.bat founds in the src folder.

For more information about Amazon SNS Mobile Push, please see docs.aws.amazon.com/sns/latest/dg/SNSMobilePush.html
For licensing information about this sample, please see the included LICENSE.txt.




