(function() {
	var app = angular.module('edit-module', []);
	
	
	app.controller('EditController', function($scope, $http, $log, $location, userService){
		
		$scope.success = false;
		$scope.employee = {};
		$scope.eir = {};
		$scope.change = false;
		$scope.submitted = false;
		
		$log.log("userServiceid "+ userService.getId() );
		
		$scope.eir._id = userService.getId();
		$scope.eir.accessToken = userService.getAccessToken();
		
		
		$scope.fillInfo = function(){
			$log.log($scope.eir);
			$scope.submitted = false;
			$http.post("/CookPayroll/api/v1/find_employee",$scope.eir).success(function(data){
				$log.log(data);
				$scope.employee = data;
			}).error(function(data){
			});
		};
		
		$scope.editInfo = function(){
			if($scope.change === true && $scope.pw1 === $scope.pw2) {
				$scope.employee.password = $scope.pw1;
			}
			$log.log($scope.employee);
			$scope.employee.accessToken = userService.getAccessToken();
			$scope.employee.dob = $scope.employee.dob.toString();
			$scope.employee._id = userService.getId();
			$http.put("/CookPayroll/api/v1/update_employee", $scope.employee).success(
			function(data) {
				$log.log(data);
				$scope.success = true;
				$scope.submitted = true;
				$scope.startTimer;
				$scope.submitted = false;
				}).error(function(data){
					$scope.error = true;
				});
		};
		
		$scope.pCheck = function(){
			return $scope.pw1 !== $scope.pw2;
		};
		
		$scope.startTimer = function () {
		    timer.start();
		    setTimeout($scope.stopTimer,3000);
		}

		$scope.stopTimer = function () {
		    timer.stop();
		}
		
	});
	
//	app.directive('passwordMatch', [function () {
//	    return {
//	        restrict: 'A',
//	        scope:true,
//	        require: 'ngModel',
//	        link: function (scope, elem , attrs, control) {
//	            var checker = function () {
//	 
//	                var e1 = scope.$eval(attrs.ngModel); 
//	                var e2 = scope.$eval(attrs.passwordMatch);
//	                return e1 == e2;
//	            };
//	            scope.$watch(checker, function (n) {
//	                control.$setValidity("unique", n);
//	            });
//	        }
//	    };
//	}]);
	
})();