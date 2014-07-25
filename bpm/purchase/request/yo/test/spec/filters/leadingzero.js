'use strict';

describe('Filter: leadingZero', function () {

  // load the filter's module
  beforeEach(module('bpmPurchaseRequestSteelAppApp'));

  // initialize a new instance of the filter before each test
  var leadingZero;
  beforeEach(inject(function ($filter) {
    leadingZero = $filter('leadingZero');
  }));

  it('should return the input prefixed with "leadingZero filter:"', function () {
    var text = 'angularjs';
    expect(leadingZero(text)).toBe('leadingZero filter: ' + text);
  });

});
