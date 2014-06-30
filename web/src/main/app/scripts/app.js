'use strict';

angular
  .module('webApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ui.bootstrap',
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
  .config(function ($routeProvider, $httpProvider, RestangularProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/task', {
        templateUrl: 'views/task.html',
        controller: 'TaskCtrl'
      })
      .when('/request', {
        templateUrl: 'views/request.html',
        controller: 'RequestCtrl'
      })
      .when('/quotation', {
        templateUrl: 'views/quotation.html',
        controller: 'QuotationCtrl'
      })
      .when('/purchaseplanning', {
        templateUrl: 'views/purchaseplanning.html',
        controller: 'PurchaseplanningCtrl'
      })
      .when('/purchaseorder', {
        templateUrl: 'views/purchaseorder.html',
        controller: 'PurchaseorderCtrl'
      })
      .when('/request-edit', {
        templateUrl: 'request.html',
        controller: 'EditRequestCtrl'
      })
      .when('/requestItem', {
        templateUrl: 'views/requestItem.html',
        controller: 'RequestItemCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });

    // Adiciona um interceptor responsável pelo tratamento das respostas http.
    var httpInterceptor = function ($q, $rootScope, $log) {
      return {
        request: function(config) {

          $rootScope.$broadcast('http:start');

          return config || $q.when(config);
        },
        response: function (resp) {
          $rootScope.$broadcast('http:stop');

          /*if (resp.data.error) {
            $log.error('Detectado um erro no servidor: ' + resp.data.error + '. Message: ' + resp.data.message);

            $rootScope.$broadcast('server:error', { code : resp.data.error, message : resp.data.message });

            return $q.reject(resp);
          }*/

          return resp || $q.when(resp);
        },
        responseError : function(rejection) {
          $rootScope.$broadcast('http:stop');

          switch(rejection.status) {
            case 401:
              $log.error('Detectada a necessidade de autenticação');

              $rootScope.$broadcast('auth:loginRequired', { origin : 'Falha de autenticação.', code : rejection.status, message : 'É necessário autenticação para acessar este recurso.', url : rejection.config.url });
              break;
            case 403:
              $log.error('Detectado o acesso a um recurso sem autorização');

              $rootScope.$broadcast('auth:forbidden', { origin : 'Acesso negado.', code : rejection.status, message : 'Você não tem autorização para acessar o recurso.', url : rejection.config.url });
              break;
            case 404:
              $log.error('Detectado o acesso a um recurso não encontrado no servidor');

              $rootScope.$broadcast('page:notFound', { origin : 'Página ou recurso não encontrado.', code : rejection.status, message : 'A página ou recurso não foi encontrada.', url : rejection.config.url });
              break;
            case 500:
              $log.error('Detectado um erro interno no servidor');

              $rootScope.$broadcast('server:error', { origin : 'Erro interno no servidor.', code : rejection.status, message : rejection.data, url : rejection.config.url });
              break;
            default:
              $log.error('Detectado um erro de conexão com o servidor: Status: ' + rejection.status + '. URL: ' + rejection.config.url);

              $rootScope.$broadcast('connection:error', { origin : 'Falha de comunicação.', code : rejection.status, message : 'Ocorreu um erro durante a chamada de um serviço.', url : rejection.config.url});
          }

          return $q.reject(rejection);
        }
      };
    };
    $httpProvider.interceptors.push(httpInterceptor);

    // Configurações do CORS.
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];

    // Configuração do framework Restangular.
    RestangularProvider.setBaseUrl('http://localhost:8080/altamira-bpm/rest');

  });
