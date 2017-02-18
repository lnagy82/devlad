(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('AccomplishmentDialogController', AccomplishmentDialogController);

    AccomplishmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Accomplishment', 'Developer'];

    function AccomplishmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Accomplishment, Developer) {
        var vm = this;

        vm.accomplishment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.developers = Developer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accomplishment.id !== null) {
                Accomplishment.update(vm.accomplishment, onSaveSuccess, onSaveError);
            } else {
                Accomplishment.save(vm.accomplishment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('devladApp:accomplishmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
