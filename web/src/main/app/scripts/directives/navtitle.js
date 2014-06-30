'use strict';

angular.module('webApp')
  .directive('navtitle', function () {
    return {
      templateUrl: 'views/navtitle.html',
      restrict: 'E',
      link: function postLink(scope, element, attrs) {
        //element.text('this is the navtitle directive');
      }
    };
  });
