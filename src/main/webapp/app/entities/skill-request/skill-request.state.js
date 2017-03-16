(function() {
    'use strict';

    angular
        .module('devladApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('skill-request', {
            parent: 'entity',
            url: '/skill-request?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.skillRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skill-request/skill-requests.html',
                    controller: 'SkillRequestController',
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
                    $translatePartialLoader.addPart('skillRequest');
                    $translatePartialLoader.addPart('skillRequestStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('skill-request-detail', {
            parent: 'skill-request',
            url: '/skill-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.skillRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skill-request/skill-request-detail.html',
                    controller: 'SkillRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('skillRequest');
                    $translatePartialLoader.addPart('skillRequestStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SkillRequest', function($stateParams, SkillRequest) {
                    return SkillRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'skill-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('skill-request-detail.edit', {
            parent: 'skill-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-request/skill-request-dialog.html',
                    controller: 'SkillRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SkillRequest', function(SkillRequest) {
                            return SkillRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('skill-request.new', {
            parent: 'skill-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-request/skill-request-dialog.html',
                    controller: 'SkillRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                created: null,
                                requestor: null,
                                description: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('skill-request', null, { reload: 'skill-request' });
                }, function() {
                    $state.go('skill-request');
                });
            }]
        })
        .state('skill-request.edit', {
            parent: 'skill-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-request/skill-request-dialog.html',
                    controller: 'SkillRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SkillRequest', function(SkillRequest) {
                            return SkillRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('skill-request', null, { reload: 'skill-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('skill-request.delete', {
            parent: 'skill-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-request/skill-request-delete-dialog.html',
                    controller: 'SkillRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SkillRequest', function(SkillRequest) {
                            return SkillRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('skill-request', null, { reload: 'skill-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
