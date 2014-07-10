'use strict';

/**
 * @ngdoc overview
 * @name bpmpurchaserequeststeelApp
 * @description
 * # bpmpurchaserequeststeelApp
 *
 * Main module of the application.
 */
angular
  .module('BpmPurchaseRequestSteelApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
