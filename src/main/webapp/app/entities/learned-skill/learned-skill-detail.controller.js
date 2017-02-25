(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('LearnedSkillDetailController', LearnedSkillDetailController);

    LearnedSkillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LearnedSkill', 'Developer', 'Skill'];

    function LearnedSkillDetailController($scope, $rootScope, $stateParams, previousState, entity, LearnedSkill, Developer, Skill) {
        var vm = this;

        vm.learnedSkill = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('devladApp:learnedSkillUpdate', function(event, result) {
            vm.learnedSkill = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
