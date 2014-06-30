'use strict';

angular.module('webApp')
  .controller('MainCtrl', function ($rootScope, $scope, $cookieStore, $location, $modal, $log, $timeout, Auth) {

    // Configura o subtítulo a ser usado na página.
    $rootScope.page.title = 'Home';

    var handleError = function(evt, error) {
      var modalInstance = $modal.open({
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

    var setTilesAreaSize = function(){
        var groups = $('.tile-group');
        var tileAreaWidth = 160;
        $.each(groups, function(i, t){
            tileAreaWidth += $(t).outerWidth()+46;
        });
        $('.tile-area').css({
            width: tileAreaWidth
        });
    };

    var addMouseWheel = function (){
        $('body').mousewheel(function(event, delta){
            var scroll_value = delta * 50;
            $(document).scrollLeft($(document).scrollLeft() - scroll_value);
            return false;
        });
    };

    setTilesAreaSize();
    addMouseWheel();

  });
