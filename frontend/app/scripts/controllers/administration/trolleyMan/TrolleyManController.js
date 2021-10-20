'use strict';

angular.module('frontendApp')
  .controller('TrolleyManCtrl', ['$scope', '$rootScope', '$modal', 'growl', 'TrolleyManService',
    function ($scope, $rootScope, $modal, growl, TrolleyManService) {

      function addLeadingZeroes(targetLength, inputString) {
        var inputString = inputString.toString();
        for (var i = inputString.length; i < targetLength; i++) {
          inputString = '0' + inputString;
        }
        return inputString.toString();
      }

      function addBarcode(trolleyMan){
        trolleyMan.forEach(function(man){
          man.barcode = '99900003' + addLeadingZeroes(5,man.id);
        })
        return trolleyMan;
      }


      function init() {
        TrolleyManService.getAllTrolleyMan(function (trolleyMan) {
          $scope.trolleyMan = addBarcode(trolleyMan);
        })
      }

      init();


      $scope.openCreateTrolleyManModal = function () {
        var scope = $rootScope.$new();
        scope.params = {};
        $modal({
          controller: "CreateTrolleyManCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/administration/trolleyMan/createTrolleyManModal.html"
        });
      };

      $scope.updateName = function (id, name) {
        var command = {};
        command.id = id;
        command.name = name;
        TrolleyManService.updateTrolleyMan(command);
      };

      $scope.updateSurname = function (id, surname) {
        var command = {};
        command.id = id;
        command.surname = surname;
        TrolleyManService.updateTrolleyMan(command);
      };


      $scope.$on('TrolleyManCreated', function () {
        init();
        growl.success('admin.trolleyMan.modal.create.message.success', {ttl: 5000})
      })
      $scope.$on('TrolleyManUpdated', function () {
        init();
        growl.success('admin.trolleyMan.modal.update.message.success', {ttl: 5000})
      })
    }])
