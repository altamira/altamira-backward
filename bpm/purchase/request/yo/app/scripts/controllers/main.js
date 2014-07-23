'use strict';

/**
 * @ngdoc function
 * @name bpmpurchaserequeststeelApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bpmpurchaserequeststeelApp
 */
angular.module('BpmPurchaseRequestApp')
  .controller('RequestCtrl', function ($scope) {
	$scope.awesomeThings = [ 'HTML5 Boilerplate', 'AngularJS', 'Karma' ];
    
    $scope.request = { items : [
                                { material: { format: 0, lamination: 'FQ', treatment: 'PR', thickness: '1.85', width: 240, length: 1400}, weight: 1260, arrival: '01/01/2014'},
                                { material: { format: 1, lamination: 'FF', treatment: 'DE', thickness: '2.4', width: 100, length: 0}, weight: 3460, arrival: '01/01/2014'},
                                { material: { format: 1, lamination: 'FF', treatment: 'GA', thickness: '1.65', width: 100, length: 0}, weight: 2280, arrival: '08/01/2014'},
                                { material: { format: 1, lamination: 'FQ', treatment: 'PR', thickness: '0.8', width: 220, length: 0}, weight: 1660, arrival: '08/01/2014'},
                                { material: { format: 0, lamination: 'FF', treatment: 'PR', thickness: '1.8', width: 260, length: 0}, weight: 260, arrival: '07/01/2014'},
                                { material: { format: 1, lamination: 'FF', treatment: 'PR', thickness: '2.0', width: 180, length: 1200}, weight: 5260, arrival: '07/01/2014'},
                                { material: { format: 1, lamination: 'FQ', treatment: 'GA', thickness: '2.4', width: 160, length: 1000}, weight: 160, arrival: '05/01/2014'},
                                { material: { format: 0, lamination: 'FF', treatment: 'PR', thickness: '2.0', width: 80, length: 0}, weight: 860, arrival: '05/01/2014'}]};
  });
