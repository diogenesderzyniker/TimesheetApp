(function() {
	var app = angular.module('time-sheet', [])	
	
	app.controller('WorkSheetController', function($scope,$http,$log,$location,userService,navHelp){
		
		if(!userService.isLoggedIn()){
			$location.path("/login");
		}
		
		
		
		$log.log("userService: "+ userService.getId() );
		
		$scope.months = ["January","February","March","April","May","June","July","August","September","October","November","December"];
		$scope.dayString = ["Su","Mo","Tu","We","Th","Fr","Sa"];
		$scope.years = [2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025];
		
		$scope.half = [1,2];
		$scope.halfString = ["1st half", "2nd half"];
		
		$scope.quickFill = {};		
		$scope.quickFill.hours = 8;
		$scope.quickFill.hoursType = "Billable";	
		
		$scope.tsr = {};
		$scope.timesheetPostRequest= {};
		$scope.success = false;
		
		$scope.timesheet = {};
		$scope.timesheet.cycleId = -1;
		$scope.timesheet.employee = userService.getId();
		$scope.timesheet.days = [];
		$scope.timesheet.accessToken = userService.getAccessToken();
		
		$scope.tsr.year = 0;
		$scope.tsr.month= 0;
		$scope.tsr.half = 0;
		
		if(navHelp.isDirty()){
			$log.log("the year was set to: " + $scope.tsr.year);
			$scope.tsr.year = navHelp.getYear();
			$scope.tsr.month = navHelp.getMonth();
			$scope.tsr.half = navHelp.getHalf();
			navHelp.setDirty(false);
		}else{
			
			var d = new Date();
			$scope.tsr.year = d.getFullYear();
			$scope.tsr.month = d.getMonth() ;
			if(d.getDate() > 15){
				$scope.tsr.half = 2;
			}else{
				$scope.tsr.half = 1;
			}
		}
		// set up the time sheet to send a request for the current Date
		$scope.tsr.employeeId = userService.getId();
		$scope.tsr.accessToken = userService.getAccessToken();
				
		$scope.show = true;
		$scope.days = [];
		
		$scope.workCycle = {};
		
		$scope.fillWorkSheet = function(){
			$log.log("quick filling worksheet" );
			$scope.success = false;
			
			var i = 0;
			
			var date = new Date();
			
			for( i; i < $scope.timesheet.days.length; i++){
				date.setFullYear($scope.timesheet.days[i].year, $scope.timesheet.days[i].month, $scope.timesheet.days[i].day)
				//$log.log("year:"+$scope.days[i].year+ " month: "+ $scope.days[i].month +" day "+ $scope.days[i].day +" day of week: "+ date.getDay() )
				//$log.log(date)
				if(!(date.getDay() === 0 || date.getDay() === 6)){				
				$scope.timesheet.days[i].entries[0].hours = $scope.quickFill.hours;
				$scope.timesheet.days[i].entries[0].hoursType = $scope.quickFill.hoursType;
				$scope.timesheet.days[i].entries[0].clientName = $scope.quickFill.clientName;
				$scope.timesheet.days[i].entries[0].projectName = $scope.quickFill.projectName;
				$scope.timesheet.days[i].entries[0].comments = $scope.quickFill.comments;
				}
			}
		};
		
		$scope.getDayString = function(year,month,day){
			var d = new Date();
			d.setFullYear(year,month,day);
			var dayString = $scope.months[d.getMonth()]; 
			dayString += " " + d.getDate();
			dayString += " " + $scope.dayString[d.getDay()];
			
			return dayString;
		};
		
		
		$scope.save = function(){
			$scope.workCycle = $scope.days;
			$scope.timesheetPostRequest.accessToken = userService.getAccessToken();
			$scope.timesheetPostRequest.timesheet= $scope.timesheet;
			//$http.post("/api/v1/entries",$scope.workCycle);
			          
			$http.put("/CookPayroll/api/v1/timesheet",$scope.timesheet).success(function(data){
				//$location.path("/success");
				$scope.success = true;
				
			});
		};
		
		$scope.addEntry = function(day){
			day.entries.push(new entry());
			$scope.success = false;
			
		};
		$scope.removeEntry = function(day){
			if(day.entries.length > 1){				
				day.entries.pop()
			}
			$scope.success = false;
		};
	
		function entry(){
			this.hours = null;
			this.hoursType = null;
			this.clientName = "";
			this.projectName = "";
			this.comments = "";
		};
		
		function day(date){
			this.date = date;
			this.day = date.getDate();
			this.month = date.getMonth();
			this.year = date.getFullYear();
			this.entries = [];
		}
		
		$scope.requestTimeSheet = function(){
			$log.log($scope.tsr);
			$scope.success = false;
			
			if($scope.tsr.half === "1"){
				$scope.tsr.half = 1;
			}
			if($scope.tsr.half === "2"){
				$scope.tsr.half = 2;
			}
			$http.post("/CookPayroll/api/v1/timesheet",$scope.tsr).success(function(data){
					$log.log(data);
					$scope.timesheet= data;
					$scope.timesheet.accessToken = userService.getAccessToken();
			}).error(function(data){
				//$scope.generateTimeSheet();
			});
		};
		       
		
		
	
		
		
		$scope.requestTimeSheet();
		
	});

})();