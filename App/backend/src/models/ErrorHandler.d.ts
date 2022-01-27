export default class ErrorHandler extends Error {
    statusCode: number;
    message: string;
    constructor(statusCode: number, message: string);
}
