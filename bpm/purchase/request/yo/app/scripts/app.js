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
    'restangular',
    'mobile-angular-ui'
  ])
  .config(['$routeProvider', '$httpProvider', 'RestangularProvider', function ($routeProvider, $httpProvider, RestangularProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/new', {
        templateUrl: 'views/edit.html',
        controller: 'EditCtrl',
        resolve: {
          item: function() {
            var arrival = new Date();
            arrival.setHours(0,0,0,0);
            return {'id': 0, 'weight': 100.00, 'arrival': arrival.getTime(), 'material': {'id': 0, 'lamination': 'FQ', 'treatment': 'PR', 'thickness': 2.00, 'width': 240.00, 'length': 0.00}};
          }
        }
      })
      .when('/edit/:id', {
        templateUrl: 'views/edit.html',
        controller: 'EditCtrl',
        resolve: {
                    item: ['Restangular', '$route', function (Restangular, $route) {
                        return Restangular.one('request', 0).one('item', $route.current.params.id).get();
                    }]}
      })
      /*.when('/tabtest', {
        templateUrl: 'views/tabtest.html',
        controller: 'TabTestCtrl',
        resolve: {
                    item: function (Restangular, $route) {
                        return Restangular.one('request', 0).one('item', $route.current.params.id).get();
                    }}
      })*/
      .otherwise({
        redirectTo: '/'
      });

    RestangularProvider.setBaseUrl('/bpm/purchase/request/rest');
  }]);
