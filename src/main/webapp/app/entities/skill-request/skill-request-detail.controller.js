(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('SkillRequestDetailController', SkillRequestDetailController);

    SkillRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SkillRequest'];

    function SkillRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, SkillRequest) {
        var vm = this;

        vm.skillRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('devladApp:skillRequestUpdate', function(event, result) {
            vm.skillRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
