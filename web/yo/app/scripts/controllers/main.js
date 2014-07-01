'use strict';

/**
 * @ngdoc function
 * @name yoApp.controller:MainCtrl
 * @description # MainCtrl Controller of the yoApp
 */
angular.module('yoApp').controller('MainCtrl', function($scope, $http) {
	$scope.awesomeThings = [ 'HTML5 Boilerplate', 'AngularJS', 'Karma' ];
	$scope.serverSays = 'none';

	$http.get('./rest/members').success(function(data) {
		$scope.serverSays = data;
	}).error(function() {
		$scope.serverSays = 'error !';
	});

	/*$scope.$watch('serverSays', function(data) {
		$scope.serverSays = data;
	});*/

});
