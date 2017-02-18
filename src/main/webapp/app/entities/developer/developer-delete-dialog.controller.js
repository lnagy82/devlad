(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('DeveloperDeleteController',DeveloperDeleteController);

    DeveloperDeleteController.$inject = ['$uibModalInstance', 'entity', 'Developer'];

    function DeveloperDeleteController($uibModalInstance, entity, Developer) {
        var vm = this;

        vm.developer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Developer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
