'use strict';

angular.module('frontendApp')
  .controller('HotelBookingCtrl', ['$rootScope', '$scope', 'growl', '$filter', '$modal', 'HotelService',
    function ($rootScope, $scope, growl, $filter, $modal, HotelService) {

      $scope.loading = 0;
      $scope.startDate = moment().subtract(1, 'months');
      $scope.endDate = moment();
      $scope.occupancy = [];
      $scope.daysTable = [];

      function onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
      }


      $scope.getUniqueDays = function (startDate, endDate) {
        var result = [];
        var day = {};
        day.date = moment(startDate).format("YYYY-MM-DD");
        while (moment(day.date).isSameOrBefore(moment(endDate))) {
          var newDay = {};
          newDay.date = moment(day.date).format("YYYY-MM-DD");
          newDay.short = moment(day.date).format("DD");
          newDay.weekDay = moment(day.date).day();
          result.push(newDay);
          day.date = moment(day.date).add(1, 'day');
        }
        return result.sort(compareDates);
      };

      function compareDates(a, b) {
        if (moment(a.date).isAfter(moment(b.date))) {
          return 1;
        }
        if (moment(a.date).isBefore(moment(b.date))) {
          return -1;
        }
        return 0;
      }

      $scope.init = function () {
        var days = $scope.getUniqueDays($scope.startDate, $scope.endDate);
        HotelService.getSettingsHotelHeaders(function (hotels) {
          $scope.hotels = hotels;
          if (hotels.length > 0) {
            $scope.loading = 1;
            if (($scope.hotelId === undefined) || ($scope.hotelId === null)) {
              $scope.hotelId = hotels[0].id;
            }
            HotelService.getRooms($scope.hotelId, function (rooms) {
              $scope.rooms = rooms;
              HotelService.getHotelOccupancy($scope.hotelId,
                moment($scope.startDate).format("YYYY-MM-DD"),
                moment($scope.endDate).format("YYYY-MM-DD"), function (occupancy) {
                  $scope.occupancy = occupancy;
                  $scope.days = $scope.getDailyUtilization(days,occupancy);
                  $scope.daysTable = $scope.buildDaysTable(rooms, occupancy, $scope.days);
                  $scope.loading = 0;
                });
            })
          }
        });
      };
      $scope.init();


      $scope.buildDaysTable = function (rooms, occupancy, days) {
        var table = [];
        rooms.forEach(function (room) {
          room.beds.forEach(function (bed) {
            var row = [];
            days.forEach(function (day) {
              row.push($scope.getBedStatus(bed.bedId, day, occupancy))
            });
            table[bed.bedId] = row;
          })
        });
        return table;
      };

      $scope.editDay = function (field) {
        var scope = $rootScope.$new();
        if (field.status === 'UNRESERVED') {
          scope.params = {
            day: field.date,
            bedId: field.bedId,
            occupancy: $scope.getBedOccupancyDaysOnly(field.bedId, $scope.occupancy),
            periodStartDate: moment($scope.startDate).format("YYYY-MM-DD"),
            periodEndDate: moment($scope.endDate).format("YYYY-MM-DD")
          };
          $modal({
            scope: scope,
            animation: true,
            templateUrl: 'views/hotel/new-reservation-modal.html',
            show: true
          })
        }


        if ((field.status === 'BOOKING') || (field.status === 'RESERVATION')) {
          scope.params = {
            reservation: field
          };
          $modal({
            scope: scope,
            animation: true,
            templateUrl: 'views/hotel/edit-booking-modal.html',
            show: true
          })
        }
      };


      $scope.getBedOccupancyDaysOnly = function (bedId, occupancy) {
        var result = [];
        occupancy.forEach(function (item) {
          if (item.bedId === bedId) {
            result.push(item);
          }
        });
        return result.sort(compareOccupanciesOfTheSameBed);
      };

      function compareOccupanciesOfTheSameBed(a, b) {
        if (moment(a.date).isAfter(moment(b.date))) {
          return 1;
        }
        if (moment(a.date).isBefore(moment(b.date))) {
          return -1;
        }
        return 0;
      }

      $scope.getBedStatus = function (bedId, date, occupancies) {
        var bedStatus = {};
        bedStatus.bedId = bedId;
        bedStatus.date = date.date;
        bedStatus.weekDay = date.weekDay;
        bedStatus.status = 'UNRESERVED';
        bedStatus.class = (date.weekDay % 6 === 0) ? 'fa fa-bed fa-lg font-green red-background' : 'fa fa-bed fa-lg font-green';
        for (var i = 0; i < occupancies.length; i++) {
          if ((occupancies[i].bedId === bedId) &&
            (moment(occupancies[i].startDate).isSameOrBefore(moment(date.date))) &&
            (moment(occupancies[i].endDate).isSameOrAfter(moment(date.date)))) {
            bedStatus.reservationId = occupancies[i].id;
            bedStatus.reservationStartDate = occupancies[i].startDate;
            bedStatus.reservationEndDate = occupancies[i].endDate;
            bedStatus.status = occupancies[i].reservationType;
            bedStatus.personId = occupancies[i].personId;
            bedStatus.personName = occupancies[i].personName;
            bedStatus.personSurname = occupancies[i].personSurname;
            bedStatus.description = occupancies[i].description;
            if (occupancies[i].reservationType === 'BOOKING') {
              bedStatus.class = 'fa fa-bed fa-lg font-red';
            }
            if (occupancies[i].reservationType === 'RESERVATION') {
              bedStatus.class = 'fa fa-bed fa-lg font-blue';
            }
            return bedStatus;
          }
        }
        return bedStatus;
      };


      $scope.getBedClass = function (status) {
        if (status === 'OCCUPIED') {
          return 'fa fa-bed fa-lg font-red';
        }
        if (status === 'UNOCCUPIED') {
          return 'fa fa-bed fa-lg font-green';
        }
        if (status === 'RESERVED') {
          return 'fa fa-bed fa-lg font-blue';
        }
        return 'fa fa-bed fa-lg font-default';
      };


      $scope.getDailyUtilization = function(days,occupancy){
        days.forEach(function(day){
          day.utilization = getNumberOfBookingsForDay(day.date,occupancy);
        });
        return days;
      };

      function getNumberOfBookingsForDay(day,occupancy){
        var total = 0;
        var mday = moment(day);
        occupancy.forEach(function(item){
          if((mday.isSameOrAfter(moment(item.startDate))) && (mday.isSameOrBefore(moment(item.endDate)))){
            total++;
          }
        });
        return total;
      }

      $scope.$on('BedBooked', function () {
        growl.success('hotel.modal.booking.message.success', {ttl: 3000});
        $scope.init();
      });

      $scope.$on('BookingUpdated', function () {
        growl.success('hotel.modal.booking.message.success', {ttl: 3000});
        $scope.init();
      });

      $scope.$on('ReservationUpdated', function () {
        growl.success('hotel.modal.booking.message.success', {ttl: 3000});
        $scope.init();
      });

      $scope.$on('ReservationDeleted', function () {
        growl.success('hotel.modal.booking.reservation-deleted-message', {ttl: 3000});
        $scope.init();
      });


      $scope.$on('BedReserved', function () {
        growl.success('hotel.modal.reservation.message.success', {ttl: 3000});
        $scope.init();
      });

      $scope.$on('error', function () {
        growl.error('error', {ttl: '5000'});
      });


    }]);

//////////////////////////////
angular.module('frontendApp')
  .controller('NewHotelReservationCtrl', ['$scope', 'HotelService', 'peopleService', function ($scope, HotelService, peopleService) {
    var bedId = {};
    $scope.selected = {};
    if ($scope.params !== undefined) {
      $scope.startDate = $scope.params.day;
      $scope.endDate = $scope.params.day;
      $scope.day = $scope.params.day;
      $scope.periodStartDate = $scope.params.periodStartDate;
      $scope.periodEndDate = $scope.params.periodEndDate;
      $scope.occupancy = $scope.params.occupancy;
      bedId = $scope.params.bedId;
    } else {
      $scope.startDate = moment();
      $scope.moementDate = moment();
      $scope.day = {};
      $scope.periodStartDate = {};
      $scope.periodEndDate = {};
      $scope.occupancy = [];
      bedId = {};
    }
    $scope.init = function () {
      $scope.minDate = ($scope.occupancy.length > 0) ? $scope.findMinNewPeriodDate($scope.occupancy, $scope.day, $scope.periodStartDate) : moment($scope.periodStartDate).subtract(1, 'day').format("YYYY-MM-DD");
      $scope.maxDate = ($scope.occupancy.length > 0) ? $scope.findMaxNewPeriodDate($scope.occupancy, $scope.day, $scope.periodEndDate) : $scope.periodEndDate;
      peopleService.getPeople(true, function (people) {
        $scope.people = preparePeopleArray(people);
      });
    };

    function preparePeopleArray(people){
      var result = [];
      var person = {};
      people.forEach(function(item){
        person = {};
        person.id = item.id;
        person.name = item.id + ' ' + item.name + ' ' + item.surname;
        result.push(person);
      });

      return result;
    }

    $scope.findMinNewPeriodDate = function (occupancy, day, periodStartDay) {
      var momentDay = moment(day);
      var momentResult = moment(periodStartDay).subtract(1, 'day');
      occupancy.forEach(function (item) {
        var momentItemEndDate = moment(item.endDate);
        if ((momentItemEndDate.isAfter(momentResult)) && ((momentItemEndDate).isBefore(momentDay))) {
          momentResult = momentItemEndDate;
        }
      });
      return momentResult.format('YYYY-MM-DD');
    };

    $scope.findMaxNewPeriodDate = function (occupancy, day, periodEndDay) {
      var momentDay = moment(day);
      var momentResult = moment(periodEndDay);
      occupancy.forEach(function (item) {
        var momentItemStartDate = moment(item.startDate);
        if ((momentItemStartDate.isBefore(momentResult)) && ((momentItemStartDate).isAfter(momentDay))) {
          momentResult = momentItemStartDate.subtract(1, 'day');
        }
      });
      return momentResult.format('YYYY-MM-DD');
    };

    $scope.createBooking = function () {
      if ((bedId > 0) && ($scope.selected.value.id > 0) && $scope.startDate && $scope.endDate) {
        var command = {};
        command.bedId = bedId;
        command.personId = $scope.selected.value.id;
        command.startDate = $scope.startDate;
        command.endDate = $scope.endDate;
        command.message = $scope.description;
        HotelService.bookHotelBed(command);
      }
    };

    $scope.createReservation = function () {

      if ((bedId > 0) && !($scope.personId > 0) && $scope.startDate && $scope.endDate) {
        var command = {};
        command.bedId = bedId;
        command.startDate = $scope.startDate;
        command.endDate = $scope.endDate;
        command.message = $scope.description;
        HotelService.reserveHotelBed(command);
      }
    }

  }]);
//////////////////////////////
angular.module('frontendApp')
  .controller('EditHotelBookingCtrl', ['$scope', 'HotelService', function ($scope, HotelService) {
    $scope.reservation = $scope.params.reservation;
    var init = function () {
      HotelService.getMaxPeriod($scope.reservation.reservationId, function (maxPeriod) {
        $scope.minDate = maxPeriod.startDate;
        $scope.maxDate = maxPeriod.endDate;
      })
    };

    $scope.deleteReservation = function(){
      HotelService.deleteReservation($scope.reservation.reservationId);
    };

    $scope.updateBooking = function () {
      var command = {};
      if ($scope.reservation.status === 'RESERVATION') {
        command.reservationId = $scope.reservation.reservationId;
        command.startDate = moment($scope.reservation.reservationStartDate).format("YYYY-MM-DD");
        command.endDate = moment($scope.reservation.reservationEndDate).format("YYYY-MM-DD");
        command.description = $scope.reservation.description;
        HotelService.updateReservation(command);
      }
      if ($scope.reservation.status === 'BOOKING') {
        command.bookingId = $scope.reservation.reservationId;
        command.startDate = moment($scope.reservation.reservationStartDate).format("YYYY-MM-DD");
        command.endDate = moment($scope.reservation.reservationEndDate).format("YYYY-MM-DD");
        command.description = $scope.reservation.description;
        HotelService.updateBooking(command);
      }
    };
    init();

  }]);

