(function() {
    'use strict';
    angular
        .module('devladApp')
        .factory('News', News);

    News.$inject = ['$resource', 'DateUtils'];

    function News ($resource, DateUtils) {
        var resourceUrl =  'api/news/:id';

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
