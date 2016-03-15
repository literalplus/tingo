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
    this.fields = [];
    this.backupField = {};

    this.saveField = function (field) {
        var myIndex = _.indexOf(ctrl.fields, field);
        if(field.text.length === 0 || !field.text.trim()) {
            ctrl.fields.splice(myIndex);
            return;
        }

        $http.post('/api/field/save', field)
            .then(function (response) {
                ctrl.fields[myIndex] = response.data;
            }, function (response) {
                console.warn('Couldn\'t save fields: ');
                console.warn(response);
                alert('Fehler beim Speichern: ' + response.data.errorMessage);
            });
    };

    this.uneditField = function (field) {
        var myIndex = _.indexOf(ctrl.fields, field);
        ctrl.backupField.editing = false;
        ctrl.fields[myIndex] = ctrl.backupField;
    };

    this.editField = function (field) {
        ctrl.backupField = _.clone(field);
        field.editing = true;
    };

    this.addField = function () {
        ctrl.fields.push({text: '', teacherId: ctrl.teacher.id, editing: true});
    };

    this.deleteField = function (field) {
        $http.post('/api/field/delete', field)
            .then(function (response) {
                ctrl.fields = _.without(ctrl.fields, field);
            }, function (response) {
                console.warn('Couldn\'t delete field: ');
                console.warn(response);
                alert('Fehler beim Löschen: ' + response.data.errorMessage);
            })
    };

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

tingoApp.factory('AuthInterceptor', function ($q, TokenService) {
    var authInterceptor = {};

    authInterceptor.request = function (config) {
        if (TokenService.hasToken()) {
            config.headers.Authorization = 'Bearer ' + TokenService.getToken();
        }
        return config;
    };

    authInterceptor.responseError = function (response) {
        if (response.status === 401) {
            TokenService.setToken(null);
        }
        return $q.reject(response);
    };

    return authInterceptor;
});

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
    $httpProvider.interceptors.push('AuthInterceptor');
});

tingoApp.factory('TokenService', function ($window) {
    var tokenService = {};

    var token = null;

    tokenService.setToken = function (newToken) {
        token = newToken;

        if (!!token) {
            $window.localStorage.setItem('tingo-token', token);
        } else {
            $window.localStorage.removeItem('tingo-token');
        }
    };

    tokenService.getToken = function () {
        return token;
    };

    tokenService.hasToken = function () {
        return !!token;
    };

    var storedToken = $window.localStorage.getItem('tingo-token');
    if (!!storedToken) {
        token = storedToken;
    }

    return tokenService;
});

tingoApp.factory('AuthService', function ($http, $rootScope, $location, AUTH_EVENTS, $window, TokenService) {
    var authService = {};

    authService.credentials = {};
    authService.username = null;
    //authService.authenticated = false;
    authService.error = false;
    authService.errorMessage = "Falscher Benutzername oder falsches Passwort!";
    authService.authChecked = false;
    authService.authRequested = false;

    var setAuth = function (token) {
        authService.setToken(token);
        $rootScope.$broadcast(AUTH_EVENTS.auth_status, authService.authenticated);
    };

    var resetAuth = function () {
        setAuth(null);
    };

    var authenticate = function (credentials, callback) {
        $http.post('auth/login', credentials).then(function (response) {
            authService.authChecked = true;
            if (response.data.token) {
                setAuth(response.data.token);
            } else {
                resetAuth();
            }
            callback && callback(authService.authenticated, response.data); //only call if we actually have a callback
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
        //$http.post('logout', {}).success(function () {
        resetAuth();
        $location.path('/');
        $rootScope.$broadcast(AUTH_EVENTS.logout);
        //}).error(function () {
        //    resetAuth();
        //});
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

    authService.isAuthenticated = TokenService.hasToken;

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

    authService.isAuthenticatedSafe = function (callback) {
        callback(authService.isAuthenticated());
    };

    authService.getUserName = function () {
        return !!authService.username ? authService.username : 'Anon';
    };

    authService.setToken = function (token) {
        $rootScope.authenticated = authService.authenticated = !!token;
        authService.token = token;
        if (token == null) {
            authService.username = null;
            TokenService.setToken(null);
        } else {
            var base64 = token.split('.')[1].replace('-', '+').replace('_', '/'); //2nd string is payload, undo URL encoding
            var claimsObj = JSON.parse($window.atob(base64)); //convert base64 to text and parse as JSON
            TokenService.setToken(token);
            authService.username = claimsObj.sub; //subject is the username
        }
    };

    $rootScope.isAuthenticated = authService.isAuthenticated;

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
