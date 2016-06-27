/**
 *  Geolocation service 
 *  @author : saliya ruwan
 *  @description : get geolocation information of the device and handle geolocation details
 *  @TODO : test the functionalities with devices (android and ios)
 **/

angular.module('app.services').factory('GeolocationService', GeolocationService);
GeolocationService.$inject = ['$cordovaGeolocation'];

function GeolocationService($cordovaGeolocation) {
    var services = {
        getCurrentPosition: getCurrentPosition,
        watchPosition: watchPosition,
        clearWatch: clearWatch
    };

    return services;

    /**
     *  get current geolocation of the device
     **/
    function getCurrentPosition(onSuccess, onError) {
        //NOTE : can set the options in the global config file 
        var posOptions = {
            timeout: 10000,
            //maximumAge: 30000, //Accept a cached position whose age is no greater than the specified time in milliseconds
            enableHighAccuracy: false
        };

        $cordovaGeolocation.getCurrentPosition(posOptions).then(onSuccess, onError);
    }

    /**
     *  get the device's current position when a change in position is detected. 
     **/
    function watchPosition(onSuccess, onError) {
        //NOTE : can set the options in the global config file 
        var watchOptions = {
            timeout: 10000,
            enableHighAccuracy: false
        };

        this.watch = $cordovaGeolocation.watchPosition(watchOptions).then(null, onError, onSuccess);
    }


    /**
     *  Clear watch
     **/
    function clearWatch(onSuccess, onError) {
        if (this.watch) {
            this.watch.clearWatch();
        }
    }

}