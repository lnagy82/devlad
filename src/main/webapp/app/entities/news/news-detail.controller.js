(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('NewsDetailController', NewsDetailController);

    NewsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'News'];

    function NewsDetailController($scope, $rootScope, $stateParams, previousState, entity, News) {
        var vm = this;

        vm.news = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('devladApp:newsUpdate', function(event, result) {
            vm.news = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
