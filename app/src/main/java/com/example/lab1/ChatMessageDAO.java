package com.example.lab1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Query("SELECT * FROM chat_messages")
    List<ChatMessage> getAllMessages();

    @Insert
    void insertMessage(ChatMessage message);

    @Delete
    void deleteMessage(ChatMessage message);
}
