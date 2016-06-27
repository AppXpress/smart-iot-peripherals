/**
 * Authentication Service
 * 
 * @author: Saliya Ruwan 
 * @desc : Service to handle user authentication with AppXpress REST API (Login and Logout)
 */
angular.module('app.services').factory('$authentication', authentication);

authentication.$inject = ['$httpService', '$localStorageService', '$window', '$log', '$config',
    '$platformService', '$ionicLoading'
];

function authentication($httpService, $localStorageService, $window, $log, $config, $platformService, $ionicLoading) {

    /**
     * user authentication usage: $authentication.authenticate(<username>,<password>,function(status,
     * data){});
     */
    function getAuthentication(username, password, callback) {
        var separator = String.fromCharCode(0x1F);
        var authorization = "Basic " + $window.btoa(username + separator + password);

        var uri = 'User/self?dataKey=' + $config.dataKey;

        $ionicLoading.show({
            template: '<ion-spinner icon="bubbles"></ion-spinner><p style="background-color: black; padding:5px; border-radius:5px"> Authenticating </p>'
        });
        var request = $httpService.get(authorization, uri);
        request.success(function(data, status, headers) {
            if (status == 200) {
                $localStorageService.set('authorization', authorization);
                $localStorageService.set('username', username);
                $localStorageService.setObject('user', data);

                $ionicLoading.show({
                    template: '<ion-spinner icon="bubbles"></ion-spinner><p style="background-color: black; padding:5px; border-radius:5px"> Loading </p>'
                });

                return;
            }
            $ionicLoading.hide();
            callback(false, "Invalid username or password");
            $log.debug('status : ' + status);
        }).error(function(data, status) {
            $ionicLoading.hide();
            $log.error('ERROR Status :' + status);
            if (status == 401) {
                callback(false, "Invalid username or password");
                return;
            }
            callback(false, "NETWORK ERROR");

        })
    }

    /**
     * user logout usage: $authentication.logout();
     */
    function logout() {
        $localStorageService.clearAll();
    }

    /**
     * check user is logged in
     * 
     * @return boolean usage: var result = $authentication.isLoggedIn();
     */
    function isLoggedIn() {
        var authToken = $localStorageService.get('authorization');
        if (authToken) {
            return true;
        }
        return false;
    }

    return {
        authenticate: getAuthentication,
        logout: logout,
        isLoggedIn: isLoggedIn
    };

}
