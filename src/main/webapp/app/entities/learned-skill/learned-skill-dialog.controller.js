(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('LearnedSkillDialogController', LearnedSkillDialogController);

    LearnedSkillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LearnedSkill', 'Developer', 'Skill'];

    function LearnedSkillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LearnedSkill, Developer, Skill) {
        var vm = this;

        vm.learnedSkill = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.developers = Developer.query();
        vm.skills = Skill.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.learnedSkill.id !== null) {
                LearnedSkill.update(vm.learnedSkill, onSaveSuccess, onSaveError);
            } else {
                LearnedSkill.save(vm.learnedSkill, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('devladApp:learnedSkillUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.learned = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
