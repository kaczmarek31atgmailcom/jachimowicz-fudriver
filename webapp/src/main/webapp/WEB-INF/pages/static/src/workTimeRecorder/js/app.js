'use strict';

angular
    .module('timerApp', [
        'ngRoute',
        'angular-growl'
    ]);

angular.module('timerApp')
    .config(['growlProvider', function (growlProvider) {
        growlProvider.onlyUniqueMessages(false);
    }]);
