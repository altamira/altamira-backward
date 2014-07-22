'use strict';

/**
 * @ngdoc overview
 * @name yoApp
 * @description
 * # yoApp
 *
 * Main module of the application.
 */
angular
  .module('dataApp', [
    'ngRoute',
    'restangular'
  ])
  .run(function($rootScope, $location) {
    $rootScope.bgcolor = 'white';
    $rootScope.fgcolor = 'black';
    $rootScope.page = { title : 'Altamira Industria Metalurgica' };

    var history = [];

    $rootScope.$on('$routeChangeSuccess', function() {
        history.push($location.$$path);
    });

    $rootScope.back = function () {
        var prevUrl = history.length > 1 ? history.splice(-2)[0] : '/';
        $location.path(prevUrl);
    };
  })
  .config(function ($routeProvider/*, $httpProvider, RestangularProvider*/) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
