'use strict';

angular.module('frontendApp')
  .factory('EastMushroomsWarehouseService',['$rootScope','$http',function($rootScope,$http){
    var service = {};
    service.startDate = moment().subtract(1, 'months');
    service.endDate = moment();
    service.showBackToReleasesBreadcrumb = false;
    service.shipment = {'id':''};


    service.getWaitingHarvestPalettes = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/waiting-harvest-palettes'
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getWarehousePalettesReadyToRelease = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/ready-to-release'
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getWarehousePaletteReadyToRelease = function(paletteId,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/ready-to-release/' + paletteId
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getCreatedWarehousePalettes = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/created'
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getCreatedWarehousePalette = function(paletteId,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/created/' + paletteId
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.createWarehousePalette = function(command){
      $http({
        method: 'POST',
        url: '/fudriver/rest/east-warehouse/warehouse-palette',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteCreated',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.createAcceptedWarehousePalette = function(command){
      $http({
        method: 'POST',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/accepted',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteCreated',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };


    service.reclassify = function(depotId){
      $http({
        method: 'POST',
        url: '/fudriver/rest/east-warehouse/reclassify',
        data: depotId,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehouseUnitReclassified',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };


    service.assignWarehousePalette = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/assign-harvest-palette',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteAssigned',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.unassignWarehousePalette = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/unassign-harvest-palette',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteUnassigned',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.moveHarvestPalette = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/move-harvest-palette',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteMoved',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.moveHarvestPalette = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/move-harvest-palette',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteMoved',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.rejectHarvestPalette = function(command){
      $http({
        method: 'POST',
        url: '/fudriver/rest/waiting/wozek/reject',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('HarvestPaletteRejected',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.acceptWarehousePaletteToWarehouse = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/accept-warehouse-palette-to-warehouse',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteAccepted',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.printPaletteLabel = function(paletteId){
      $http({
        method: 'POST',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/print-label/' + paletteId,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteLabelPrinted',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.createShipment = function(command){
      $http({
        method: 'POST',
        url: '/fudriver/rest/east-warehouse/shipment',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response,data){
        $rootScope.$broadcast('WarehouseShipmentCreated',response,data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.getShipmentHeaders = function(startDate,endDate,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/shipment/header/period/' + startDate + '/' + endDate
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getShipmentHeader = function(shipmentId,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/shipment/header/' + shipmentId
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getShipmentPalettes = function(shipmentId,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/shipment/' + shipmentId + "/palette"
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    service.getWz = function(wzId,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/wz/' + wzId
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getWarehouseStatus = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/status'
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    service.getWarehouseUnit = function(pickerId,uniqId,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/east-warehouse/warehouseUnit/' + pickerId + '/' + uniqId
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.moveBoxesBetweenWarehousePalettes = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/move-boxes',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('BoxesMovedToOtherPalette',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.joinWarehousePalettes = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/join-palettes',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePalettesJoined',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.getActivePaletteTypes = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/palette-type/active'
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.changeWarehousePaletteType = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/east-warehouse/warehouse-palette/palette-type',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('WarehousePaletteTypeChanged',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.updatePaletteTypeSortOrder = function(command){
      $http({
        method: 'PUT',
        url: '/fudriver/rest/palette-type/sort-order',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('SortOrderUpdated',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };


    return service;
  }]);
