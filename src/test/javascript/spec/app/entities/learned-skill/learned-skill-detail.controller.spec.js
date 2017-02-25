'use strict';

describe('Controller Tests', function() {

    describe('LearnedSkill Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLearnedSkill, MockDeveloper, MockSkill;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLearnedSkill = jasmine.createSpy('MockLearnedSkill');
            MockDeveloper = jasmine.createSpy('MockDeveloper');
            MockSkill = jasmine.createSpy('MockSkill');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LearnedSkill': MockLearnedSkill,
                'Developer': MockDeveloper,
                'Skill': MockSkill
            };
            createController = function() {
                $injector.get('$controller')("LearnedSkillDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'devladApp:learnedSkillUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
