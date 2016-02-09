//TODO: Split into multiple files

var HomeController = function ($http) {
    var ctrl = this;
    this.greeting = 'nothing yet';

    $http.get('http://localhost:8080/hello/World')
        .success(function (data) {
            ctrl.greeting = data.greeting;
        });
};

var AuthController = function($rootScope, $http, $location) {
    var auth = this;
    this.credentials = {};
    this.error = false;
    this.errorMessage = 'Vermutlich falscher Benutzername oder falsches Passwort.';

    var authenticate = function(credentials, callback) {
        var headers = credentials ? {
                authorization : "Basic " + btoa(credentials.username + ":" + credentials.password)
            } : {}; //add headers if we were called using credentials

        $http.get('auth/status', {headers : headers}).success(function(data) {
          if (data.name) {
            $rootScope.authenticated = true;
          } else {
            $rootScope.authenticated = false;
          }
          callback && callback($rootScope.authenticated); //only call if we actually have a callback
        }).error(function() {
          $rootScope.authenticated = false;
          callback && callback(false);
        });
    }

    this.login = function() {
        authenticate(auth.credentials,
            function (authSuccess) {
                auth.error = !authSuccess;
                if(authSuccess) {
                    $location.path('/');
                } else {
                    $location.path('/login');
                }
            }
        );
    };

    this.logout = function () {
        $http.post('logout', {}).success(function () {
            $rootScope.authenticated = false;
            $location.path('/');
        }).error(function(data) {
            $rootScope.authenticated = false;
        });
    };

    authenticate();
};

var tingoApp = angular.module('tingo', [ 'ngRoute' ]);

tingoApp.config(function($routeProvider, $httpProvider) {
    $routeProvider.when('/', {
        templateUrl: 'pages/home.html',
        controller: 'HomeController',
        controllerAs: 'homeCtrl'
    }).when('/login', {
        templateUrl: 'pages/login.html',
        controller: 'LoginController',
        controllerAs: 'loginCtrl'
    }).otherwise('/');

    //Prevent Spring Security from displaying auth dialog, we control authentication ourselves!
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

tingoApp.controller('HomeController', HomeController);

tingoApp.controller('AuthController', AuthController);

tingoApp.controller('LoginController', function () {});
