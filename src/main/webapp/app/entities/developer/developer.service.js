(function() {
    'use strict';
    angular
        .module('devladApp')
        .factory('Developer', Developer);

    Developer.$inject = ['$resource'];

    function Developer ($resource) {
        var resourceUrl =  'api/developers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
