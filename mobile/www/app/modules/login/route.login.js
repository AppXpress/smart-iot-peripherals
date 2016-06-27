/*
 * Login router
 * Login route configurations for the application
 */

angular.module('app.route').config(loginConfiguration);

loginConfiguration.$inject = ['$stateProvider'];

function loginConfiguration($stateProvider) {
    $stateProvider
        .state('login', {
            url: "/login",
            templateUrl: "app/modules/login/view.login.html",
            controller: 'loginController'
        })
    
}