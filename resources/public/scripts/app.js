(function() {
var app = angular.module('CookTimeSheet', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ui.bootstrap',
    'time-sheet',
    'login-module',
    'admin',
    'edit-module',
    'history-module',
    'projectUpdate',
    'home-module'
]);


app.controller("NavigationBarController",function($scope,$http,$log,$location,userService){
	
	$scope.logout = function(){
		$http.post("/CookPayroll/api/v1/logout/"+userService.getAccessToken(),null).success(function(data){
			$log.log(data);
			userService.logout();
			$location.path("/login");

	}).error(function(data){
		$log.log(data);
		//$scope.generateTimeSheet();
	});
	}
	
});
app.service('navHelp',function(){
	this.dirty = false;
	
	this.cycleNum = 0;
	
	this.getMonth = function(){
		var month = parseInt(this.cycleNum % 24);
		month = parseInt(month / 2);
		return month;
	};
	this.getYear = function(){
		var year = parseInt(this.cycleNum / 24);
		year += 2015;
		return year;
	};
	this.getHalf = function(){
		var cycle = parseInt(this.cycleNum % 2);
		cycle += 1;
		return cycle;
	};
	
	this.isDirty = function(){
		return this.dirty;
	};
	this.setDirty = function(bool){
		this.dirty = bool;
	};
	
	this.getCycleNum = function(){
		return this.cycleNum;
	};
	this.setCycleNum = function(num){
		this.cycleNum = num;
	}
	
});

app.service('userService',function(){
	this.userData = {}
	this.userData.accessToken = "";
	this.userData.employeeId = "";
	this.userData.loggedIn = false;
	this.userData.role = "";
	
	this.isLoggedIn = function(){
		return this.userData.loggedIn;			
	}
	
	this.setLoggedIn = function(bool){
		this.userData.loggedIn = bool;
	}
	
	this.getId = function(){
		return this.userData.employeeId;
	}
	this.setId = function(id){
		this.userData.employeeId = id;
	}
	
	this.setAccessToken = function(key){
		this.userData.accessToken = key;
	}
	this.getAccessToken = function(){
		return this.userData.accessToken;
	}
	this.getRole = function(){
		return this.userData.role;
	}
	this.setRole = function(role){
		this.userData.role = role;
	}
	this.logout = function(){
		this.userData.accessToken = "";
		this.userData.employeeId = "";
		this.userData.loggedIn = false;
	}
	this.isAdmin = function(){
		if (this.userData.role === "admin"){
			return true;
		} else return false;
	}
});

 
app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/home.html'
    }).when('/cycles', {
        templateUrl: 'views/emptySheet.html',
        controller: 'CycleCtrl'
    }).when('/success', {
        templateUrl: 'views/success.html',        
    }).when('/shane', {
        templateUrl: 'views/shaneWorkSheet.html',
        controller: 'WorkSheetController'
    }).when('/login',{
    	templateUrl: 'views/login.html',
    	controller: 'LoginController'    	
    }).when('/profile',{
    	templateUrl: 'views/profile.html',
    	controller: 'EditController'
    }).when('/unknown',{
    	templateUrl: 'views/unknownUrl.html'
    }).when('/admin',{
    	templateUrl: 'views/admin.html',
    	controller: 'AdminController'
    }).when('/home',{
    	templateUrl: 'views/home.html',
    	controller: "HomeController"
    }).when('/updates',{
    	templateUrl: 'views/projectupdate.html',
    	controller: 'ProjectUpdateController'
    }).when('/history',{
    	templateUrl: 'views/history.html',
    	controller: 'HistoryController'
    		
    }).otherwise({
        redirectTo: '/unknown'
    })
});

})();