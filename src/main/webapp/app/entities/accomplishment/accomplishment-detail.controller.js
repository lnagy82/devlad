(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('AccomplishmentDetailController', AccomplishmentDetailController);

    AccomplishmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Accomplishment', 'Developer'];

    function AccomplishmentDetailController($scope, $rootScope, $stateParams, previousState, entity, Accomplishment, Developer) {
        var vm = this;

        vm.accomplishment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('devladApp:accomplishmentUpdate', function(event, result) {
            vm.accomplishment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
