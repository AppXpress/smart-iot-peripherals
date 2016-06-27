/*
 * Login controller
 * controlls the all the actions in login page
 */

angular.module('app.login').controller('loginController', loginController);

loginController.$inject = ['$rootScope', '$scope', '$state','$menuService'];

function loginController($rootScope, $scope, $state,$menuService) {
	$scope.login = function(){
		$state.go('tab.dash');
	}
	$menuService.initialization();
    
}
