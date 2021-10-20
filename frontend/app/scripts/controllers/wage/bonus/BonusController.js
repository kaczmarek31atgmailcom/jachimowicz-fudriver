"use strict";

angular.module("frontendApp")
  .controller("BonusCtrl", ["$scope", "growl", "$modal", "BonusService", function ($scope, growl, $modal, BonusService) {

    $scope.bonuses = [];

    var init = function () {
      BonusService.getActiveBonuses(function (bonuses) {
        $scope.bonuses = bonuses;
      })
    };
    init();

    $scope.openAddFixedBonusModal = function () {
      var addFixedBonusModal = $modal({
        animation: $scope.animationsEnabled,
        templateUrl: 'views/wage/bonus/add-fixed-bonus-modal.html',
        show: true
      })
    };

    $scope.openAddPercentBonusModal = function () {
      var addPercentBonusModal = $modal({
        animation: $scope.animationsEnabled,
        templateUrl: 'views/wage/bonus/add-percent-bonus-modal.html',
        show: true
      })
    };

    $scope.openConfirmDeletePrompt = function(bonus){
      $scope.bonusToDelete = angular.copy(bonus);
      var deleteBonusModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/wage/bonus/confirmDeleteBonusPrompt.html',
        show: true
      })
    };



    $scope.$on('BonusCreated',function(){
      growl.success('payroll.bonus.add-message.success',{ttl:3000});
      init();
    });

    $scope.$on('BonusDeleted',function(){
      growl.success('payroll.bonus.remove-message.success',{ttl:3000});
      init();
    });

    $scope.$on('error',function(){
      growl.error('error');
    });

  }]);
