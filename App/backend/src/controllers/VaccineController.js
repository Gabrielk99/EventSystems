"use strict";
var fs = require('fs');
var pathToStatus = __dirname.replace("App/backend/src/controllers", "Database/data_for_app/Vacinas/datasSimulation.json");
var pathToVacinasInfo = __dirname.replace("App/backend/src/controllers", "Database/data_for_app/Vacinas/vacinas.json");
var VaccineController = /** @class */ (function () {
    function VaccineController() {
    }
    VaccineController.prototype.getVaccines = function () {
        var data = fs.readFileSync(pathToVacinasInfo);
        var vacinasInfo = JSON.parse(data).vacinas;
        return vacinasInfo;
    };
    VaccineController.prototype.getVaccine = function (id) {
        var data = fs.readFileSync(pathToVacinasInfo);
        var vacinasInfo = JSON.parse(data).vacinas;
        return vacinasInfo.find(function (vacina) { return vacina.id == id; });
    };
    VaccineController.prototype.getAllVaccinesStatus = function () {
        var data = fs.readFileSync(pathToStatus);
        try {
            var status_1 = JSON.parse(data);
            return status_1;
        }
        catch (err) {
            return {
                status: 'error',
                message: err.message
            };
        }
    };
    VaccineController.prototype.getVaccineStatus = function (id) {
        var data = fs.readFileSync(pathToStatus);
        var status = JSON.parse(data);
        return status.find(function (statusVacina) { return statusVacina.id == id; });
    };
    return VaccineController;
}());
module.exports = new VaccineController();
