'use strict';

var ConfirmRemoveItemModalController = function ($scope, $modalInstance, title, item) {
  $scope.title = title;

  $scope.ok = function () {
    $modalInstance.close(item);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
};

var ConfirmSendRequestModalController = function ($scope, $modalInstance, title) {
  $scope.title = title;

  $scope.ok = function () {
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
};

angular.module('webApp')
  .controller('RequestCtrl', function ($filter, $rootScope, $scope, $modal, $window, $log, $timeout, $location, Restangular, DateUtil, Request, Material) {

    $scope.title = "Requisição de Compra de Aço";

    $rootScope.$on("RequestChanged", function(event, data) {
      $scope.request = data;
      // Configura o subtítulo a ser usado na página.
      $scope.title = 'Requisição de Compra de Aço #' + $scope.request.id;
    });

    $scope.request = Request.getCurrent();

    $scope.edit = function(item) {

      Request.setSelectedItem(item);

      $location.path("/requestItem");

    };

    $scope.delete = function (item) {
      var modalInstance = $modal.open({
          templateUrl: 'confirmRemoveItemModal.html',
          controller: ConfirmRemoveItemModalController,
          resolve: {
            title : function () {
              return 'Confirmação Remoção';
            },
            item : function () {
              return item;
            }
          }
        });

      modalInstance.result.then(function (item) {
        $scope.request.items = _.reject($scope.request.items, function (itemRequest) {
          return itemRequest.id === item.id;
        });

        Request.save($scope.request).finally(function() {
          $scope.getCurrentRequest();
        });
      }, function () {
      });
    };


    $scope.send = function () {
      var modalInstance = $modal.open({
          templateUrl: 'confirmSendRequestModal.html',
          controller: ConfirmSendRequestModalController,
          resolve: {
            title : function () {
              return 'Confirmação Envio';
            }
          }
        });

      modalInstance.result.then(function () {
        // Exibe modal de envio da requisição...
        var modalInstanceProgress = $modal.open({
            templateUrl: 'progressSendRequestModal.html',
            keyboard : false,
            backdrop : 'static'
          });

        Request.sendCurrentRequest().then(function () {
          $scope.alerts.push({ msg : 'Requisição enviada com sucesso!', type : 'success'});

          $timeout(function() { if (!_.isEmpty($scope.alerts)) { $scope.alerts.splice(0, 1); }}, 5000);
        }).finally(function() {
          // Fecha a modal de envio após o retorno.
          modalInstanceProgress.close('');

          // Realiza uma nova consulta para recuperar a próxima requisição.
          $scope.getCurrentRequest();
        });
      }, function () {
      });
    };

    $scope.print = function() {
      $window.open("http://localhost:8080/altamira-bpm/rest/requests/" + 126 + "/report");
    };

    $scope.closeAlert = function(index) {
      $scope.alerts.splice(index, 1);
    };
  
  });
