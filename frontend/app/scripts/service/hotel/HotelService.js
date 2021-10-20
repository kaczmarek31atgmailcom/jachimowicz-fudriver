'use strict';

angular.module('frontendApp')
.factory('HotelService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getSettingsHotelHeaders = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/hotel/settings/hotel/headers'
    }).then(function successCallback(response){
      successFn(response.data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.getRooms = function(hotelId,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/hotel/' + hotelId + '/rooms'
    }).then(function successCallback(response){
      successFn(response.data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.getMaxPeriod = function(reservationId,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/hotel/reservation/' + reservationId + '/max-period'
    }).then(function successCallback(response){
      successFn(response.data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.getHotelOccupancy = function(hotelId,startDate,endDate,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/hotel/book/' + hotelId + '/' + startDate + '/' + endDate
    }).then(function successCallback(response){
      successFn(response.data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.createHotel = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/hotel',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('HotelCreated',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.bookHotelBed = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/hotel/book',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('BedBooked',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.reserveHotelBed = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/hotel/reserve',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('BedReserved',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.updateBooking = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/hotel/book',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('BookingUpdated',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.updateReservation = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/hotel/reserve',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('ReservationUpdated',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.deleteReservation = function(reservationId){
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/hotel/booking/' + reservationId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('ReservationDeleted',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };



  return service;
}]);
