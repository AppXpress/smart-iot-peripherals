/**
 *  Menu Service
 *  @author: Sumali Jayasinghe
 *  Service to control the application side menu 
 */
angular.module('app.services')
    .factory('$menuService', menuService);


menuService.$inject = ['$rootScope', '$state'];

function menuService($rootScope, $state) {


    var initialization = function() {

        $rootScope.menulist = [{
            name: 'Settings',
            icon: 'ion-ios-gear-outline ion-android-settings',
            goTo: 'settings',
            url: ''
        }, {
            name: 'Help',
            icon: 'ion-help-circled ion-ios-help-outline ', //ion-information-circled ion-ios-help-outline
            goTo: '',
            url: 'http://appxpresslabs.com/swift-help/'
        }, {
            name: 'Terms and Conditions',
            icon: 'ion-ios-locked-outline',
            goTo: '',
            url: 'https://developer.gtnexus.com/terms'
        }, {
            name: 'Feedback',
            icon: 'ion-ios-star-outline ion-android-star-outline',
            goTo: '',
            url: 'http://appxpresslabs.com/portfolio/swift/#reply-title'
        }, {
            name: "Thank-You's",
            icon: 'ion-ios-heart-outline ion-android-favorite-outline',
            goTo: '',
            url: 'http://appxpresslabs.com/swift-thank-yous/'
        }];

        // initialize the side menu
        $rootScope.slideLeft = new Menu({
            wrapper: '#o-wrapper',
            type: 'slide-left',
            menuOpenerClass: '.swift-button',
            maskId: '#swift-mask',
            navToggle: "#nav-toggle"
        });
    };


    /*
     * show error msg
     * usage: $popupService.showErrorMessage();
     */

    var openSideMenu = function() {
        $rootScope.slideLeft.open();

    };

    /*
     * show success msg
     * usage: $popupService
.showSuccessMessage();
     */

    var closeSideMenu = function(state, url) {
        $rootScope.slideLeft.close();
        if (state == "settings") {
            $state.go(state);
        } else {
            window.open(url, '_blank', 'location=no');
            return false;
        }

    };


    return {
        initialization: initialization,
        openSideMenu: openSideMenu,
        closeSideMenu: closeSideMenu
    }

}
