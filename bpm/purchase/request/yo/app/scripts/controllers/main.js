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

      $scope.groups = _.groupBy(request.items, 'arrival');

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
