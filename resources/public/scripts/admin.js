(function(){
	var app = angular.module('admin',[])
	
	app.controller('AdminController',function($scope,$http,$log,$location,userService){
		if(!userService.isLoggedIn()){
			$log.log(!userService.isLoggedIn());
			$location.path("/login");
		}
		if(userService.getRole() !== "admin"){
			$log.log("Not an admin. Go away.");
			alert("Please mind your own business, " + userService.getRole());
			$location.path("/home");
		}
				
		$log.log("userService: "+ userService.getId() );
		
		$scope.months = ["January","February","March","April","May","June","July","August","September","October","November","December"];
		$scope.dayString = ["Su","Mo","Tu","We","Th","Fr","Sa"];
		$scope.years = [2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025];
		
		$scope.half = [1,2];
		$scope.halfString = ["1st half", "2nd half"];
		
		$scope.tsr = {};
		$scope.timesheetPostRequest= {};
		
		$scope.employeeList = [];
		$scope.selected = 1;
		$scope.maxsize = 10;
		$scope.totalItems =200;
		
		$scope.employeeUsername = "";
		$scope.billableHours = 0;
		$scope.nonBillableHours = 0;
		$scope.ptoHours = 0;
		
		$scope.timesheet = {};
		$scope.timesheet.cycleId = 7;
		$scope.timesheet.employee = userService.getId();
		$scope.timesheet.days = [];
		$scope.searchText = "";
		
		$scope.timesheetApproval = {};
		$scope.comment = "";
		
		$scope.approved = false;
		$scope.denied = false;
		$scope.error = false;
		
		// set up the time sheet to send a request for the current Date
		var d = new Date();
		
		$scope.tsr.year = d.getFullYear();
		$scope.tsr.month = d.getMonth() ;
		//$log.log("the date is" +d.getDate());
		if(d.getDate() > 15){
			$scope.tsr.half = 2;
		}else{
			$scope.tsr.half = 1;
		}
		$scope.tsr.employeeId = userService.getId();
		$scope.tsr.accessToken = userService.getAccessToken();
		
		$scope.getDayString = function(year,month,day){
			var d = new Date();
			d.setFullYear(year,month,day);
			var dayString = $scope.months[d.getMonth()]; 
			dayString += " " + d.getDate();
			dayString += " " + $scope.dayString[d.getDay()];
			
			return dayString;
		};
		
		
		$scope.requestTimeSheet = function(){
			$log.log($scope.tsr);
			
			if($scope.tsr.half === "1"){
				$scope.tsr.half = 1;
			}
			if($scope.tsr.half === "2"){
				$scope.tsr.half = 2;
			}
			$scope.tsr.cycleNum = $scope.timesheet.cycleNum;
			$http.post("/CookPayroll/api/v1/timesheet",$scope.tsr).success(function(data){
					$log.log(data);
					$scope.timesheet= data;
			}).error(function(data){
				//$scope.generateTimeSheet();
			});
		};
		
		$scope.requestEmployeeList = function(){
			$log.log(userService.getAccessToken());
			$http.post("/CookPayroll/api/v1/all_employees",{accessToken: userService.getAccessToken()}).success(function(data){
				$log.log(data);
				$scope.employeeList = data;
				
			});
		};
		
		$scope.entryFilter = function(day){
			if(day.entries[0].hours === 0){
				return false
			}
			return true;
		}
		
		$scope.filterFunction = function(employee){			
			var username = employee.username.toLowerCase();
			var searchtext = $scope.searchText.toLowerCase();
			
			if(username.indexOf(searchtext) > -1 ){
				return true;
			}
			return false;
			
		};
		
		
		
		
		$scope.getTimesheet = function(employee){
			$log.log("employee id" +employee._id);
			$scope.tsr.employeeId = employee._id;
			//$scope.tsr.employeeUsername = employee.username;
			
			
			$http.post("/CookPayroll/api/v1/timesheet",$scope.tsr).success(function(data){
				$log.log(data);
				$scope.timesheet = data;
				$scope.billableHours = 0;
				$scope.nonBillableHours = 0;
				$scope.ptoHours = 0;
				
				var i = 0;
				for(i = 0; i < data.days.length; i ++){
					var y = 0;
					for(y = 0; y < data.days[i].entries.length; y ++){
						if(data.days[i].entries[y].hoursType == "Billable"){
							$scope.billableHours += data.days[i].entries[y].hours;
						}
						if(data.days[i].entries[y].hoursType == "nonBillable"){
							$scope.nonBillableHours += data.days[i].entries[y].hours;
						}
						if(data.days[i].entries[y].hoursType == "PTO"){
							$scope.ptoHours += data.days[i].entries[y].hours;
						}
					}
				}
			});
		};
		
		$scope.acceptTimesheet = function(){
			$scope.timesheet.validatorId = userService.getId();
			$scope.timesheet.status = "accepted";
			$scope.timesheet.comment = $scope.comment;
			$scope.timesheet.validatorId = userService.getId();
			$scope.timesheet.accessToken = userService.getAccessToken();
			$scope.timesheetPostRequest.timesheet = $scope.timesheet;
			$scope.timesheetPostRequest.accessToken = userService.getAccessToken();
			$http.post("/CookPayroll/api/v1/timesheet_approval",$scope.timesheet).success(function(data){
				$log.log("timesheet_approval approval success");
				$scope.approved = true;
				$scope.denied = false;
				$scope.error = false;
			}).error(function(data){
				$scope.error = true;
				$scope.approved = false;
				$scope.denied = false;
			});		
		};
		
		$scope.denyTimesheet = function(){
			$scope.timesheet.validatorId = userService.getId();
			$scope.timesheet.status = "denied";
			$scope.timesheet.comment = $scope.comment;
			$scope.timesheet.validatorId = userService.getId();
			$scope.timesheet.accessToken = userService.getAccessToken();
			$scope.timesheetPostRequest.timesheet = $scope.timesheet;
			$scope.timesheetPostRequest.accessToken = userService.getAccessToken();
			$http.post("/CookPayroll/api/v1/timesheet_approval",$scope.timesheet).success(function(data){
				$log.log("timesheet_approval denied success");
				$scope.denied = true;
				$scope.approved = false;
				$scope.error = false;
			}).error(function(data){
				$scope.error = true;
				$scope.approved = false;
				$scope.denied = false;
			});		
		};
		
		
		
		$scope.requestEmployeeList();
		
		//$scope.requestTimeSheet();
		
	});  
	
})();