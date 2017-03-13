(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('DeveloperDetailController', DeveloperDetailController);

    DeveloperDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Developer', 'Principal', 'LearnedSkillbyDeveloper', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$state'];

    function DeveloperDetailController($scope, $rootScope, $stateParams, previousState, entity, Developer, Principal, LearnedSkillbyDeveloper, ParseLinks, AlertService, paginationConstants, pagingParams, $state) {
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
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = 5;

        loadAll();

        function loadAll () {
            LearnedSkillbyDeveloper.query({
            	developerId: vm.developer.id,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.learnedSkills = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
