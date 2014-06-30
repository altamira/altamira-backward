'use strict';

angular.module('webApp')
  .factory('Auth', function ($cookieStore) {
    var _user = $cookieStore.get('user');

    var setUser = function(user) {
      _user = user;

      $cookieStore.put('user', _user);
    };

    // Public API here
    return {
      isLogged: function () {
        return _user !== null;
      },
      login: function (email, password) {
        setUser({email : email, password : password});
      },
      logout: function () {
        $cookieStore.remove('user');

        _user = null;
      }
    };
  });