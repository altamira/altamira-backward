'use strict';

/**
 * @ngdoc function
 * @name quotationApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the quotationApp
 */
angular.module('quotationApp')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
