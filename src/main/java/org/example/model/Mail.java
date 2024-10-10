package org.example.model;

public class Mail {

    private String subject;
    private String text;
    private String recipient;
    private String contentType;

    public Mail(String subject, String text, String recipient, String contentType) {
        this.subject = subject;
        this.text = text;
        this.recipient = recipient;
        this.contentType = contentType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
