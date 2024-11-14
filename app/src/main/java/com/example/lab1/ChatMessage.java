package com.example.lab1;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "chat_messages")
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "sender")
    public String sender;

    @ColumnInfo(name = "receiver")
    public String receiver;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "sender_dp")
    public String senderDp;

    @ColumnInfo(name = "receiver_dp")
    public String receiverDp;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    // Constructor
    public ChatMessage(String sender, String receiver, String message, String senderDp, String receiverDp, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.senderDp = senderDp;
        this.receiverDp = receiverDp;
        this.timestamp = timestamp;
    }

    // Getters
    public String getMessageText() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getText() {
        return 0;
    }

    // Other necessary getters and setters...
}
