/**
 *  App menu directive 
 *  @author: Sumali Jayasinghe
 *
 **/
 

 angular.module('app.directives')
    .directive('menu', function($compile, $http) {
        return {
            restrict: 'E',
            scope: {
                menulist: '=',
                side: '@',
                direct: '&',
                logout: '&'

            },
            templateUrl: 'app/modules/menu/view.menu.html'
        }
    })
