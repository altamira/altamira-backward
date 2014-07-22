'use strict';

/**
 * @ngdoc function
 * @name quotationApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the quotationApp
 */
angular.module('quotationApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
