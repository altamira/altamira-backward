'use strict';

angular.module('webApp')
  .controller('RequestItemCtrl', function ($rootScope, $scope, $log, Restangular, Request, Material, DateUtil) {

  // Verifica se um item foi selecionado
  $scope.title = 'Ítem Requisição';

  var item = Request.getSelectedItem();

  if (item.id === 0) {
    $scope.title = 'Novo ' + $scope.title;
  }

  // Adiciona uma propriedade para controlar o formato do material (Bobina ou Chapa).
  item.cut = item.material.length === 0 ? 'B' : 'C';

  var treatments = Request.getTreatments();
  var thicknesses = Request.getThicknesses();
  var widths = Request.getWidths();
  var lengths = Request.getLengths();

  // Adiciona a opção de outra escolha às listas.
  var thicknessesOptions = [];
  _.each(thicknesses, function (thickness) {
    thicknessesOptions.push({ desc : thickness.toFixed(2), value : thickness });
  });
  thicknessesOptions.push({ desc : 'Outra...', value :  '--' });

  item.otherThickness = item.material.thickness;
  if(!_.contains(thicknesses, item.material.thickness)) {
    item.material.thickness = thicknessesOptions[thicknessesOptions.length - 1].value;
  }

  // Uma vez construída a lista de espessuras, reatribuí à variável que será exibida.
  thicknesses = thicknessesOptions;

  widths = angular.copy(widths);
  widths.push('Outra...');

  item.otherWidth = item.material.width;
  if(!_.contains(widths, item.material.width)) {
    item.material.width = widths[widths.length - 1];
  }

  lengths = angular.copy(lengths);
  lengths.push('Outro...');

  item.otherLength = item.material.length;
  if(!_.contains(lengths, item.material.length)) {
    item.material.length = lengths[lengths.length - 1];
  }

  // Converte o valor para o tipo Date usado na modal.
  item.arrival = new Date(item.arrival);

  var minDate = new Date();
  minDate.setHours(0,0,0,0);

  // Configura o escopo que será acessado pelo template (view).
  $scope.modal = {
    item : item,
    treatments : treatments,
    thicknesses : thicknesses,
    widths : widths,
    lengths : lengths,
    showOtherThickness : item.material.thickness === thicknesses[thicknesses.length - 1].value,
    showOtherWidth : item.material.width === widths[widths.length - 1],
    showOtherLength : item.material.length === lengths[lengths.length - 1],
    opened : false,
    minDate : minDate
  };

  $scope.openCalendar = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.modal.opened = true;
  };

  $scope.ok = function () {
    // Tradução do valor para casos de escolha da uma opção alternativa.
    if (_.isNaN(parseFloat(item.material.thickness))) {
      item.material.thickness = item.otherThickness;
    }
    delete item.otherThickness;

    if (_.isNaN(parseFloat(item.material.width))) {
      item.material.width = item.otherWidth;
    }
    delete item.otherWidth;

    if (_.isNaN(parseFloat(item.material.length))) {
      item.material.length = item.otherLength;
    }
    delete item.otherLength;

    // Remove a propriedade auxiliar usada para controlar o tipo de material.
    delete item.cut;

    // Trunc no dia da data escolhida.
    item.arrival.setHours(0, 0, 0, 0);

    // Transforma a data para o formato trocado com o servidor.
    item.arrival = item.arrival.getTime();

    Request.setSelectedItem(item);

    // Cria o material. Para isso é necessário que o mesmo esteja sempre com o identificador igual à '0'.
    var editedItem = item;
    editedItem.material.id = 0;
    Material.save(editedItem.material).then(function(dataMaterial) {
      // Verifica se existe na requisição um ítem com o material escolhido e na data escolhida.
      if (editedItem.id === 0 && _.find($scope.request.items, function (requestItem) {
          return requestItem.material.id === dataMaterial.id && requestItem.arrival === editedItem.arrival;
        })){

        $scope.alerts.push({ msg : 'Não foi possível adicionar o ítem. Já existe um material para a data escolhida!', type : 'danger'});

        $timeout(function() { if (!_.isEmpty($scope.alerts)) { $scope.alerts.splice(0, 1); }}, 5000);
      } else {
        // Configura o material após uma inserção com sucesso.
        editedItem.material = Restangular.stripRestangular(dataMaterial);

        // Transfere as alterações para o objeto que será original.
        angular.copy(editedItem, item);

        // Ser for um novo ítem adicioná-o ao final da lista antes de enviar. Se for uma edição os dados já foram atualizados localmente.
        if (item.id === 0) {
          $scope.request.all('items').post(item).then(function (data) {
            $log.info(data);

            $scope.getCurrentRequest();
          });
        } else {
          $log.info(editedItem);
          $scope.request.all('items').customPUT(item, item.id).then(function (data) {
            $log.info(data);

            $scope.getCurrentRequest();
          });
        }
      }
    });

    $rootScope.back();
  };

  $scope.cancel = function () {
    $rootScope.back();
  };

  $scope.changedThickness = function () {
    $scope.modal.showOtherThickness = _.isNaN(parseFloat($scope.modal.item.material.thickness));
  };

  $scope.changedWidth = function () {
    $scope.modal.showOtherWidth = _.isNaN(parseFloat($scope.modal.item.material.width));
  };

  $scope.changedLength = function () {
    $scope.modal.showOtherLength = _.isNaN(parseFloat($scope.modal.item.material.length));
  };

  $scope.changedCut = function () {
    if ($scope.modal.item.cut === 'B') {
      $scope.modal.item.material.length = 0;
    }
  };

  /*$rootScope.$on('RequestItemChanged', function() {
      var item = Request.getSelectedItem();
  });*/

});
