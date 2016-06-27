/**
 *	AppXpress template for an appliacation
 *
 **/


angular.module('app', ['ionic', 'ngCordova', 'app.controllers', 'app.services','app.directives','app.route'])


.run(function ($ionicPlatform, $rootScope, $menuService) {
    $ionicPlatform.ready(function () {
        
        if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
            cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
            cordova.plugins.Keyboard.disableScroll(true);

        }
        
        if (window.StatusBar) {
            StatusBar.styleDefault();
        }

        // the drawer initialization
        // $rootScope.drawer = $nativeDrawer;
        // default options (all of them)
        // var options = {
        //         maxWidth: 300,
        //         speed: 0.2,
        //         animation: 'ease-out',
        //         topBarHeight: 56,
        //         modifyViewContent: true,
        //         useActionButton: true
        //     }
        // initialize with options
        // $rootScope.drawer.init(options);
        $menuService.initialization();
    });
})

.config(function ($stateProvider, $urlRouterProvider, $ionicConfigProvider) {

    $ionicConfigProvider.tabs.position('bottom');
    $ionicConfigProvider.spinner.icon('bubbles');

    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise('/login');

});