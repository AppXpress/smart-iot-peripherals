/**
 *  Utils 
 *  @author: Saliya Ruwan
 *  For reusable functions 
 */
angular.module('app.services')
    .factory('$utils', ['$config', function ($swiftConfig) {

        function formatDate(date) {
            var d = new Date(date),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

            if (month.length < 2) month = '0' + month;
            if (day.length < 2) day = '0' + day;

            return [year, month, day].join('-');
        }

        return {
            formatDate: formatDate,
        };

}]);