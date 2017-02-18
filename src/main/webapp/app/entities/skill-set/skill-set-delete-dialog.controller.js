(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('SkillSetDeleteController',SkillSetDeleteController);

    SkillSetDeleteController.$inject = ['$uibModalInstance', 'entity', 'SkillSet'];

    function SkillSetDeleteController($uibModalInstance, entity, SkillSet) {
        var vm = this;

        vm.skillSet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SkillSet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
