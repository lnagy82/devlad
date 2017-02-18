(function() {
    'use strict';

    angular
        .module('devladApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('skill-set', {
            parent: 'entity',
            url: '/skill-set?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.skillSet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skill-set/skill-sets.html',
                    controller: 'SkillSetController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('skillSet');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('skill-set-detail', {
            parent: 'skill-set',
            url: '/skill-set/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.skillSet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skill-set/skill-set-detail.html',
                    controller: 'SkillSetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('skillSet');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SkillSet', function($stateParams, SkillSet) {
                    return SkillSet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'skill-set',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('skill-set-detail.edit', {
            parent: 'skill-set-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-set/skill-set-dialog.html',
                    controller: 'SkillSetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SkillSet', function(SkillSet) {
                            return SkillSet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('skill-set.new', {
            parent: 'skill-set',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-set/skill-set-dialog.html',
                    controller: 'SkillSetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('skill-set', null, { reload: 'skill-set' });
                }, function() {
                    $state.go('skill-set');
                });
            }]
        })
        .state('skill-set.edit', {
            parent: 'skill-set',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-set/skill-set-dialog.html',
                    controller: 'SkillSetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SkillSet', function(SkillSet) {
                            return SkillSet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('skill-set', null, { reload: 'skill-set' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('skill-set.delete', {
            parent: 'skill-set',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-set/skill-set-delete-dialog.html',
                    controller: 'SkillSetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SkillSet', function(SkillSet) {
                            return SkillSet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('skill-set', null, { reload: 'skill-set' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
