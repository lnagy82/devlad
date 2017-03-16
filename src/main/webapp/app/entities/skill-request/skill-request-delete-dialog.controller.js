(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('SkillRequestDeleteController',SkillRequestDeleteController);

    SkillRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'SkillRequest'];

    function SkillRequestDeleteController($uibModalInstance, entity, SkillRequest) {
        var vm = this;

        vm.skillRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SkillRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
