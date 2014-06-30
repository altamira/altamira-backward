'use strict';

var showErrorServerModalController = function ($scope, $modalInstance, title, error) {
  $scope.title = title;
  $scope.error = error;

  $scope.ok = function () {
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
};

angular.module('webApp')
  .controller('SystemCtrl', function ($scope, $cookieStore, $location, $modal, $log, $timeout, Auth) {
    var handleError = function(evt, error) {
      /*var modalInstance = $modal.open({
          templateUrl: 'showErrorServerModal.html',
          controller: showErrorServerModalController,
          resolve: {
            title : function () {
              return 'Erro na aplicação';
            },
            error : function () {
              return error;
            }
          }
        });

      modalInstance.result.then(function (request) {
        $log.debug(request);
      }, function () {
        $log.debug('Modal cancelada: ' + new Date());
      });*/

      $.Dialog({
          shadow: true,
          overlay: false,
          icon: '<span class="icon-warning"></span>',
          title: 'Código do Erro: ' + error.code,
          width: 100,
          height: 100,
          padding: 5,
          content: '<h4>' + error.origin + '</h4><p>' + error.message + '</p><p><strong><small>' + error.url + '</small></strong></p>'
      });

    };

    // Tratamento dos eventos de início e fim de requisições http.
    $scope.$on('http:start', function(evt, error) {
      $scope.httpIndicator = true;

      $timeout(function() {
        $scope.httpIndicator = false;
      }, 10000);
    });

    $scope.$on('http:stop', function(evt, error) {
      $scope.httpIndicator = false;
    });

    // Eventos gerados em respostas com erro.
    $scope.$on('page:notFound', handleError);
    $scope.$on('server:error', handleError);
    $scope.$on('connection:error', handleError);

	  $scope.collapse = true;

	  $scope.logged = Auth.isLogged();
	  $scope.email = '';
	  $scope.password = '';

	  $scope.login = function () {
      Auth.login($scope.email, $scope.password);

      $scope.logged = Auth.isLogged();

      $location.path('/request');
	  };

    $scope.logout = function () {
      Auth.logout();

      $scope.logged = Auth.isLogged();

      $location.path('/');
    };

  });
