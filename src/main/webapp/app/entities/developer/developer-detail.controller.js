(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('DeveloperDetailController', DeveloperDetailController);

    DeveloperDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Developer'];

    function DeveloperDetailController($scope, $rootScope, $stateParams, previousState, entity, Developer) {
        var vm = this;

        vm.developer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('devladApp:developerUpdate', function(event, result) {
            vm.developer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
