"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var express_1 = require("express");
var VaccineController_1 = __importDefault(require("../../controllers/VaccineController"));
var VaccineRouter = /** @class */ (function () {
    function VaccineRouter() {
        this._router = (0, express_1.Router)();
        this._controller = VaccineController_1.default;
        this._configure();
    }
    Object.defineProperty(VaccineRouter.prototype, "router", {
        get: function () {
            return this._router;
        },
        enumerable: false,
        configurable: true
    });
    /**
     * Connect routes to their matching controller endpoints.
     */
    VaccineRouter.prototype._configure = function () {
        var _this = this;
        this._router.get('/status/:id', function (req, res, next) {
            res.status(200).json(_this._controller.getVaccineStatus(Number(req.params.id)));
        });
        this._router.get('/status', function (req, res, next) {
            res.status(200).json(_this._controller.getAllVaccinesStatus());
        });
        this._router.get('/:id', function (req, res, next) {
            res.status(200).json(_this._controller.getVaccine(Number(req.params.id)));
        });
        this._router.get('/', function (req, res, next) {
            res.status(200).json(_this._controller.getVaccines());
        });
    };
    return VaccineRouter;
}());
module.exports = new VaccineRouter().router;
