"use strict";
var fs = require('fs');
var pathToLocations = __dirname.replace("App/backend/src/controllers", "Database/data_for_app/Gestores/datasSimulation.json");
var pathToGestoresInfo = __dirname.replace("App/backend/src/controllers", "Database/data_for_app/Gestores/gestores.json");
var ManagerController = /** @class */ (function () {
    function ManagerController() {
    }
    ManagerController.prototype.getManagers = function () {
        var data = fs.readFileSync(pathToGestoresInfo);
        var gestores = JSON.parse(data).gestores;
        return gestores;
    };
    ManagerController.prototype.getManager = function (id) {
        var data = fs.readFileSync(pathToGestoresInfo);
        var gestores = JSON.parse(data).gestores;
        return gestores.find(function (gestor) { return gestor.id == id; });
    };
    ManagerController.prototype.getManagersLocation = function () {
        var data = fs.readFileSync(pathToLocations);
        var locations = JSON.parse(data);
        return locations;
    };
    ManagerController.prototype.getManagerLocation = function (id) {
        var data = fs.readFileSync(pathToLocations);
        var locations = JSON.parse(data);
        return locations.find(function (gestor) { return gestor.id == id; });
    };
    return ManagerController;
}());
module.exports = new ManagerController();
