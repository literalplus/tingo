//TODO: Split into multiple files
var AuthController = ['$rootScope', 'AuthService', 'AUTH_EVENTS',
    function ($rootScope, AuthService, AUTH_EVENTS) {
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
    }];

var HomeController = ['$http', '$rootScope', 'AuthService',
    function ($http, $rootScope, AuthService) {
        var ctrl = this;
        this.teachers = {};

        AuthService.
        (function () {
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
    }];

var RegisterController = function () {

};

var LoginController = ['$stateParams', '$rootScope', '$location',
    function ($stateParams, $rootScope, $location) {
        this.back = function () {
            if ($rootScope.returnto !== null) {
                $location.path($rootScope.returnto);
                $rootScope.returnto = null;
            } else {
                $location.path('/');
            }
        };
    }];

var TeacherDetailController = ['TeacherDetailService', '$stateParams',
    function (TeacherDetailService, $stateParams) {
        this.getFields = TeacherDetailService.getFields;
        this.getTeacher = TeacherDetailService.getTeacher;

        this.saveField = TeacherDetailService.saveField;
        this.uneditField = TeacherDetailService.uneditField;
        this.editField = TeacherDetailService.editField;
        this.addField = TeacherDetailService.addField;
        this.deleteField = TeacherDetailService.deleteField;
        this.isFetched = TeacherDetailService.isFetched;

        TeacherDetailService.fetchTeacher($stateParams.id);
    }];

var NavDataController = ['$http', 'AuthService',
    function ($http, AuthService) {
        var ctrl = this;
        this.teachers = {};
        this.teachersLoaded = false;

        AuthService.onAuthChange(function () {
            $http.get('/api/teacher/list')
                .success(function (data) {
                    ctrl.teachers = data;
                    ctrl.teachersLoaded = true;
                });
        }, function () {
            ctrl.teachersLoaded = false;
            ctrl.teachers = {};
        });
    }];

var tingoApp = angular.module('tingo', ['ui.router', 'ui.bootstrap', 'ngCookies']);

tingoApp.factory('AuthInterceptor', ['$q', 'TokenService', '$location', '$rootScope',
    function ($q, TokenService, $location, $rootScope) {
        var authInterceptor = {};

        authInterceptor.request = function (config) {
            if (TokenService.hasToken()) {
                config.headers.Authorization = 'Bearer ' + TokenService.getToken();
            }
            return config;
        };

        authInterceptor.responseError = function (response) {
            if (response.status === 401) {
                console.info("401 encountered");
                TokenService.setToken(null);
                $rootScope.returnto = $location.path(); //save current path for login back
                $location.path('/login');
            }
            return $q.reject(response);
        };

        return authInterceptor;
    }]);

tingoApp.config(['$stateProvider', '$urlRouterProvider', '$urlMatcherFactoryProvider', '$httpProvider',
    function ($stateProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $httpProvider) {
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
    }]);

tingoApp.factory('TeacherDetailService', ['$http',
    function ($http) {
        var detailService = {};
        detailService.teacher = {name: 'Loading..', abbreviation: '....'};
        detailService.fields = [];
        var fetched = false;
        var backupField = {};

        detailService.saveField = function (field) {
            var myIndex = _.indexOf(detailService.fields, field);
            if (field.text.length === 0 || !field.text.trim()) {
                detailService.fields.splice(myIndex);
                return;
            }

            $http.post('/api/field/save', field)
                .then(function (response) {
                    detailService.fields[myIndex] = response.data;
                }, function (response) {
                    console.warn('Couldn\'t save fields: ');
                    console.warn(response);
                    alert('Fehler beim Speichern: ' + response.data.errorMessage);
                });
        };

        detailService.uneditField = function (field) {
            var myIndex = _.indexOf(detailService.fields, field);
            backupField.editing = false;
            detailService.fields[myIndex] = backupField;
        };

        detailService.editField = function (field) {
            backupField = _.clone(field);
            field.editing = true;
        };

        detailService.addField = function () {
            detailService.fields.push({text: '', teacherId: detailService.teacher.id, editing: true});
        };

        detailService.deleteField = function (field) {
            $http.post('/api/field/delete', field)
                .then(function (response) {
                    detailService.fields = _.without(detailService.fields, field);
                }, function (response) {
                    console.warn('Couldn\'t delete field: ');
                    console.warn(response);
                    alert('Fehler beim Löschen: ' + response.data.errorMessage);
                });
        };

        detailService.fetchTeacher = function (id) {
            fetched = false;
            $http.get('/api/field/by/teacher/' + id)
                .success(function (data) {
                    detailService.teacher = data.teacher;
                    detailService.fields = data.fields;
                    fetched = true;
                });
        };

        // Getters and Setters
        detailService.isFetched = function () {
            return fetched;
        };

        detailService.getFields = function () {
            return detailService.fields;
        };

        detailService.getTeacher = function () {
            return detailService.teacher;
        };

        return detailService;
    }]);

tingoApp.factory('TokenService', ['$window', '$cookies',
    function ($window, $cookies) {
        var tokenService = {};
        var token = null;
        var cookieOpts = {
            path: '/',
            //secure: true, //doesn't work with local HTTP-only debug; TODO
            expires: new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 15)) /* 15 days */
        };

        tokenService.setToken = function (newToken) {
            token = newToken;

            if (!!token) {
                $window.localStorage.setItem('tingo-token', token);
                $cookies.put('tingo-token-cookie', token, cookieOpts);

            } else {
                $window.localStorage.removeItem('tingo-token');
                $cookies.remove('tingo-token-cookie', cookieOpts);
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
    }]);

tingoApp.factory('AuthService', ['$http', '$rootScope', '$location', 'AUTH_EVENTS', '$window', 'TokenService',
    function ($http, $rootScope, $location, AUTH_EVENTS, $window, TokenService) {
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

                if (callback) {
                    callback(authService.authenticated, response.data);
                }
            }, function () {
                resetAuth();
                if (callback) {
                    callback(false, {});
                }
            });
        };

        authService.login = function (credentials) {
            authenticate(credentials,
                function (authSuccess, data) {
                    authService.error = !authSuccess;
                    if (authSuccess) {
                        $rootScope.$broadcast(AUTH_EVENTS.login_success, data);
                        if ($rootScope.returnto !== null) {
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
            resetAuth();
            $location.path('/');
            $rootScope.$broadcast(AUTH_EVENTS.logout);
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
            return TokenService.hasToken() && authService.authenticated;
        };

        authService.runWhenAuthenticated = function (callback) { //this is kind of deprecated, use #onAuthChange(...)
            onAuthChange(callback, null);
        };

        authService.onAuthChange = function (onLogin, onLogout) {
            if (onLogin) {
                if (authService.isAuthenticated()) {
                    onLogin();
                }
                //Call again on re-authentication
                $rootScope.$on(AUTH_EVENTS.login_success, onLogin);
            }

            if (onLogout) {
                if(!authService.isAuthenticated()) {
                    onLogout();
                }
                $rootScope.$on(AUTH_EVENTS.logout, onLogout);
            }
        };

        authService.isAuthenticatedSafe = function (callback) {
            callback(authService.isAuthenticated());
        };

        authService.getUserName = function () {
            return !!authService.username ? authService.username : 'Anon';
        };

        authService.setToken = function (token) {
            authService.token = token;
            if (token === null) {
                authService.username = null;
                TokenService.setToken(null);
                $rootScope.authenticated = authService.authenticated = false;
            } else {
                var base64 = token.split('.')[1].replace('-', '+').replace('_', '/'); //2nd string is payload, undo URL encoding
                var claimsObj = JSON.parse($window.atob(base64)); //convert base64 to text and parse as JSON
                TokenService.setToken(token);
                authService.username = claimsObj.sub; //subject is the username
                $rootScope.authenticated = authService.authenticated = true;
            }
        };

        $rootScope.isAuthenticated = authService.isAuthenticated;

        setAuth(TokenService.getToken());

        return authService;
    }]);

tingoApp.run(['$rootScope', '$state', '$location', 'AuthService',
    function ($rootScope, $state, $location, AuthService) { //just inject so it gets initialised
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
    }]);

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
