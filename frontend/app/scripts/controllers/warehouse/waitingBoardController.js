'use strict';
angular.module('frontendApp').controller('waitingBoardCtrl', ['$scope', '$interval', 'notify', 'waitingBoardService', function ($scope, $interval, notify, waitingBoardService) {


  $scope.getHeaders = function () {
    waitingBoardService.getWaitingHeaders(function (headers) {
      $scope.headers = headers;
    })
  };


  var checkHeaders = function () {
    waitingBoardService.getWaitingHeaders(function (data) {
      var headers = data;
      var found = false;
      headers.forEach(function (newHeader) {
        $scope.headers.forEach(function (oldHeader) {
          if (newHeader.trolleyId == oldHeader.trolleyId) {
            found = true;
          }
        });
        if (!found) {
          $scope.headers.push(newHeader);
        }
        found = false;
      });


        for (var i = $scope.headers.length - 1; i >= 0; i--) {
          var found = false;
          headers.forEach(function (newHeader) {
            if (newHeader.trolleyId == $scope.headers[i].trolleyId) {
              found = true;
            }
          });
          if (!found) {
            $scope.headers.splice(i, 1);
          }
          found = false;
        }
      })
    };

    $interval(checkHeaders, 50000);


    $scope.commit = function (nr, data) {
      var command = {};
      command.nr = nr;
      command.amount = data;
      if (command.amount > 0) {
        waitingBoardService.commit(command);
      } else {
        notify({message: 'Wprowadź ilość większą od zera.', duration: 1500, classes: ['alert', 'alert-danger']});
      }
    };

    $scope.$on('WozekCommited', function (event, data) {
      if (data.data.body == 'Invalid amount' && data.data.status == 'ERROR') {
        notify({message: 'Niewłaściwa ilość', duration: 1500, classes: ['alert', 'alert-danger']});
      }
      else if (data.data.status == 'OK') {
        notify({message: 'Paleta przeniesiona do chłodni', duration: 1500, classes: ['alert', 'alert-danger']});
        checkHeaders();
      }
    });

    $scope.$on('WozekCommitError', function () {
      notify({message: 'Coś poszło nie tak.', duration: 1500, classes: ['alert', 'alert-danger']});
    });


    $scope.reject = function (nr) {
      var command = {};
      command.wozekNr = nr;
      waitingBoardService.reject(command);
    };

    $scope.$on('WozekRejected', function () {
      checkHeaders();
    });

  }
  ]);
