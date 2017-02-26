(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('DeveloperDetailController', DeveloperDetailController);

    DeveloperDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Developer', 'Principal'];

    function DeveloperDetailController($scope, $rootScope, $stateParams, previousState, entity, Developer, Principal) {
        var vm = this;

        vm.developer = entity;
        vm.previousState = previousState.name;
        
        if(vm.developer == null){
        	var a = Principal.identity();
        	vm.developer = Principal.identity().$$state.value.developer;
        }

        var unsubscribe = $rootScope.$on('devladApp:developerUpdate', function(event, result) {
            vm.developer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
