/*
 * Core router
 * Basic router configurations for the application
 */

angular.module('app.route').config(coreConfiguration);

coreConfiguration.$inject = ['$stateProvider'];

function coreConfiguration($stateProvider) {
    $stateProvider
        .state('core', {
            url: "/",
            templateUrl: "app/modules/core/view.core.html",
            controller: 'CoreCtrl'
        })
    
}