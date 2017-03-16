(function() {
    'use strict';
    angular
        .module('devladApp')
        .factory('SkillRequest', SkillRequest);

    SkillRequest.$inject = ['$resource', 'DateUtils'];

    function SkillRequest ($resource, DateUtils) {
        var resourceUrl =  'api/skill-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created = DateUtils.convertDateTimeFromServer(data.created);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
