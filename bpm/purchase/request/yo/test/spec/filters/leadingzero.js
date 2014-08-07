'use strict';

describe('Filter: leadingZero', function () {

  // load the filter's module
  beforeEach(module('1820e33145e64965a1432bda5b86f405'));

  // initialize a new instance of the filter before each test
  var leadingZero;
  beforeEach(inject(function ($filter) {
    leadingZero = $filter('leadingZero');
  }));

  it('should return the input prefixed with "leadingZero filter:"', function () {
    expect(leadingZero(1234, 6)).toBe('001234');
  });

});
