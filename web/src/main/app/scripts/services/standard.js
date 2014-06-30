'use strict';

angular.module('webApp')
  .factory('Standard', function (Restangular) {
    var request = Restangular.one('standard');

    // Public API...
    return {
      getAll: function () {
        return request.get();
      }
    };
  });
