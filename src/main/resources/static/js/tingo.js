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
    };

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

var LoginController = function ($stateParams) {
    this.errRedirect = $stateParams.errRedirect;
};

var TeacherDetailController = function($http, $stateParams) {

};

var tingoApp = angular.module('tingo', [ 'ui.router' ]);

tingoApp.config(function($stateProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $httpProvider) {
    $urlRouterProvider.otherwise("/");
    $urlRouterProvider.when('/teachers', '/teachers/list');

    $stateProvider
        .state('home', {
            url: '/',
            templateUrl: 'partials/home.html',
            controller: 'HomeController',
            controllerAs: 'homeCtrl',
            data: {
                no_auth: true
            }
        })
        .state('login', {
            url: '/login?errRedirect',
            templateUrl: 'partials/login.html',
            controller: 'LoginController',
            controllerAs: 'loginCtrl',
            data: {
                no_auth: true
            }
        })
        .state('teachers', {
            url: '/teachers',
            abstract: true,
            template: '<ui-view/>'
        })
        .state('teachers.list', {
            url: '/list'
        })
        .state('teachers.detail', {
            url: '/detail',
            templateUrl: 'partials/teacher-detail.html',
            controller: 'TeacherDetailController',
            controllerAs: 'detailCtrl'
        });

    //Prevent Spring Security from displaying auth dialog, we control authentication ourselves!
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

tingoApp.run(function($rootScope, $state) {
    $rootScope.authenticated = false;
    $rootScope.$on('$stateChangeStart', function(e, to) {
        console.info(to);
        if((!to.data || !to.data.no_auth) && !$rootScope.authenticated) {
            e.preventDefault();
            $state.go('login', {errRedirect: true});
        }
    })
});

tingoApp.controller('HomeController', HomeController);
tingoApp.controller('AuthController', AuthController);
tingoApp.controller('TeacherDetailController', TeacherDetailController);
tingoApp.controller('LoginController', LoginController);
