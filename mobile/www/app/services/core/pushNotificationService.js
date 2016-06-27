/*
 * @Description: Unified Push Notification Service.
 * @author: NThusitha
 * @date: 23-Sep-2015
 * 
 * */
angular.module('app.services').factory('PushService', function ($rootScope, $http, $cordovaPush, $cordovaDevice, $log, $config, $filter) {

    /*
     * Bootstrap push plugin.
     * */
    function register_(onRegisterCallback, notificationCallback) {

        var platform = $cordovaDevice.getPlatform();
        switch (platform) {

            case "iOS" :
                $log.debug("platform is ios");
                var iosConfig = {
                    "badge": true,
                    "sound": true,
                    "alert": true,
                };
                $cordovaPush.register(iosConfig).then(function (deviceToken) {
// Success -- send deviceToken to server, and store for future use
                    $log.debug("deviceToken: " + deviceToken)
                    getPushDSubscriberId_(deviceToken, "apnsSwift").success(onRegisterCallback);
                }, function (err) {
                    $log.error("Registration error (APNS): " + err)
                });
                $rootScope.$on('$cordovaPush:notificationReceived', function (event, notification) {
                    $log.debug("apns notification received");
                    if (notification.alert) {
                      //  navigator.notification.alert(notification.alert);
                    }

                    if (notification.sound) {
                        var snd = new Media(event.sound);
                        snd.play();
                    }


                    notificationCallback(notification);
                    if (notification.badge) {
                        $cordovaPush.setBadgeNumber(notification.badge).then(function (result) {
                            $log.debug("set the badge number" + result);
                        }, function (err) {
                            $log.debug("error occured setting badge number");
                        });
                    }
                });
                break;
            case "Android" :
                $log.debug("platform is android");
                var androidConfig = {
                    "senderID": $config.push.gcm.senderId,
                };
                $cordovaPush.register(androidConfig).then(function (result) {
                    $log.debug("successfully registered" + "\n" + result);
                    //onRegisterCallback(result, "gcm");
                }, function (err) {
                    $log.error("Registration error (GCM) :" + err);
                })

                $rootScope.$on('$cordovaPush:notificationReceived', function (event, notification) {
                    switch (notification.event) {
                        case 'registered':
                            if (notification.regid.length > 0) {
                                $log.debug('GCM registration ID = ' + notification.regid);
                                getPushDSubscriberId_(notification.regid, "gcm").success(onRegisterCallback);
                            }
                            break;
                        case 'message':

                            /*
                             * Notification Msg
                             * 
                             *  "{"event":"message","from":"169925722149","message":"test","collapse_key":"unicast:03I5QtPVF_U","foreground":true,"payload":{"polluid":"123","title":"msg to htc","message":"test"}}" 
                             *  polluid is actual data
                             */
                            $log.debug("gcm notification received");
                            // this is the actual push notification. its format depends on the data model from the push server
                            $log.debug('message = ' + notification.message + ' msgCount = ' + notification.msgcnt);

                            notificationCallback(notification);





                            break;
                        case 'error':
                            $log.error('GCM error = ' + notification.msg);
                            break;
                        default:
                            $log.error('An unknown GCM event has occurred');
                            break;
                    }
                });
                break;
            default:


                //	http://solutionoptimist.com/2013/10/07/enhance-angularjs-logging-using-decorators/
        }
    }
    /*
     * Un register from the Push servers.
     * */
    function unregister_(unregisterCallback) {

// WARNING! dangerous to unregister (results in loss of tokenID)
        $cordovaPush.unregister(options).then(function (result) {
            unregisterCallback(result);
        }, function (err) {
            $log.error("error occured unregistering : " + e);
        });
    }

    function transformToFormPost(data, headers) {

// rewrite headers
        headers["Content-Type"] = "application/x-www-form-urlencoded;"; // no charset!
        headers['X-Requested-With'] = "XMLHttpRequest";
        // If this is not an object, defer to native stringification.
        if (!angular.isObject(data)) {
            return ((data == null) ? "" : data.toString());
        }

        var buffer = [];
        // Serialize each key in the object.
        for (var name in data) {
            if (!data.hasOwnProperty(name)) {
                continue;
            }
            var value = data[name];
            buffer.push(
                    name +
                    "=" +
                    encodeURIComponent((value == null) ? "" : value)
                    );
        }
// Serialize the buffer and clean it up for transportation.
        var source = buffer
                .join("&")
                .replace(/%20/g, "+");
        return (source);
    }

    function toString(token) {
        return token;
    }

    /* Default onRegistration call back implementation. Call .success for response handling */
    function getPushDSubscriberId_(deviceToken, provider) {

        var proto_, token_, lang_, badge_, category_, contentAvailable_;
        proto_ = toString(provider);
        token_ = toString(deviceToken);
        lang_ = toString("en");
        badge_ = toString("0");
        category_ = toString("show");
        contentAvailable_ = toString("true");
        var pushdConfig = {
            proto: proto_, //provider = [apnsSwift, gcm]
            token: token_,
            lang: lang_,
            badge: badge_,
            category: category_,
            contentAvailable: contentAvailable_
        };
        // var headers = new Object();
        //var postData = transformToFormPost(pushdConfig, headers);
        var subscribersUrl = $config.push.server + $config.push.subscribers_endpoint;
        return $http({
            method: 'POST',
            url: subscribersUrl,
            data: pushdConfig
                    // headers: headers
        });
    }
    /*
     * 
     *  Data Format: 
     { 
     "pushData" : {
     "key": "value"
     
     }, "subscribers" : ["abc", "cde"]
     
     }
     * 
     * 
     */
    function unicast_(data) {
        var unicastUrl = $config.push.server + $config.push.unicast_endpoint;
        return $http({
            method: 'POST',
            url: unicastUrl,
            data: data
        });

    }

    return {
        registerPushNotifications: function (onRegistrationCallback, onNotificationCallback) {
            register_(onRegistrationCallback, onNotificationCallback);
        },
        unregisterPushNotifications: function (unregisterCallback) {
            unregister_(unregisterCallback);
        },
        unicast: function (data) {
            return unicast_(data);
        }
    }

});