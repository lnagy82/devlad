(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('SkillSetDetailController', SkillSetDetailController);

    SkillSetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SkillSet', 'Skill'];

    function SkillSetDetailController($scope, $rootScope, $stateParams, previousState, entity, SkillSet, Skill) {
        var vm = this;

        vm.skillSet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('devladApp:skillSetUpdate', function(event, result) {
            vm.skillSet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
