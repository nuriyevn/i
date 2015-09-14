'use strict';

angular.module('dashboardJsApp')
  .controller('ReportsCtrl', function ($scope, $timeout, Modal, reports, processes) {
    $scope.export = {};
    $scope.export.from = '2015-06-01';
    $scope.export.to = '2015-08-01';
    $scope.export.sBP = 'dnepr_spravka_o_doxodax';

    $scope.exportLink = function () {
      reports.exportLink({ from: $scope.export.from, to: $scope.export.to, sBP: $scope.export.sBP}, 
      function (result, fileToSave) {
        var hiddenElement = document.createElement('a');
        hiddenElement.href = 'data:text/csv;charset=windows-1251,' + encodeURI(result);
        hiddenElement.target = '_blank';
        hiddenElement.download = fileToSave;
        hiddenElement.click();
      });
    };

    $scope.statistic = {};
    $scope.statistic.from = '2015-06-01';
    $scope.statistic.to = '2015-08-01';
    $scope.statistic.sBP = 'dnepr_spravka_o_doxodax';

    processes.getUserProcesses().then(function (data) {
      $scope.processesList = data;
      if ($scope.processesList.length > 0) {
        $scope.statistic.sBP = $scope.processesList[0].sID;
        $scope.export.sBP = $scope.processesList[0].sID;
      }
    });
    
    $scope.processesLoaded = function() {
      if ($scope.processesList)
      return true;
    return false;
    }
    
    $scope.statisticLink = function () {
      reports.statisticLink({ from: $scope.statistic.from, to:  $scope.statistic.to, sBP: $scope.statistic.sBP}, 
      function (result, fileToSave) {
        var hiddenElement = document.createElement('a');
        hiddenElement.href = 'data:text/csv;charset=UTF-8,' + encodeURI(result);
        hiddenElement.target = '_blank';
        hiddenElement.download = fileToSave;
        hiddenElement.click();
      });
    };

  });
