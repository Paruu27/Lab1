package com.example.lab1;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version = 1, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {
    public abstract ChatMessageDAO chatMessageDAO();
}
