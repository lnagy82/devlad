(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('SkillSetDialogController', SkillSetDialogController);

    SkillSetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SkillSet', 'Skill'];

    function SkillSetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SkillSet, Skill) {
        var vm = this;

        vm.skillSet = entity;
        vm.clear = clear;
        vm.save = save;
        vm.skills = Skill.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.skillSet.id !== null) {
                SkillSet.update(vm.skillSet, onSaveSuccess, onSaveError);
            } else {
                SkillSet.save(vm.skillSet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('devladApp:skillSetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
