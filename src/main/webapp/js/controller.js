(function () {
    var as = angular.module('exampleApp.controllers', []);

    as.controller('MainController', function ($q, $scope, $rootScope, $http, i18n, $location) {
        var load = function () {
        };

        load();

        $scope.language = function () {
            return i18n.language;
        };
        $scope.setLanguage = function (lang) {
            i18n.setLanguage(lang);
        };
        $scope.activeWhen = function (value) {
            return value ? 'active' : '';
        };

        $scope.path = function () {
            return $location.url();
        };

        $scope.logout = function () {
            $rootScope.user = null;
            $scope.username = $scope.password = null;
            $scope.$emit('event:logoutRequest');
            $location.url('/login');
        };

    });

    as.controller('LoginController', function ($scope, $rootScope, $http, base64, $location) {

        $scope.login = function () {
            console.log('username:password @' + $scope.username + ',' + $scope.password);
            $scope.$emit('event:loginRequest', $scope.username, $scope.password);
            // $('#login').modal('hide');
        };
    });

    as.controller('NewPostController', function ($scope, $http, i18n, $location) {
        var actionUrl = 'api/posts/';

        $scope.save = function () {
            $http.post(actionUrl, $scope.newPost).success(function () {
                $location.path('/posts');
            });
        };


        $scope.cancel = function () {
            $location.path('/posts');
        };

    });

    as.controller('DetailsController', function ($scope, $http, $routeParams, $q) {
        $scope.p = 1;
        var actionUrl = 'api/posts/',
                loadComments = function () {
                    $http.get(actionUrl + $routeParams.id + '/comments')
                            .success(function (data) {
                                $scope.comments = data.content;
                                $scope.totalItems = data.totalElements;
                            });
                },
                load = function () {
                    $q.all([
                        $http.get(actionUrl + $routeParams.id),
                        $http.get(actionUrl + $routeParams.id + '/comments')
                    ])
                            .then(function (result) {
                                $scope.post = result[0].data;
                                $scope.comments = result[1].data.content;
                                $scope.totalItems = result[1].data.totalElements;
                            });
                };

        load();

        $scope.newComment = {};

        $scope.save = function () {
            $http.post(actionUrl + $routeParams.id + '/comments', $scope.newComment).success(function () {
                $('#commentDialog').modal('hide');
                loadComments();
                $scope.newComment = {};
            });
        };

        $scope.delComment = function (idx) {
            $http.delete('api/comments/' + $scope.comments[idx].id).success(function () {
                $scope.comments.splice(idx, 1);
            });
        };

        $scope.addComment = function () {
            $('#commentDialog').modal('show');
        };

        $scope.search = function () {
            loadComments();
        };

    });

    as.controller('PostsController', function ($scope, $http, $location, i18n) {
        $scope.p = 1;
        $scope.q = '';
        $scope.statusOpt = {'label': $.i18n.prop('ALL'), 'value': 'ALL'};
        $scope.statusOpts = [
            {'label': $.i18n.prop('ALL'), 'value': 'ALL'},
            {'label': $.i18n.prop('DRAFT'), 'value': 'DRAFT'},
            {'label': $.i18n.prop('PUBLISHED'), 'value': 'PUBLISHED'}
        ];
        
        

        var actionUrl = 'api/posts/',
                load = function () {
                    $http.get(actionUrl + '?q=' + $scope.q
                            + '&status=' + ($scope.statusOpt.value == 'ALL' ? '' : $scope.statusOpt.value)
                            + '&page=' + ($scope.p - 1))
                            .success(function (data) {
                                $scope.posts = data.content;
                                $scope.totalItems = data.totalElements;
                            });
                };

        load();

        $scope.search = function () {
            load();
        };

        $scope.toggleStatus = function (r) {
            $scope.statusOpt = r;
        };
        
        $scope.add = function () {
            $location.path('/posts/new');
        };

        $scope.delPost = function (idx) {
            console.log('delete index @' + idx + ', id is@' + $scope.users[idx].id);
            if (confirm($.i18n.prop('confirm.delete'))) {
                $http.delete(actionUrl + $scope.posts[idx].id)
                        .success(function () {
                            $scope.posts.splice(idx, 1);
                        });
            }
        };

    });

}());