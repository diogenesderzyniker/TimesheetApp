(function() {
	var app = angular.module('home-module', []);
	
	app.controller('HomeController', function($scope, $http, $log,$location, userService) {
		$scope.show = function(){
			return userService.isLoggedIn();
		}
		$scope.showAdmin = function(){
			return userService.isAdmin();
		}
	});

})();