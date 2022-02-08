"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var express_1 = require("express");
var ManagerRouter_1 = __importDefault(require("./manager/ManagerRouter"));
var VaccineRouter_1 = __importDefault(require("./vaccine/VaccineRouter"));
var EmailRouter_1 = __importDefault(require("./email/EmailRouter"));
var MasterRouter = /** @class */ (function () {
    function MasterRouter() {
        this._router = (0, express_1.Router)();
        this._managerR = ManagerRouter_1.default;
        this._vaccineR = VaccineRouter_1.default;
        this._emailR = EmailRouter_1.default;
        this._configure();
    }
    Object.defineProperty(MasterRouter.prototype, "router", {
        get: function () {
            return this._router;
        },
        enumerable: false,
        configurable: true
    });
    /**
     * Connect routes to their matching routers.
     */
    MasterRouter.prototype._configure = function () {
        this._router.use('/gestor', this._managerR);
        this._router.use('/vacina', this._vaccineR);
        this._router.use('/email', this._emailR);
    };
    return MasterRouter;
}());
module.exports = new MasterRouter().router;
