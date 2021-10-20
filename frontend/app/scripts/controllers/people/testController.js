'use strict';
angular.module('frontendApp')
  .controller('testCtrl', ['$scope', 'testService', '$stomp','$log', function ($scope, testService, $stomp, $log) {

    var connectHeaders = '';

    $scope.res = 'dd';


    $scope.changeRes = function(){
      var response = {};
        response.content = new Date();
      setResponse(response);

    };

    $stomp.setDebug(function (args) {
      $log.debug(args)
    });


    var setResponse = function(response){
      $scope.res = response.content;
      $scope.$apply();
    };

    $scope.$watch('res', function(){
    });


    $stomp
      .connect('/fudriver/hello', connectHeaders)
      // frame = CONNECTED headers
      .then(function (frame) {
        var subscription = $stomp.subscribe('/topic/greetings', function (payload, headers, res) {
          //$scope.payload = payload;
          setResponse(payload);
        }, {
          'headers': 'are awesome'
        });

        // Unsubscribe
//        subscription.unsubscribe()

        // Send message
//        $stomp.send('/app/hello', {
//          message: 'body'
//        }, {
//          priority: 9,
//          custom: 42 // Custom Headers
//        });

        // Disconnect
//        $stomp.disconnect(function () {
//          $log.info('disconnected')
//        })
      });

  }]);
