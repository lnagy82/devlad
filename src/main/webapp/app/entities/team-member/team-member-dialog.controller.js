(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('TeamMemberDialogController', TeamMemberDialogController);

    TeamMemberDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TeamMember', 'Team', 'Developer'];

    function TeamMemberDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TeamMember, Team, Developer) {
        var vm = this;

        vm.teamMember = entity;
        vm.clear = clear;
        vm.save = save;
        vm.teams = Team.query({
            page: 0,
            size: 100,
            sort: 'asc'
        }, null, null);
        vm.developers = Developer.query({
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
            if (vm.teamMember.id !== null) {
                TeamMember.update(vm.teamMember, onSaveSuccess, onSaveError);
            } else {
                TeamMember.save(vm.teamMember, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('devladApp:teamMemberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
