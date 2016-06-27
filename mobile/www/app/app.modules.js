angular.module('app.services', ['ionic']),
angular.module('app.directives', ['ionic']),
angular.module('app.login', ['ionic']),
angular.module('app.route', ['ionic']),
angular.module('app.controllers', ['ionic', 'app.services','app.login']);