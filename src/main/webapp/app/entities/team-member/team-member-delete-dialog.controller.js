(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('TeamMemberDeleteController',TeamMemberDeleteController);

    TeamMemberDeleteController.$inject = ['$uibModalInstance', 'entity', 'TeamMember'];

    function TeamMemberDeleteController($uibModalInstance, entity, TeamMember) {
        var vm = this;

        vm.teamMember = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TeamMember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
