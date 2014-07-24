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
  .module('1820e33145e64965a1432bda5b86f405', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'restangular'
  ])
  .config(function ($routeProvider, $httpProvider, RestangularProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'RequestCtrl'
      })
      .when('/edit', {
        templateUrl: 'views/edit.html',
        controller: 'EditCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });

    RestangularProvider.setBaseUrl('/data/rest');
  });

