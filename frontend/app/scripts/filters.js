'use strict';

var app = angular.module('myFilters', []);

app.filter('dotToCommaKg', function () {
  return function (input) {
    if (input) {
      var result = input.toString().replace('.', ',');
      return result + ' kg';
    }
  };
});

app.filter('dotToComma', function () {
  return function (input) {
    if (input) {
      var result = input.toString().replace(',', '').replace('.', ',');
      return result;
    }
    return 0;
  };
});

app.filter('round', function () {
  return function (input) {
    if (input) {
      var result = Math.round((input * 100)) / 100;
      return result;
    }
  };
});

app.filter('roundDecimal', function () {
  return function (input, decimalNumbers) {
    var decimal = Math.pow(10,decimalNumbers);
    if (input) {
      var result = Math.round((input * decimal )) / (decimal) ;
      return result;
    }
  };
});

app.filter('hideZero',function(){
  return function(input){
    if(String(input) !== "0"){
      return input;
    }
  }
});

app.filter('reverse', function () {
  return function (items) {
    return items.slice().reverse();
  };
});

app.filter('longToCurrency', function () {
  return function (input) {
    var result = input / 100;
    result = result.toString().replace(',', '').replace('.', ',');
    return result;
  }
});

app.filter('longToWeight', function () {
  return function (input) {
    if (input === undefined) {
      return 0
    } else {
      var result = parseInt(input) / 1000;
      result = result.toString().replace(',', '').replace('.', ',');
      return result;
    }
  }
});

app.filter('minutesToHours', function () {
  return function (input) {
    if (input === undefined) {
      return 0
    } else {
      var hours = parseInt(input / 60);
      var minutes = input % 60;
      for (var i = hours.toString().length; i < 2; i++) {
        hours = '0' + hours;
      }
      for (var j = minutes.toString().length; j < 2; j++) {
        minutes = '0' + minutes;
      }
      return hours + ':' + minutes;
    }
  }
});

app.filter('secondsToHours', function () {
  return function (input) {
    if (input === undefined) {
      return 0
    } else {
      var hours = parseInt(input / 3600);
      var minutes = input % 60;
      for (var i = hours.toString().length; i < 2; i++) {
        hours = '0' + hours;
      }
      for (var j = minutes.toString().length; j < 2; j++) {
        minutes = '0' + minutes;
      }
      return hours + ':' + minutes;
    }
  }
});


app.filter('minutesToDecimal', function () {
  return function (input) {
    if (input === undefined) {
      return 0
    } else {
      return (parseInt((input / 60) * 100) / 100).toString().replace('.', ',');

    }
  }

});
