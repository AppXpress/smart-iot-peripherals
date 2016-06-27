/*
 * Core router
 * Basic router configurations for the application
 */

angular.module('app.route').config(homeConfiguration);

homeConfiguration.$inject = ['$stateProvider'];

function homeConfiguration($stateProvider) {
    $stateProvider
    // setup an abstract state for the tabs directive
        .state('tab', {
        url: '/tab',
        abstract: true,
        templateUrl: 'app/modules/home/view.tabs.html'
    })

    // Each tab has its own nav history stack:

    .state('tab.dash', {
        url: '/dash',
        views: {
            'tab-dash': {
                templateUrl: 'app/modules/home/view.dash.html',
                controller: 'DashCtrl'
            }
        }
    })

    .state('tab.chats', {
            url: '/chats',
            views: {
                'tab-chats': {
                    templateUrl: 'app/modules/home/view.chats.html',
                    controller: 'ChatsCtrl'
                }
            }
        })
//        .state('tab.test', {
//            url: '/test',
//            views: {
//                'tab-test': {
//                    templateUrl: 'app/modules/home/view.test.html',
//                    controller: 'TestCtrl'
//                }
//            }
//        })
        .state('tab.test', {
            url: '/test',
            views: {
                'tab-test': {
                    templateUrl: 'app/modules/home/view.tasks.html',
                    controller: 'TestCtrl'
                }
            }
        })
        // .state('tab.chat-detail', {
        //     url: '/chats/:chatId',
        //     views: {
        //         'tab-chats': {
        //             templateUrl: 'app/modules/home/view.detail.html',
        //             controller: 'ChatDetailCtrl'
        //         }
        //     }
        // })
    .state('tab.account', {
        url: '/account',
        views: {
            'tab-account': {
                templateUrl: 'app/modules/home/view.account.html',
                controller: 'AccountCtrl'
            }
        }
    });

}