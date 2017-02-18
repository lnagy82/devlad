(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('AccomplishmentDeleteController',AccomplishmentDeleteController);

    AccomplishmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Accomplishment'];

    function AccomplishmentDeleteController($uibModalInstance, entity, Accomplishment) {
        var vm = this;

        vm.accomplishment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Accomplishment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
