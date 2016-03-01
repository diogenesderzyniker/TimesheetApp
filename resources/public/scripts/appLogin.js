(function() {
	var app = angular.module('login-module', []);
	
	

	app.controller('LoginController', function($scope, $http, $log,$location, userService) {
		
		$log.log("userserviceid "+userService.getId());
		
		$scope.error = false;
		
		
		$scope.credentials = {};
		$scope.newUser = {};

		$scope.login = function() {
			$log.log($scope.credentials);
//			$.ajax({
//				type: "POST",
//				url: "http://192.168.1.14:8181/CookPayroll/api/v1/login",
//				data: $scope.credentials
//			}).done(function(data,status,headers,config) {
//				$log.log( data );						
//				if(data.loginSuccess === true){							
//				$location.path('/home');
//				userService.setId(data.employeeId);
//				userService.setAccessToken(data.accessToken);
//				userService.setLoggedIn(true);
//				}
//			}).error(function(data){
//					$log.log("Error: " +data);
//					$scope.showHelp("Error: " + data);
//					$scope.error = true;
//				});
//			};
			$http.post("/CookPayroll/api/v1/login", $scope.credentials).success(
					function(data,status,headers,config) {
						$log.log( data );						
						if(data.loginSuccess === true){							
						$location.path('/home');
						userService.setId(data.employeeId);
						userService.setAccessToken(data.accessToken);
						userService.setLoggedIn(true);
						userService.setRole(data.role);
						}
											
					}).error(function(data){
						$log.log("Error: " +data);		
						$scope.error = true;
					});
		};
		
		$scope.register = function() {
			$log.log($scope.newUser);
			$http.post("/CookPayroll/api/v1/register", $scope.newUser).success(
					function(data) {
						$log.log("register" +data);
						if (data === "true") {
							$log.log("data was true")
							$location.path('/login');
						}
					}).error(function(data){
						$scope.error = true;
					});
		};
	});

})();