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
                for (var i = 0; i < data.length; i++) {
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
    };
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
    authService.authChecked = false;
    authService.authRequested = false;

    var setAuth = function (authed, name, credentials) {
        $rootScope.authenticated = authed;
        authService.authenticated = authed;
        authService.username = name;
        authService.credentials = credentials;
        $rootScope.$broadcast(AUTH_EVENTS.auth_status, authed);
    };

    var resetAuth = function () {
        setAuth(false, null, {});
    };

    var authenticate = function (credentials, callback) {
        var headers = credentials ? {
            authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)
        } : {}; //add headers if we were called using credentials

        $http.get('auth/status', {headers: headers}).then(function (response) {
            authService.authChecked = true;
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
        if (!callback) {
            return;
        }

        if (authService.isAuthenticated()) {
            callback();
        } else {
            $rootScope.$on(AUTH_EVENTS.login_success, callback);
        }
    };

    authService.checkAuthenticationStatus = function () {
        authService.authRequested = true;
        var prevAuthState = authService.authenticated;
        authenticate(null, function (authSuccess, data) {
            //Broadcast login success because auth check is async,
            //other code might have seen unauthenticated state
            //before and registered an event for authentication in the meantime
            if (authSuccess && !prevAuthState) {
                $rootScope.$broadcast(AUTH_EVENTS.login_success, data);
            }
        });
    };

    authService.isAuthenticatedSafe = function (callback) {
        if (!authService.authChecked && !authService.authRequested) {
            authService.checkAuthenticationStatus();
        }

        if (authService.authChecked) {
            callback(authService.isAuthenticated())
        } else if (callback) {
            var unregisterListener =
                $rootScope.$on(AUTH_EVENTS.auth_status, function (evt, authenticated) {
                    callback(authenticated);
                    unregisterListener();
                });
        }
    };

    authService.getUserName = function () {
        return !!authService.username ? authService.username : 'Anon';
    };

    $rootScope.isAuthenticated = authService.isAuthenticated;

    authService.checkAuthenticationStatus();

    return authService;
});

tingoApp.run(function ($rootScope, $state, $location, AuthService) { //just inject so it gets initialised
    $rootScope.authenticated = false;
    $rootScope.returnto = null;
    $rootScope.$on('$stateChangeStart', function (evt, to) {
        if (!to.data || !to.data.no_auth) {
            $rootScope.returnto = $location.path();
            //There must be a better solution to this
            //This event is called also when the first state is loaded, so
            //we might not know whether we're authenticated yet. If we don't, we can't really
            //do anything except wait for the auth reply - this happens primarily when a user
            //reloads an authed page or enters the URL directly
            //
            //This might flicker the new state for a fraction of a second, but they can't access
            //sensitive data anyways since the server checks auth itself
            //
            //The proper way to do this would be to somehow get Angular to wait for the login state
            //before loading any page, or use an intent, which Angular's event system doesn't support.
            AuthService.isAuthenticatedSafe(function (authenticated) {
                console.log('auth callback: ' + authenticated);
                if (!authenticated) {
                    $state.go('login');
                }
            });
        }
    });
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
    auth_status: 'auth-status-change',
    logout: 'auth-logout'
});
