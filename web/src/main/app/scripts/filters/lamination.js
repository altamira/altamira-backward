'use strict';

angular.module('webApp')
  .filter('lamination', function () {
    var laminations = { 'FQ' : 'Fina Quente', 'FF' : 'Fina Frio'};

    return function (input) {
      var lamination = laminations[input];

      return lamination;
    };
  });