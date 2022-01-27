"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var datasSimulation_json_1 = __importDefault(require("../../../../Database/data_for_app/Gestores/datasSimulation.json"));
var gestores_json_1 = __importDefault(require("../../../../Database/data_for_app/Gestores/gestores.json"));
var ManagerController = /** @class */ (function () {
    function ManagerController() {
    }
    ManagerController.prototype.getManagers = function () {
        return gestores_json_1.default.gestores;
    };
    ManagerController.prototype.getManager = function (id) {
        return gestores_json_1.default.gestores.find(function (gestor) { return gestor.id == id; });
    };
    ManagerController.prototype.getManagersLocation = function () {
        return datasSimulation_json_1.default;
    };
    ManagerController.prototype.getManagerLocation = function (id) {
        return datasSimulation_json_1.default.find(function (gestor) { return gestor.id == id; });
    };
    return ManagerController;
}());
module.exports = new ManagerController();
