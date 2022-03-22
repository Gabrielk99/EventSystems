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
            var result = (0, Email_1.sendEmail)(req.body);
            res.status(200).json(result);
        });
        this._router.post('/setKey/:key', function (req, res, next) {
            (0, Email_1.setEmailSender)(Number(req.params.key));
            res.status(200);
        });
        this._router.post('/setSend', function (req, res, next) {
            (0, Email_1.setSendEmail)(JSON.parse(req.body.send));
            res.status(200);
        });
    };
    return EmailRouter;
}());
module.exports = new EmailRouter().router;
