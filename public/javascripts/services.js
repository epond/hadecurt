'use strict';

/* Services */

var services = angular.module('services', ['ngResource']);

services.factory('EventService', ['$resource',
  function($resource){
    return $resource('events/:eventId', {}, {
      query: {method:'GET', isArray:true}
    });
  }]);