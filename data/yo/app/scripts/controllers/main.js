'use strict';

/**
 * @ngdoc function
 * @name yoApp.controller:MainCtrl
 * @description # MainCtrl Controller of the yoApp
 */
angular.module('dataApp').controller('MainCtrl', function($scope, $http) {
	$scope.awesomeThings = [ 'HTML5 Boilerplate', 'AngularJS', 'Karma' ];
	$scope.serverSays = 'none';
    // Configura o subtítulo a ser usado na página.

	$http.get('./rest/members').success(function(data) {
		$scope.serverSays = data[0];
		console.log($scope.serverSays);
	}).error(function() {
		$scope.serverSays = 'error !';
	});

    var setTilesAreaSize = function(){
        var groups = $('.tile-group');
        var tileAreaWidth = 160;
        $.each(groups, function(i, t){
            tileAreaWidth += $(t).outerWidth()+46;
        });
        /*$('.tile-area').css({
            width: tileAreaWidth
        });*/
        $('.container').css({
            width: tileAreaWidth
        });
    };

    var addMouseWheel = function (){
        $('body').mousewheel(function(event, delta){
            var scrollValue = delta * 50;
            $(document).scrollLeft($(document).scrollLeft() - scrollValue);
            return false;
        });
    };

    setTilesAreaSize();
    addMouseWheel();

});
