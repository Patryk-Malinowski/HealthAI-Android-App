// Patryk Malinowski
// R00210173
// HealthAI Android App


/*

Followed tutorial by "Easy Tuto"

Youtube Link: https://www.youtube.com/watch?v=ahhze_u5ZUs

 */

package com.example.healthai;

public class Message {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    String message;
    String sentBy;

    public Message(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}