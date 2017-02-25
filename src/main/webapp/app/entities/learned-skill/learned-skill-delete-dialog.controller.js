(function() {
    'use strict';

    angular
        .module('devladApp')
        .controller('LearnedSkillDeleteController',LearnedSkillDeleteController);

    LearnedSkillDeleteController.$inject = ['$uibModalInstance', 'entity', 'LearnedSkill'];

    function LearnedSkillDeleteController($uibModalInstance, entity, LearnedSkill) {
        var vm = this;

        vm.learnedSkill = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LearnedSkill.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
