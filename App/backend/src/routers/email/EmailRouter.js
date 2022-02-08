"use strict";
var express_1 = require("express");
var Email_1 = require("../../helper/Email");
var EmailRouter = /** @class */ (function () {
    function EmailRouter() {
        this._router = (0, express_1.Router)();
        this._configure();
    }
    Object.defineProperty(EmailRouter.prototype, "router", {
        get: function () {
            return this._router;
        },
        enumerable: false,
        configurable: true
    });
    EmailRouter.prototype._configure = function () {
        this._router.post('/send', function (req, res, next) {
            var result = (0, Email_1.sendEmail)(req.body.message);
            res.status(200).json(result);
        });
    };
    return EmailRouter;
}());
module.exports = new EmailRouter().router;
