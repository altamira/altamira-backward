'use strict';

/**
 * @ngdoc directive
 * @name altamiraWebApp.directive:maintitle
 * @description
 * # maintitle
 */
angular.module('dataApp')
  .directive('maintitle', function () {
    return {
      templateUrl: 'views/maintitle.html',
      restrict: 'E',
      link: function postLink(/*scope, element, attrs*/) {
        //element.text('this is the maintitle directive');
      }
    };
  });
