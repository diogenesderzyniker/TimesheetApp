(function() {
	var app = angular.module('history-module', []);
	
	

	app.controller('HistoryController', function($scope, $http,$location, $log, userService,navHelp) {
		
		if(!userService.isLoggedIn()){
			//$location.path("/login");
		}
		
		$log.log("userserviceid "+userService.getId());
		
		$scope.timesheets = ["timesheet 1", "timesheet 2", "timesheet3"];
		$scope.tsr = {};
		$scope.tsr.accessToken = userService.getAccessToken();
		$scope.tsr.employeeId = userService.getId();
		
		$scope.getUserTimesheets = function(){
		$http.post("/CookPayroll/api/v1/history",$scope.tsr).success(function(data){
				$log.log(data);
				$scope.timesheets = data;
			});
		};
		
		$scope.goToTimesheet = function(cycleNum){
			$log.log("going to cycle"+cycleNum)
			navHelp.setDirty(true);
			navHelp.setCycleNum(cycleNum);
			
			$location.path("/shane");
		};
		
		$scope.getDateFromCycle = function(cycleNum){
			var month = parseInt(cycleNum % 24);
			month = parseInt(month / 2);		
			var year = parseInt(cycleNum / 24);
			year += 2015;
			var cycle = parseInt(cycleNum % 2);
			cycle += 1;
			return year + "/" + month + "/"+ cycle;
		}
		
		$scope.getUserTimesheets();
		
	});

})();