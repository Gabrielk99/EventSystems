"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.sendEmail = exports.setSendEmail = exports.setEmailSender = void 0;
var dotenv_1 = __importDefault(require("dotenv"));
var Vaccine_1 = require("../models/Vaccine");
var Geocoding_1 = require("./Geocoding");
dotenv_1.default.config({
    path: '.env'
});
var axios = require('axios');
var emailSendUrl = 'https://api.sendgrid.com/v3/mail/send';
var key = 0;
var send = false;
var getTemplateId = function (status, isFrequentAlert) {
    var templateId = 'batata';
    //TODO: colocar id do template pra alerta frequente
    switch (status) {
        case Vaccine_1.Status.warning:
            if (key === 0) {
                templateId = isFrequentAlert ? "d-607f444d07d34fc68af45d6016698a21" : "d-6463b62cb655495ebf3fff00eefc7ebf";
            }
            else {
                templateId = isFrequentAlert ? "d-d833e21be95b498a96d11e36a967d1b5" : "d-537ac74b7c2048d1ad122d08e96273dd";
            }
            break;
        case Vaccine_1.Status.danger:
            if (key === 0) {
                templateId = isFrequentAlert ? "d-a289ccd851c940dc885cd1ab5e666ace" : "d-5e77f1ed29fd4e16a0f39e3b5853eb9e";
            }
            else {
                templateId = isFrequentAlert ? 'd-09b7ee5a44c148e9a667db506ec5ba52' : 'd-7896e840032c4c2b8e6c57c2119ced2c';
            }
            break;
        case Vaccine_1.Status.gameover:
            if (key === 0) {
                templateId = "d-fb176167feb94e569aaf3934b6e3e84d";
            }
            else {
                templateId = "d-5f90e91b9b4e40d294c7781f6087c296";
            }
            break;
        default:
            break;
    }
    return templateId;
};
var setEmailSender = function (value) {
    console.log("antes: " + key);
    key = value;
    console.log("depois: " + key);
    console.log("===================");
};
exports.setEmailSender = setEmailSender;
var setSendEmail = function (value) {
    console.log("old: ", send);
    console.log(value);
    send = value;
    console.log("new: ", send);
};
exports.setSendEmail = setSendEmail;
var sendEmail = function (email) { return __awaiter(void 0, void 0, void 0, function () {
    var sendgridApiKey, sender, message, _a, _b, _c, res, err_1;
    var _d, _e, _f;
    return __generator(this, function (_g) {
        switch (_g.label) {
            case 0:
                if (!send) return [3 /*break*/, 6];
                sendgridApiKey = key === 0 ? process.env.SENDGRID_API_KEY : process.env.SENDGRID_API_KEY2;
                sender = key === 0 ? process.env.EMAIL_SENDER : process.env.EMAIL_SENDER2;
                _g.label = 1;
            case 1:
                _g.trys.push([1, 4, , 5]);
                _d = {};
                _a = 'personalizations';
                _e = {
                    'to': [
                        {
                            'email': email.to
                        }
                    ],
                    'subject': email.subject
                };
                _b = "dynamic_template_data";
                _f = {
                    "name": email.vaccine.name,
                    "lat": email.location.latitude,
                    "long": email.location.longitude,
                    "manager": email.manager
                };
                _c = "address";
                return [4 /*yield*/, (0, Geocoding_1.getAddressFromLatLong)(email.location.latitude, email.location.longitude)];
            case 2:
                message = (_d[_a] = [
                    (_e[_b] = (_f[_c] = _g.sent(),
                        _f),
                        _e)
                ],
                    _d['from'] = {
                        'email': sender
                    },
                    _d["template_id"] = getTemplateId(email.status, email.isFrequentAlert),
                    _d);
                return [4 /*yield*/, axios({
                        method: 'post',
                        url: emailSendUrl,
                        headers: {
                            Authorization: "Bearer ".concat(sendgridApiKey),
                        },
                        data: message,
                    })];
            case 3:
                res = _g.sent();
                return [2 /*return*/, res];
            case 4:
                err_1 = _g.sent();
                console.log(err_1);
                return [2 /*return*/, { status: 'error', message: "limite de email batido" }];
            case 5: return [3 /*break*/, 7];
            case 6: return [2 /*return*/, { status: "success", message: "nenhum email foi enviado, o envio está bloqueado." }];
            case 7: return [2 /*return*/];
        }
    });
}); };
exports.sendEmail = sendEmail;
