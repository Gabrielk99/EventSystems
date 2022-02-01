export interface EmailMessage {
    to: String,
    from: String | undefined,
    subject: String,
    text: String,
    html: String
}