"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var datasSimulation_json_1 = __importDefault(require("../../../../Database/data_for_app/Vacinas/datasSimulation.json"));
var vacinas_json_1 = __importDefault(require("../../../../Database/data_for_app/Vacinas/vacinas.json"));
var VaccineController = /** @class */ (function () {
    function VaccineController() {
    }
    VaccineController.prototype.getVaccines = function () {
        return vacinas_json_1.default.vacinas;
    };
    VaccineController.prototype.getVaccine = function (id) {
        return vacinas_json_1.default.vacinas.find(function (vacina) { return vacina.id == id; });
    };
    VaccineController.prototype.getAllVaccinesStatus = function () {
        return datasSimulation_json_1.default;
    };
    VaccineController.prototype.getVaccineStatus = function (id) {
        return datasSimulation_json_1.default.filter(function (statusVacina) { return statusVacina.id == id; });
    };
    return VaccineController;
}());
module.exports = new VaccineController();
