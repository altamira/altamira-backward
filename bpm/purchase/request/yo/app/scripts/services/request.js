'use strict';

/**
 * @ngdoc service
 * @name BpmPurchaseRequestApp.request
 * @description
 * # request
 * Service in the BpmPurchaseRequestApp.
 */
angular.module('1820e33145e64965a1432bda5b86f405')
	.service('Request', function Request() {
	// AngularJS will instantiate a singleton by calling "new" on this function
	})
	.factory('Request', function (Restangular) {
		var request = Restangular.all('request');

		var treatments = [{ desc : 'Chapa Preta', value : 'PR' }, { desc : 'Decapado', value : 'DE' }, { desc : 'Galvanizado', value : 'GA' }];

		var thicknesses = [0.65, 0.85, 0.90, 1.20, 1.40, 2.00, 2.20];

		var widths = [80, 90, 100, 120, 200, 240, 260, 300, 320, 330, 350, 400, 450];

		var lengths = [0, 100, 200, 500, 800, 900, 950, 1000, 1100, 1200, 1500, 1750, 1900, 2000, 2100, 2200, 2400, 3000, 3200];

		// Public API...
		return {
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
		    return request.get('0');
		  },
		  save : function(requestToSave) {
		    return request.customPOST(requestToSave);
		  },
		  sendCurrent: function () {
		    return request.customPOST();
		  }
		};
	});
