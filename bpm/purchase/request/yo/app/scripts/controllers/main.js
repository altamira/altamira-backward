'use strict';

/**
 * @ngdoc function
 * @name bpmpurchaserequeststeelApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bpmpurchaserequeststeelApp
 */
angular.module('1820e33145e64965a1432bda5b86f405')
  .controller('MainCtrl', function ($scope, Restangular, Request) {
	$scope.awesomeThings = [ 'HTML5 Boilerplate', 'AngularJS', 'Karma' ];

  $scope.getCurrent = function () {
    Request.getCurrent().then(function(request) {

      $scope.request = request;

      $scope.groups = _.chain(request.items)
        .sortBy('arrival')
        .groupBy('arrival')
        .map(function(group, key) {
          return {
              arrival: key,
              items: group,
              total: _.reduce(
                  _.map(group, function(o) { return o.weight; }), 
                  function(t, v) { return t + v; }, 0)};
        }).value();

      $scope.total = _.reduce($scope.groups, function(t, g) { return t + g.total; }, 0);

    });
  };
  $scope.getCurrent(); 
                          
});
