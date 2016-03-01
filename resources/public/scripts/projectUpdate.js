(function(){
	var app = angular.module('projectUpdate',[])
	
	app.controller('ProjectUpdateController',function($scope,$http,$log,$location,userService){
		
		
		$scope.feilds = ['Name', 'ProjectName', 'Project End Date', 'Role', 'Location', 'Project Manager', 'AMEX  Tech Lead Name',
		                 'What do you know about your current project /Assignment',	'Current Assignment Details	What is the Progress ?',
		                 'Projected Completion Date','Are there any  challenges ?',	'Any Other Comments	Rally Tasks	Proposal/New Initiatives'];
		
	});  
	
})();