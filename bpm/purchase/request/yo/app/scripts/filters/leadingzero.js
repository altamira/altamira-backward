'use strict';

/**
 * @ngdoc filter
 * @name bpmPurchaseRequestSteelAppApp.filter:leadingZero
 * @function
 * @description
 * # leadingZero
 * Filter in the bpmPurchaseRequestSteelAppApp.
 */
angular.module('1820e33145e64965a1432bda5b86f405')
  .filter('leadingZero', function () {
    return function(input, minNumberOfDigits) {
    	if (!_.isNull(input) && !_.isUndefined(input)) {
		    minNumberOfDigits = minNumberOfDigits || 2;
		    input = input + '';
		    var zeros = new Array(minNumberOfDigits - input.length + 1).join('0');
		    return zeros + input;
		}
	};
  });
