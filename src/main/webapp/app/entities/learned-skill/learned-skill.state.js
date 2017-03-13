(function() {
    'use strict';

    angular
        .module('devladApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('learned-skill', {
            parent: 'entity',
            url: '/learned-skill?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.learnedSkill.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learned-skill/learned-skills.html',
                    controller: 'LearnedSkillController',
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
                    $translatePartialLoader.addPart('learnedSkill');
                    $translatePartialLoader.addPart('level');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('learned-skill-detail', {
            parent: 'learned-skill',
            url: '/learned-skill/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'devladApp.learnedSkill.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/learned-skill/learned-skill-detail.html',
                    controller: 'LearnedSkillDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('learnedSkill');
                    $translatePartialLoader.addPart('level');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LearnedSkill', function($stateParams, LearnedSkill) {
                    return LearnedSkill.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'learned-skill',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('learned-skill-detail.edit', {
            parent: 'learned-skill-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learned-skill/learned-skill-dialog.html',
                    controller: 'LearnedSkillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LearnedSkill', function(LearnedSkill) {
                            return LearnedSkill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learned-skill.new', {
            parent: 'learned-skill',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learned-skill/learned-skill-dialog.html',
                    controller: 'LearnedSkillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                learned: null,
                                signed: null,
                                level: null,
                                exp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('learned-skill', null, { reload: 'learned-skill' });
                }, function() {
                    $state.go('learned-skill');
                });
            }]
        })
        .state('learned-skill.edit', {
            parent: 'learned-skill',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learned-skill/learned-skill-dialog.html',
                    controller: 'LearnedSkillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LearnedSkill', function(LearnedSkill) {
                            return LearnedSkill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learned-skill', null, { reload: 'learned-skill' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('learned-skill.delete', {
            parent: 'learned-skill',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/learned-skill/learned-skill-delete-dialog.html',
                    controller: 'LearnedSkillDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LearnedSkill', function(LearnedSkill) {
                            return LearnedSkill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('learned-skill', null, { reload: 'learned-skill' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
