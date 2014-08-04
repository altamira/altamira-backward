'use strict';

/**
 * @ngdoc function
 * @name bpmpurchaserequeststeelApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bpmpurchaserequeststeelApp
 */
angular.module('1820e33145e64965a1432bda5b86f405')
  .controller('RequestCtrl', function ($scope, Restangular, Request) {
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
                _.map(group, function(o, x) { return o.weight; }), 
                function(t, v) { return t + v; }, 0)
        }})
      .value();

      $scope.total = _.reduce($scope.groups, function(t, g) { console.log(g.total); return t + g.total; }, 0);

      // console.log('total: ' + $scope.total);
      // console.log('groups: ' + $scope.groups);

      // Configura o subtítulo a ser usado na página.
      //$rootScope.subtitle = 'Requisição de Compra de Aço #' + $scope.request.id;

      // Correção no caso de um array null ao invés de vazio.
      /*if (_.isNull($scope.request.items)) {
        $scope.request.requestItem = [];
      }*/
    });
  };
  $scope.getCurrent(); 
                          
});
