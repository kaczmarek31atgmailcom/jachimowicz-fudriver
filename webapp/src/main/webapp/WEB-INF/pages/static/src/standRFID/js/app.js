'use strict';

angular
    .module('standRFIDApp', [
        'ngRoute',
        'angular-growl'
    ]);

angular.module('standRFIDApp')
    .config(['growlProvider', function (growlProvider) {
        growlProvider.onlyUniqueMessages(false);
    }]);
