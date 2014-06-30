'use strict';

angular.module('webApp')
  .factory('PurchasePlanning', function (Restangular) {
    var quotation = Restangular.one('quotation');

    // Public API...
    return {
      getCurrentPurchasePlanning: function () {
        return quotation.get();
      },
      save : function(purchasePlanningToSave) {
        return quotation.customPUT(purchasePlanningToSave);
      },
      closeCurrentPurchasePlanning : function() {
        return quotation.customPOST();
      }
    };
  });