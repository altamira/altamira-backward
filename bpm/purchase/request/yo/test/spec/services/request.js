'use strict';

describe('Service: request', function () {

  // load the service's module
  beforeEach(module('1820e33145e64965a1432bda5b86f405'));

  // instantiate service
  var request;
  beforeEach(inject(function (_request_) {
    request = _request_;
  }));

  it('should do something', function () {
    expect(!!request).toBe(true);
  });

});
