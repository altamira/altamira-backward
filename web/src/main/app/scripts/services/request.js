'use strict';

angular.module('webApp')
  .factory('Request', function ($rootScope, Restangular) {
    var rest = Restangular.all('requests');

    var treatments = [{ desc : 'Chapa Preta', value : 'PR' }, { desc : 'Decapado', value : 'DE' }, { desc : 'Galvanizado', value : 'GA' }];

    var thicknesses = [0.65, 0.85, 0.90, 1.20, 1.40, 2.00, 2.20];

    var widths = [80, 90, 100, 120, 200, 240, 260, 300, 320, 330, 350, 400, 450];

    var lengths = [0, 100, 200, 500, 800, 900, 950, 1000, 1100, 1200, 1500, 1750, 1900, 2000, 2100, 2200, 2400, 3000, 3200];

    var selectedRequest = null;
    var selectedRequestItem = null;

    // Public API...
    return {
      getSelectedItem : function() {
        if (selectedRequestItem === null) {
          selectedRequestItem = {
            id : 0,
            weight : null,
            arrival : new Date().getTime(),
            material : {
              id : 0,
              treatment : 'PR',
              lamination : 'FQ',
              thickness : 0.65,
              width : 80,
              length : 0
            }
          };
        }
        return selectedRequestItem;
      },
      setSelectedItem : function(item) {
        selectedRequestItem = item;
        $rootScope.$emit('RequestItemChanged');
      },
      getTreatments : function () {
        return treatments;
      },
      getThicknesses : function () {
        return thicknesses;
      },
      getWidths : function () {
        return widths;
      },
      getLengths : function () {
        return lengths;
      },
      getCurrent: function () {
        rest.get('current').then(function(request) {

          selectedRequest = request;
        // Correção no caso de um array null ao invés de vazio.
        if (_.isNull(selectedRequest.items)) {
          selectedRequest.items = [];
        }

        $rootScope.$emit('RequestChanged', selectedRequest);
        });
        return selectedRequest;
      },
      save : function(requestToSave) {
        return rest.customPUT(requestToSave);
      },
      sendCurrentRequest: function () {
        return rest.customPOST();
      },
      Selected : selectedRequest
    };
  });