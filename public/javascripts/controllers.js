'use strict';

/* Controllers */

var controllers = angular.module('controllers', []);

controllers.controller('DemoCtrl', ['$scope', 'ngTableParams', 'EventService',
  function($scope, ngTableParams, EventService) {
    var data = EventService.query(function() {
        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10           // count per page
        }, {
            total: data.length, // length of data
            getData: function($defer, params) {
                $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    });

  }]);
