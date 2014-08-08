'use strict';

/**
 * @ngdoc function
 * @name 1820e33145e64965a1432bda5b86f405.controller:EditCtrl
 * @description
 * # EditCtrl
 * Controller of the 1820e33145e64965a1432bda5b86f405
 */
angular.module('1820e33145e64965a1432bda5b86f405')
  .controller('EditCtrl', function ($rootScope, $scope, $location, Restangular, item) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    var treatments = [{ desc : 'Chapa Preta', value : 'PR' }, { desc : 'Decapado', value : 'DE' }, { desc : 'Galvanizado', value : 'GA' }];

    var thicknesses = [0.65, 0.85, 0.90, 1.20, 1.40, 2.00, 2.20];

    var widths = [80, 90, 100, 120, 200, 240, 260, 300, 320, 330, 350, 400, 450];

    var lengths = [0, 100, 200, 500, 800, 900, 950, 1000, 1100, 1200, 1500, 1750, 1900, 2000, 2100, 2200, 2400, 3000, 3200];

    // var thicknesses = [0.60, 0.75, 0.85, 1.80, 2.00];
    // $scope.widths = [100.00, 132.00, 165.00, 200.00, 240.00, 330.00, 367.00, 420.00, 915.00, 990.00];
    // $scope.lengths = [0.00, 1200.00, 1400.00, 1800.00, 2000.00, 2400.00];

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
		//title : title,
		item : item,
		treatments : treatments,
		thicknesses : thicknesses,
		widths : widths,
		lengths : lengths,
		showOtherThickness : item.material.thickness === thicknesses[thicknesses.length - 1].value,
		showOtherWidth : item.material.width === widths[widths.length - 1],
		showOtherLength : item.material.length === lengths[lengths.length - 1],
		opened : false,
		minDate : minDate,
		format : 'R',
		treatmentFF : false,
		treatmentFQ : 'PR'
	};

	$scope.formatChapa = false;
	$scope.formatRolo = false;

	$scope.save = function () {
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

		// Trunc no dia da data escolhida.
		item.arrival.setHours(0, 0, 0, 0);

		// Transforma a data para o formato trocado com o servidor.
		item.arrival = item.arrival.getTime();

		// Retorna fornecendo o ítem.
		if (item.id === 0) {
			Restangular.one('request', 0).post('item', item).then(function () {
				$location.path('/#');
			});
		} else {
			item.put().then(function () {
				$location.path('/#');
			});
		}
        
        
        
	};

	$scope.format = function(f) {
		$scope.modal.item.material.length = lengths[0];
		$scope.modal.format = f;
		$scope.changedLength();
	};

	$scope.lamination = function(l) {
		if (l === 'FQ') {
			$scope.modal.treatmentFF = false;
			$scope.modal.item.material.treatment = $scope.modal.treatmentFQ;
		} else {
			$scope.modal.item.material.treatment = '';
		}
		$scope.modal.item.material.lamination = l;
	};

	$scope.treatment = function(t) {
		if (t === 'GA') {
			$scope.modal.item.material.treatment = $scope.modal.treatmentFF ? 'GA' : '';	
		} else {
			$scope.modal.treatmentFQ = t;
			$scope.modal.item.material.treatment = t;
		}
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

	$scope.$watch('modal.item', function (it) {
		if (!it) { return; }

		if (it.material.length > 0) {
			$rootScope.toggle('TabChapa', 'on'); 	
			$scope.modal.format = 'C';
		} else {
			$rootScope.toggle('TabRolo', 'on');
		}
		if (it.material.lamination === 'FF') {
			$rootScope.toggle('TabFF', 'on');
			if (it.material.treatment === 'GA') {
				$scope.modal.treatmentFF = true;
			}
		} else {
			$rootScope.toggle('TabFQ', 'on');
			if (it.material.treatment === 'DE') {
				$rootScope.toggle('TabDE', 'on');
				$scope.modal.treatmentFQ = 'DE';
			} else {
				$rootScope.toggle('TabPR', 'on');
			}
		}
        
    });
  });
