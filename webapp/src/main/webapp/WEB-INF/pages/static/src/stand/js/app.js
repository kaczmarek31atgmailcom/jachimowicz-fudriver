'use strict';

angular
    .module('standApp', [
        'ngRoute',
        'angular-growl'
    ]);

angular.module('standApp')
    .config(['growlProvider', function (growlProvider) {
        growlProvider.onlyUniqueMessages(false);
    }]);
