(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('TeamMemberDetailController', TeamMemberDetailController);

    TeamMemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TeamMember', 'Team', 'Developer'];

    function TeamMemberDetailController($scope, $rootScope, $stateParams, previousState, entity, TeamMember, Team, Developer) {
        var vm = this;

        vm.teamMember = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('devladApp:teamMemberUpdate', function(event, result) {
            vm.teamMember = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
