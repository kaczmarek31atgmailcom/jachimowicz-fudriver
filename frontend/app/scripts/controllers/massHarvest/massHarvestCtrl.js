'use strict';
angular.module('frontendApp')
  .controller('massHarvestCtrl', ['$scope', '$rootScope', '$modal', '$http', 'notify', 'massHarvestService', 'massHarvestPromiseService', function ($scope, $rootScope, $modal, $http, notify, massHarvestService, massHarvestPromiseService) {

    $scope.harvestDate = new Date();

    massHarvestPromiseService.then(function (data) {
      $scope.hale = data.hale;
      $scope.pickers = data.pickers;
      $scope.types = data.types;
      $scope.groups = getGroups($scope.pickers);
      $scope.activeGroup = $scope.groups[0];
      $scope.activeHala = $scope.hale[0];
      $scope.activeType = $scope.types[0];
    });


    $scope.setActiveGroup = function (group) {
      $scope.activeGroup = group;
    };

    $scope.setActiveHala = function (hala) {
      $scope.activeHala = hala;
    };


    $scope.setActiveType = function (type) {
      $scope.activeType = type;
    };


    $scope.send = function () {
      var paleta = {};
      var pickerHarvests = [];
      for (var i = $scope.pickers.length - 1; i >= 0; i--) {
        if ($scope.pickers[i].amount != undefined) {
          if ($scope.pickers[i].amount > 0) {
            var pickerHarvest = {};
            pickerHarvest.pickerId = $scope.pickers[i].id;
            pickerHarvest.amount = $scope.pickers[i].amount;
            pickerHarvests.push(pickerHarvest);
          }
        }
      }
      paleta.halaId = $scope.activeHala.id;
      paleta.typeId = $scope.activeType.id;
      paleta.harvestDate = $scope.harvestDate;
      paleta["pickerHarvests"] = pickerHarvests;
      $scope.test = paleta;
      massHarvestService.send(paleta);
      $scope.pickers.forEach(function (item) {
        item.amount = '';
      });
      $scope.totalWeight = 0;
      $scope.totalAmount = 0;
    };

    $scope.$on('paletaCreated', function () {
      notify({
        message: 'Paleta zatwierdzona pomyślnie'
      });
    });

    $scope.$on('error', function () {
      notify({
        message: 'Błąd zatwierdzania palety!',
        classes: 'alert-danger'
      });
    });


    $scope.totalWeight = 0;
    $scope.totalAmount = 0;
    $scope.isFormInvalid = true;
    $scope.updateTotalWeightAndAmount = function (isFormInvalid) {
      var totalAmount = 0;
      $scope.isFormInvalid = isFormInvalid;
      $scope.pickers.forEach(function (item) {
        if (item.hasOwnProperty('amount')) {
          if (parseInt(item.amount) >= 0) {
            totalAmount = parseInt(totalAmount) + parseInt(item.amount);
          }
        }
      });
      $scope.totalAmount = totalAmount;
      $scope.totalWeight = totalAmount * $scope.activeType.weight;
    };

    $scope.isWyslijButtonInactive = true;

    $scope.updateWyslijButtonState = function () {
      if (!$scope.isFormInvalid && $scope.totalAmount > 0) {
        $scope.isWyslijButtonInactive = false;
      } else {
        $scope.isWyslijButtonInactive = true;
      }
    };

    $scope.getWeight = function (amount, weight) {
      var result = amount * weight;
      if (amount > 0 && weight > 0) {
        return result;
      } else {
        return 0;
      }
    };

    $scope.shouldBeDisplayed = function (pickerGroupId) {
      if (pickerGroupId == $scope.activeGroup.id) {
        return true;
      }
      return false;
    };


    var getGroups = function (pickers) {
      var groupsArray = [];
      var groups = [];
      pickers.forEach(function (item) {
        groupsArray[item.groupId] = item.groupName;

      });
      for (var i in groupsArray) {
        var grupa = {};
        grupa['id'] = i;
        grupa['name'] = groupsArray[i];
        groups.push(grupa);
      }
      return groups;
    };


  }]);
