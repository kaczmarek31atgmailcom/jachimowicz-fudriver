'use strict';

angular.module('frontendApp')
  .controller('HotelSettingsCtrl', ['$scope', 'growl', '$modal', 'HotelService',
    function ($scope, growl, $modal, HotelService) {

      function init() {
        HotelService.getSettingsHotelHeaders(function (headers) {
          $scope.headers = headers;
        })
      }

      init();

      $scope.openCreateHotelModal = function () {
        $modal({
          scope: $scope,
          animation: $scope.animationsEnabled,
          templateUrl: 'views/hotel/settings/create-hotel-modal.html',
          show: true
        })
      };

      $scope.$on('HotelCreated', function () {
        growl.success("hotel.settings.create.hotel.message.hotel-created-success", {ttl: 4000});
        init();
      });

      $scope.$on('error', function () {
        growl.error('error', {ttl: 5000});
      })

    }]);


////////////////////////////////
angular.module('frontendApp')
  .controller('CreateHotelCtrl', ['$scope', '$filter', 'HotelService', function ($scope, $filter, HotelService) {

    $scope.name = '';
    $scope.formValid = false;

    $scope.isNameInvalid = function (field) {
      var pristine = field.createHotel.$pristine;
      if ($scope.name.length > 0) {
        $scope.nameInvalid = false;
        return;
      }
      if (($scope.name.length === 0) && (pristine !== 'false')) {
        $scope.nameInvalid = true;
        $scope.nameErrorMessage = $filter('translate')('hotel.settings.create.hotel.message.invalid-name');
        return;
      }
    };

    $scope.createHotel = function () {
      var command = {};
      command.name = angular.copy($scope.name);
      command.roomsAmount = ($scope.roomsAmount) ? $scope.roomsAmount : 0;
      command.bedsInRoomAmount = ($scope.bedsAmount) ? $scope.bedsAmount : 0;
      HotelService.createHotel(command);
    }

  }]);
