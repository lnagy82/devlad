(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('SkillRequestDialogController', SkillRequestDialogController);

    SkillRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SkillRequest'];

    function SkillRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SkillRequest) {
        var vm = this;

        vm.skillRequest = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.skillRequest.id !== null) {
                SkillRequest.update(vm.skillRequest, onSaveSuccess, onSaveError);
            } else {
                SkillRequest.save(vm.skillRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('devladApp:skillRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
