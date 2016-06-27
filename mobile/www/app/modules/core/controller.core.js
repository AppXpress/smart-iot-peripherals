/**
 *  App Core controller 
 *
 **/

angular.module('app.controllers').controller('CoreCtrl', coreController);
coreController.$inject = ['$scope', '$rootScope', '$menuService'];

function coreController($scope, $rootScope, $menuService) {


    $rootScope.closeSideMenu = function(state, url) {
        $menuService.closeSideMenu(state, url);
    }
    
    $rootScope.openSideMenu = function() {
        $menuService.openSideMenu();
    }


    // logout 
    $rootScope.logout = function() {
        $rootScope.slideLeft.close();
        $state.go('login');
    }
}


//~~~~~~~~~~ TEST Controllers ~~~~~~~~~~~~
angular.module('app.controllers').controller('DashCtrl', dashController);
dashController.$inject = ['$scope'];

angular.module('app.controllers').controller('ChatsCtrl', chatController);
chatController.$inject = ['$scope', 'Chats'];

angular.module('app.controllers').controller('ChatsCtrl', chatController);
chatController.$inject = ['$scope', 'Chats'];

angular.module('app.controllers').controller('ChatDetailCtrl', chatDetailController);
chatDetailController.$inject = ['$scope', 'Chats', '$stateParams'];

angular.module('app.controllers').controller('TestCtrl', testController);
testController.$inject = ['$scope', 'Chats', '$stateParams'];

angular.module('app.controllers').controller('TestCtrl', testController);
testController.$inject = ['$scope', '$stateParams', 'Chats'];

angular.module('app.controllers').controller('AppCtrl', appController);
appController.$inject = ['$scope'];

angular.module('app.controllers').controller('SettingsCtrl', settingsController);
settingsController.$inject = ['$scope'];

angular.module('app.controllers').controller('AccountCtrl', accountController);
settingsController.$inject = ['$scope'];

function dashController($scope) {}

function chatController($scope, Chats) {
    $scope.chats = Chats.all();
    $scope.remove = function(chat) {
        Chats.remove(chat);
    };
}

function chatDetailController($scope, Chats, $stateParams) {}

function testController($scope, $stateParams, Chats) {
    $scope.chats = Chats.all();
}

function appController($scope) {}

function settingsController($scope) {}

function accountController($scope) {}

angular.module('app.controllers').factory('Chats', function() {
    // Might use a resource here that returns a JSON array

    // Some fake testing data
    var chats = [{
        id: 0,
        name: 'Which country from the following is NOT the member of UNO?',
        lastText: '2015-11-25 : 2015-12-25',
        face: 'img/ben.png'
    }, {
        id: 1,
        name: 'Max Lynx',
        lastText: 'Hey, it\'s me',
        face: 'img/max.png'
    }, {
        id: 2,
        name: 'Adam Bradleyson',
        lastText: 'I should buy a boat',
        face: 'img/adam.jpg'
    }, {
        id: 3,
        name: 'Perry Governor',
        lastText: 'Look at my mukluks!',
        face: 'img/perry.png'
    }, {
        id: 4,
        name: 'Mike Harrington',
        lastText: 'This is wicked good ice cream.',
        face: 'img/mike.png'
    }, {
        id: 1,
        name: 'Max Lynx',
        lastText: 'Hey, it\'s me',
        face: 'img/max.png'
    }, {
        id: 2,
        name: 'Adam Bradleyson',
        lastText: 'I should buy a boat',
        face: 'img/adam.jpg'
    }, {
        id: 3,
        name: 'Perry Governor',
        lastText: 'Look at my mukluks!',
        face: 'img/perry.png'
    }, {
        id: 1,
        name: 'Max Lynx',
        lastText: 'Hey, it\'s me',
        face: 'img/max.png'
    }, {
        id: 2,
        name: 'Adam Bradleyson',
        lastText: 'I should buy a boat',
        face: 'img/adam.jpg'
    }, {
        id: 3,
        name: 'Perry Governor',
        lastText: 'Look at my mukluks!',
        face: 'img/perry.png'
    }, {
        id: 1,
        name: 'Max Lynx',
        lastText: 'Hey, it\'s me',
        face: 'img/max.png'
    }, {
        id: 2,
        name: 'Adam Bradleyson',
        lastText: 'I should buy a boat',
        face: 'img/adam.jpg'
    }, {
        id: 3,
        name: 'Perry Governor',
        lastText: 'Look at my mukluks!',
        face: 'img/perry.png'
    }, {
        id: 1,
        name: 'Max Lynx',
        lastText: 'Hey, it\'s me',
        face: 'img/max.png'
    }, {
        id: 2,
        name: 'Adam Bradleyson',
        lastText: 'I should buy a boat',
        face: 'img/adam.jpg'
    }, {
        id: 3,
        name: 'Perry Governor',
        lastText: 'Look at my mukluks!',
        face: 'img/perry.png'
    }];

    return {
        all: function() {
            return chats;
        },
        remove: function(chat) {
            chats.splice(chats.indexOf(chat), 1);
        },
        get: function(chatId) {
            for (var i = 0; i < chats.length; i++) {
                if (chats[i].id === parseInt(chatId)) {
                    return chats[i];
                }
            }
            return null;
        }
    };
});
