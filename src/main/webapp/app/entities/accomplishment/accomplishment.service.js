(function() {
    'use strict';
    angular
        .module('devladApp')
        .factory('Accomplishment', Accomplishment);

    Accomplishment.$inject = ['$resource'];

    function Accomplishment ($resource) {
        var resourceUrl =  'api/accomplishments/:id';

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
