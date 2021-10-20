'use strict';

angular.module('frontendApp')
.controller('OutgoDeliverLetterCtrl',['$scope','growl','$routeParams','DeliveryLetterService','EventService',
  function($scope,growl,$routeParams,DeliveryLetterService,EventService){

    $scope.orderId = $routeParams.orderId;
    $scope.deliveryLetter = {};
    $scope.palettes = [];
    var init = function () {
      DeliveryLetterService.getLetter($scope.orderId, function (deliveryLetter) {
        $scope.deliveryLetter = deliveryLetter;
        $scope.palettes = angular.copy(deliveryLetter.palettes);
        $scope.palettes.sort(comparePalettes);
      });
    };
    init();

    var comparePalettes = function (a, b) {
      if (a.position < b.position)
        return -1;
      if (a.position > b.position)
        return 1;
      return 0;
    };

    $scope.event = EventService.getEvents(function (response) {
      var response = JSON.parse(response);
      var message = response.source;

      if (message === 'DELIVERY_LETTER_UPDATED') {
        DeliveryLetterService.getLetter($scope.orderId, function (deliveryLetter) {
          $scope.deliveryLetter = deliveryLetter;
          $scope.palettes = angular.copy(deliveryLetter.palettes);
          $scope.palettes.sort(comparePalettes);
          growl.success("delivery-letter.success-message", {ttl: 2000});
        })
      }
    });

    $scope.$on('error', function () {
      growl.error("error", {ttl: 5000});
    })


  }]);
