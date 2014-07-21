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
  .module('steelApp', [
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
        controller: 'RequestCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
