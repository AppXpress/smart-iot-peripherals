/**
 *  HTTP Service
 *  @author: Saliya Ruwan
 *  Service to handle http request (GET, POST)
 */
angular.module('app.services')
    .factory('$httpService', ['$http', '$q', '$config', function ($http, $q, $config) {

        return {
            get: doGet,
            post: doPost
        };

        /**
         * GET request
         * @param authToken : authorization token
         * @param uri : oql query + dataKey for url 
         * usage: $httpService.get(<authToken>,<uri>);
         */
        function doGet(authToken, uri) {
            url = (uri) ? $config.basePath + uri : $config.basePath;
            return $http.get(url, {
                headers: {
                    'Authorization': authToken,
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json'
                    //'Cache-Control' : 'no-cache' 
                }
            });
        }

        /**
         * POST request
         * @param authToken : authorization token
         * @param uri : oql query + dataKey for url
         * @param data : data for payload
         * @param fingerprint : value for IF-Match header
         * usage: $httpService.post(<authToken>,<uri>,<data>,<if-match>);
         */
        function doPost(authToken, uri, data, fingerprint) {
            url = (uri) ? $config.basePath + uri : $config.basePath;
            var headers_ = {
                    'Authorization': authToken,
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json', 
                    'If-Match': fingerprint,
                    // 'Cache-Control' : 'no-cache'
                };
            
            if(fingerprint == 'undefined' || fingerprint == null){
            	delete headers_['If-Match'];
            }
            return $http.post(url, data, {
                headers: headers_
            });
        }

}]);