"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var express_1 = require("express");
var ManagerController_1 = __importDefault(require("../../controllers/ManagerController"));
var ManagerRouter = /** @class */ (function () {
    function ManagerRouter() {
        this._router = (0, express_1.Router)();
        this._controller = ManagerController_1.default;
        this._configure();
    }
    Object.defineProperty(ManagerRouter.prototype, "router", {
        get: function () {
            return this._router;
        },
        enumerable: false,
        configurable: true
    });
    /**
     * Connect routes to their matching controller endpoints.
     */
    ManagerRouter.prototype._configure = function () {
        var _this = this;
        this._router.get('/localizacao', function (req, res, next) {
            res.status(200).json(_this._controller.getManagersLocation());
        });
        this._router.get('/localizacao/:id', function (req, res, next) {
            res.status(200).json(_this._controller.getManagerLocation(Number(req.params.id)));
        });
        this._router.get('/', function (req, res, next) {
            res.status(200).json(_this._controller.getManagers());
        });
        this._router.get('/:id', function (req, res, next) {
            res.status(200).json(_this._controller.getManager(Number(req.params.id)));
        });
    };
    return ManagerRouter;
}());
module.exports = new ManagerRouter().router;
