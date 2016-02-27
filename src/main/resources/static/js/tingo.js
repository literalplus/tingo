//TODO: Split into multiple files
var AuthController = function ($rootScope, AuthService, AUTH_EVENTS) {
    var auth = this;
    this.credentials = {};
    this.loginError = null;
    this.registerError = null;

    this.login = function () {
        AuthService.login(auth.credentials);
    };

    this.logout = function () {
        AuthService.logout();
    };

    this.register = function () {
        AuthService.register(auth.credentials);
    };

    this.getUserName = function () {
        return AuthService.getUserName();
    };

    $rootScope.$on(AUTH_EVENTS.login_failure, function () {
        auth.loginError = 'Falscher Benutzername oder falsches Passwort!';
    });

    $rootScope.$on(AUTH_EVENTS.register_failure, function (evt, data) {
        auth.registerError = !!data ? data.errorMessage : 'Fehler beim Registrieren!';
    });

    $rootScope.$on(AUTH_EVENTS.login_success, function () {
        auth.registerError = null;
        auth.loginError = null;
    });
};

var HomeController = function ($http, $rootScope, AuthService) {
    var ctrl = this;
    this.teachers = {};

    AuthService.runWhenAuthenticated(function () {
        $http.get('/api/teacher/list')
            .success(function (data) {
                for(var i = 0; i < data.length; i++) {
                    console.info(data[i]);
                    var split = data[i].name.split(' ', 2);
                    data[i].firstName = split[0];
                    data[i].lastName = split[1];
                }

                ctrl.teachers = data;
            });
    });
};

var RegisterController = function () {

};

var LoginController = function ($stateParams, $rootScope, $location) {
    this.back = function () {
        if ($rootScope.returnto != null) {
            $location.path($rootScope.returnto);
            $rootScope.returnto = null;
        } else {
            $location.path('/');
        }
    }
};

var TeacherDetailController = function ($http, $stateParams) {
    var ctrl = this;
    this.teacher = {name: '...', abbreviation: '....'};
    this.fields = {};

    $http.get('/api/field/by/teacher/' + $stateParams.id)
        .success(function (data) {
            ctrl.teacher = data.teacher;
            ctrl.fields = data.fields;
        });
};

var NavDataController = function ($http, AuthService) {
    var ctrl = this;
    this.teachers = {};
    this.teachersLoaded = false;

    AuthService.runWhenAuthenticated(function () {
        $http.get('/api/teacher/list')
            .success(function (data) {
                ctrl.teachers = data;
                ctrl.teachersLoaded = true;
            });
    });
};

var tingoApp = angular.module('tingo', ['ui.router', 'ui.bootstrap']);

tingoApp.config(function ($stateProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $httpProvider) {
    $urlRouterProvider.otherwise('/');
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
        .state('register', {
            url: '/register',
            templateUrl: 'partials/register.html',
            controller: 'RegisterController',
            controllerAs: 'regCtrl',
            data: {
                no_auth: true
            }
        })
        .state('teachers', {
            url: '/teachers',
            abstract: true,
            template: '<ui-view/>'
        })
        .state('teachers.detail', {
            url: '/detail/:id',
            templateUrl: 'partials/teacher-detail.html',
            controller: 'TeacherDetailController',
            controllerAs: 'detailCtrl'
        });

    //Prevent Spring Security from displaying auth dialog, we control authentication ourselves!
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

tingoApp.factory('AuthService', function ($http, $rootScope, $location, AUTH_EVENTS) {
    var authService = {};

    authService.credentials = {};
    authService.username = null;
    authService.authenticated = false;
    authService.error = false;
    authService.errorMessage = "Falscher Benutzername oder falsches Passwort!";

    var setAuth = function (authed, name, credentials) {
        $rootScope.authenticated = authed;
        authService.authenticated = authed;
        authService.username = name;
        authService.credentials = credentials;
    };

    var resetAuth = function () {
        setAuth(false, null, {});
    };

    var authenticate = function (credentials, callback) {
        var headers = credentials ? {
            authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)
        } : {}; //add headers if we were called using credentials

        $http.get('auth/status', {headers: headers}).then(function (response) {
            if (response.data.name) {
                setAuth(true, response.data.name, credentials);
            } else {
                resetAuth();
            }
            callback && callback($rootScope.authenticated, response.data); //only call if we actually have a callback
        }, function () {
            resetAuth();
            callback && callback(false, {});
        });
    };

    authService.login = function (credentials) {
        authenticate(credentials,
            function (authSuccess, data) {
                authService.error = !authSuccess;
                if (authSuccess) {
                    $rootScope.$broadcast(AUTH_EVENTS.login_success, data);
                    if ($rootScope.returnto != null) {
                        $location.path($rootScope.returnto);
                        $rootScope.returnto = null;
                    } else {
                        $location.path('/');
                    }
                } else {
                    $rootScope.$broadcast(AUTH_EVENTS.login_failure, data);
                    $location.path('/login');
                }
            }
        );
    };

    authService.logout = function () {
        $http.post('logout', {}).success(function () {
            resetAuth();
            $location.path('/');
            $rootScope.$broadcast(AUTH_EVENTS.logout);
        }).error(function () { //TODO: What do we do here?
            resetAuth();
        });
    };

    authService.register = function (credentials) {
        resetAuth();
        $http.post('auth/register', credentials).then(function () {
            authService.login(credentials);
        }, function (response) {
            authService.error = true;
            authService.errorMessage = response.data.errorMessage;
            $rootScope.$broadcast(AUTH_EVENTS.register_failure, response.data);
        });
    };

    authService.isAuthenticated = function () {
        return authService.authenticated;
    };

    authService.runWhenAuthenticated = function (callback) {
        if(!callback) {
            return;
        }

        console.info(authService.isAuthenticated());
        console.info(callback);

        if(authService.isAuthenticated()) {
            callback();
        } else {
            $rootScope.$on(AUTH_EVENTS.login_success, callback);
        }
    };

    authService.checkAuthenticationStatus = function () {
        authenticate();
    };

    authService.getUserName = function () {
        return !!authService.username ? authService.username : 'Anon';
    };

    $rootScope.isAuthenticated = authService.isAuthenticated;

    return authService;
});

tingoApp.run(function ($rootScope, $state, $location, AuthService) {
    $rootScope.authenticated = false;
    $rootScope.returnto = null;
    $rootScope.$on('$stateChangeStart', function (e, to) {
        if ((!to.data || !to.data.no_auth) && !$rootScope.authenticated) {
            e.preventDefault();
            $state.go('login', {errRedirect: true});
            $rootScope.returnto = $location.path();
        }
    });

    AuthService.checkAuthenticationStatus();
});

tingoApp.controller('HomeController', HomeController);
tingoApp.controller('AuthController', AuthController);
tingoApp.controller('TeacherDetailController', TeacherDetailController);
tingoApp.controller('LoginController', LoginController);
tingoApp.controller('RegisterController', RegisterController);
tingoApp.controller('NavDataController', NavDataController);
tingoApp.constant('AUTH_EVENTS', {
    login_success: 'auth-login-success',
    login_failure: 'auth-login-failure',
    register_failure: 'auth-register-failure',
    logout: 'auth-logout'
});
