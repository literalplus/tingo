var GreetingController = function ($scope, $http) {
    var ctrl = this;
    this.greeting = 'nothing yet';

    $http.get('http://localhost:8080/hello/World')
        .success(function (data) {
            ctrl.greeting = data.greeting;
        });
};

var tingoApp = angular.module('tingo', []);

tingoApp.controller('GreetingController', GreetingController);
