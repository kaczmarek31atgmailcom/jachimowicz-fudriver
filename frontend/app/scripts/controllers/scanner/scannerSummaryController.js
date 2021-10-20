'use strict';
angular.module('frontendApp')
  .controller('scannerSummaryCtrl', ['$scope','$rootScope','$location','growl','$modal','scannerService','appService',
    function ($scope, $rootScope, $location, growl, $modal, scannerService,appService) {

    $scope.headers = [];
    $scope.commitedTrolley = '';
    $scope.commitError = false;
    $scope.activeEastMushrooms = false;

    function isEastMushroomsActive() {
      appService.getActiveEastMushrooms(function (response) {
        $scope.activeEastMushrooms = response;
      });
    }
    isEastMushroomsActive();

    $scope.loadHeaders = function(){
      scannerService.getWozekHeaders(function(headers){
        $scope.headers = headers;
        $scope.totalAmount = $scope.countTotalAmount(headers);
        $scope.totalWeight = $scope.countTotalWeight(headers);
      })
    };

    $scope.edit = function(wozekId){
      $location.url('trolleyDetails/' + wozekId);
    };

    $scope.commit = function(wozekId){
      var command = {};
      command.wozekId = wozekId;
      scannerService.commitTrolley(command);
    };

    $scope.showSummary = function(){
      $location.url('trolleysSummary');
    };

    $scope.$on('trolleyCommitSuccess',function(event,data){
      $scope.commitedTrolley = data.entityId;
      removeCommitedTrolley(data.entityId)
    });

      $scope.$on('MassCommitSuccess',function(event,data){
        $scope.loadHeaders();
        growl.success('scanner.total.commit.success.message',{ttl:4000})
      });


      $scope.$on('trolleyCommitError',function(event,data){
      $scope.commitedTrolley = '';
      $scope.commitError = true;
    });


    $scope.onHold = function(trolleyId){
      scannerService.onHold(trolleyId);
    };

    $scope.activate = function(trolleyId){
      scannerService.activateTrolley(trolleyId);
    };

    $scope.$on('trolleyActivated', function(){
      scannerService.getWozekHeaders(function(headers){
        $scope.headers = angular.copy(headers);
      })
    });

    $scope.$on('onHoldSucceed', function(){
      scannerService.getWozekHeaders(function(headers){
        $scope.headers = headers;
      })
    });

    $scope.isActivationDisabled = function(rodzajId){
      var result = false;
      $scope.headers.forEach(function(item){
        if((item.spaceId == 0) && (item.rodzajId == rodzajId)){
          result = true;
        }
      });
      return result;
    };

    $scope.countTotalAmount = function(headers){
      var result = 0;
      headers.forEach(function(header){
        if(header.spaceId === 0){
          result += header.totalPcs;
        }
      });
      return result;
    };

    $scope.countTotalWeight = function(headers){
      var result = 0;
      headers.forEach(function(header){
        if(header.spaceId === 0){
          result += header.totalWeight;
        }
      });
      return Math.round(result * 100)/100;
    };


    var removeCommitedTrolley = function(trolleyId){
      for(var i = $scope.headers.length - 1; i >= 0; i--){
        if($scope.headers[i].wozekNr == trolleyId){
          $scope.headers.splice(i,1);
        }
      }
    };

    $scope.openCommitToWarehouseModal = function(trolleyId){
      var scope = $rootScope.$new();
      $modal({
        scope: scope,
        animation: true,
        templateUrl: 'views/scanner/commit-harvest-palette-to-warehouse-modal.html',
        show: true
      })
    };


  }]);
