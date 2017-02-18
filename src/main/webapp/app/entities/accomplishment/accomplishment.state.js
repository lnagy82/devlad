(function() {
    'use strict';

    angular
        .module('devladApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('accomplishment', {
            parent: 'entity',
            url: '/accomplishment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.accomplishment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accomplishment/accomplishments.html',
                    controller: 'AccomplishmentController',
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
                    $translatePartialLoader.addPart('accomplishment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('accomplishment-detail', {
            parent: 'accomplishment',
            url: '/accomplishment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.accomplishment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accomplishment/accomplishment-detail.html',
                    controller: 'AccomplishmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accomplishment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Accomplishment', function($stateParams, Accomplishment) {
                    return Accomplishment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'accomplishment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('accomplishment-detail.edit', {
            parent: 'accomplishment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accomplishment/accomplishment-dialog.html',
                    controller: 'AccomplishmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Accomplishment', function(Accomplishment) {
                            return Accomplishment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('accomplishment.new', {
            parent: 'accomplishment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accomplishment/accomplishment-dialog.html',
                    controller: 'AccomplishmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                experiencePoints: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('accomplishment', null, { reload: 'accomplishment' });
                }, function() {
                    $state.go('accomplishment');
                });
            }]
        })
        .state('accomplishment.edit', {
            parent: 'accomplishment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accomplishment/accomplishment-dialog.html',
                    controller: 'AccomplishmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Accomplishment', function(Accomplishment) {
                            return Accomplishment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('accomplishment', null, { reload: 'accomplishment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('accomplishment.delete', {
            parent: 'accomplishment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accomplishment/accomplishment-delete-dialog.html',
                    controller: 'AccomplishmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Accomplishment', function(Accomplishment) {
                            return Accomplishment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('accomplishment', null, { reload: 'accomplishment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
