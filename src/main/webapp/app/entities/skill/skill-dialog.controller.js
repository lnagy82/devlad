(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('SkillDialogController', SkillDialogController);

    SkillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Skill', 'SkillSet'];

    function SkillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Skill, SkillSet) {
        var vm = this;

        vm.skill = entity;
        vm.clear = clear;
        vm.save = save;
        vm.skillsets = SkillSet.query({
            page: 0,
            size: 100,
            sort: 'asc'
        }, null, null);

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.skill.id !== null) {
                Skill.update(vm.skill, onSaveSuccess, onSaveError);
            } else {
                Skill.save(vm.skill, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('devladApp:skillUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
