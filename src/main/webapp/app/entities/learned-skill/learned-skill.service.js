(function() {
    'use strict';
    angular
        .module('devladApp')
        .factory('LearnedSkill', LearnedSkill);

    LearnedSkill.$inject = ['$resource', 'DateUtils'];

    function LearnedSkill ($resource, DateUtils) {
        var resourceUrl =  'api/learned-skills/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.learned = DateUtils.convertDateTimeFromServer(data.learned);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

(function() {
    'use strict';
    angular
        .module('devladApp')
        .factory('LearnedSkillbyDeveloper', LearnedSkillbyDeveloper);

    LearnedSkillbyDeveloper.$inject = ['$resource', 'DateUtils'];

    function LearnedSkillbyDeveloper ($resource, DateUtils) {
        var resourceUrl =  'api/learned-skills/developer/:developerId';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();


