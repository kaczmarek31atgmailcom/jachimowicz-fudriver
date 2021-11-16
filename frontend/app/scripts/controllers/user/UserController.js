'use strict';

angular.module('frontendApp').controller('UserCtrl', ['$scope', '$modal', 'userService', function ($scope, $modal, userService) {
  $scope.users;
  $scope.animationsEnabled = true;
  $scope.addUser;
  $scope.isLoginUnique = true;
  $scope.isEditLoginUnique = true;

  $scope.getActiveUsers = function () {
    userService.getActiveUsers(function (users) {
      $scope.users = users;
    })
  };

  $scope.checkRight = function (userId, role) {
    var result = false;
    $scope.users.forEach(function (item) {
      if (item != null) {
        if (item.id == userId) {
          item.roles.forEach(function (itemRole) {
              if ((itemRole.name != undefined) && (itemRole != null)) {
                if (!itemRole.name.localeCompare(role)) {
                  result = true;
                }
              }
            }
          )
        }
      }
    });
    return result;
  };

  $scope.checkLoginUniqueness = function (login) {
    var result = true;
    $scope.logins.forEach(function (item) {
      if (!item.localeCompare(login)) {
        result = false
      }
    });
    $scope.isLoginUnique = result;
  };

  $scope.checkEditLoginUniqueness = function (login, origLogin) {
    var result = true;
    $scope.logins.forEach(function (item) {
      if ((!item.localeCompare(login)) && (item.localeCompare(origLogin))) {
        result = false
      }
    });
    $scope.isLoginUnique = result;
  };


  $scope.openAddUserModal = function () {
    userService.getAllLogins(function (logins) {
      $scope.logins = logins;
    });
    var addUserModal = $modal({
      scope: $scope,
      animation: $scope.animationsEnabled,
      templateUrl: 'views/administration/users/addUserModal.html',
      show: true
    })
  };

  $scope.openEditUserModal = function (user) {
    userService.getAllLogins(function (logins) {
      $scope.logins = logins;
    });
    $scope.origLogin = user.login;
    $scope.editUser = {};
    $scope.editUser.id = user.id;
    $scope.editUser.login = user.login;
    $scope.editUser.password = '';
    $scope.editUser.name = user.name;
    $scope.editUser.surname = user.surname;
    $scope.editUser.version = user.version;
    $scope.editUser.panel = $scope.checkRight(user.id, 'ROLE_PANEL');
    $scope.editUser.admin = $scope.checkRight(user.id, 'ROLE_DOSTEPY');
    $scope.editUser.place = $scope.checkRight(user.id, 'ROLE_ROZLICZENIA');
    $scope.editUser.waga = $scope.checkRight(user.id, 'ROLE_WAGA');
    $scope.editUser.leader = $scope.checkRight(user.id, 'ROLE_LEADER');
    $scope.editUser.palety = $scope.checkRight(user.id, 'ROLE_PALETY');
    $scope.editUser.handlowiec = $scope.checkRight(user.id, 'ROLE_HANDLOWIEC');
    $scope.editUser.hotel = $scope.checkRight(user.id, 'ROLE_HOTEL');
    var editUserModal = $modal({
      scope: $scope,
      animation: $scope.animationsEnabled,
      templateUrl: 'views/administration/users/editUserModal.html',
      show: true
    })
  };


  $scope.saveUser = function (addUser) {
    userService.createUser(addUser);
  };

  $scope.updateUser = function (editUser) {
    userService.updateUser(editUser);
  };

  $scope.deleteUser = function (userId) {
    var deleteCommand = {};
    deleteCommand.userId = userId;
    userService.deleteUser(deleteCommand);
  };

  $scope.$on('UserUpdated', function () {
    userService.getActiveUsers(function (users) {
      $scope.users = users;
    });
  });

  $scope.$on('UserCreated', function () {
    userService.getActiveUsers(function (users) {
      $scope.users = users;
    });
  });

  $scope.$on('UserDeleted', function () {
    userService.getActiveUsers(function (users) {
      $scope.users = users;
    });
  });

  $scope.$on('error', function () {
    $scope.errorMessage = "Coś poszło nie tak";
  });


}]);
