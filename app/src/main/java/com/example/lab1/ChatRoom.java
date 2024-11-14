package com.example.lab1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoom extends AppCompatActivity {
    private static final String TAG = "ChatRoom";
    private ChatMessageDAO mDAO;
    private MessageDatabase db;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private Button sendButton, receiveButton;
    private EditText messageEditText;
    private ChatMessage recentlyDeletedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        recyclerView = findViewById(R.id.recyclerView);
        sendButton = findViewById(R.id.sendButton);
        receiveButton = findViewById(R.id.receiveButton);
        messageEditText = findViewById(R.id.messageEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "chat_database")
                .fallbackToDestructiveMigration()
                .build();
        mDAO = db.chatMessageDAO();

        sendButton.setOnClickListener(v -> {
            String messageText = messageEditText.getText().toString();
            if (!messageText.isEmpty()) {
                sendMessage("Sender", "Receiver", messageText);
                messageEditText.setText("");
            }
        });

        receiveButton.setOnClickListener(v -> {
            loadMessages();
        });

        myAdapter = new MyAdapter(messages, position -> {
            ChatMessage messageToDelete = messages.get(position);
            deleteMessage(messageToDelete);
        });
        recyclerView.setAdapter(myAdapter);
    }

    private void sendMessage(String sender, String receiver, String messageText) {
        long timestamp = System.currentTimeMillis();
        ChatMessage newMessage = new ChatMessage(sender, receiver, messageText, "senderDp", "receiverDp", timestamp);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mDAO.insertMessage(newMessage);
            runOnUiThread(this::loadMessages);
        });
    }

    private void deleteMessage(ChatMessage message) {
        recentlyDeletedMessage = message;
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mDAO.deleteMessage(message);
            runOnUiThread(() -> {
                loadMessages();
                Snackbar.make(recyclerView, "Message deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> undoDelete())
                        .show();
            });
        });
    }

    private void undoDelete() {
        if (recentlyDeletedMessage != null) {
            sendMessage(recentlyDeletedMessage.sender, recentlyDeletedMessage.receiver,
                    recentlyDeletedMessage.message);
        }
    }

    private void loadMessages() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<ChatMessage> allMessages = mDAO.getAllMessages();
            runOnUiThread(() -> {
                messages.clear();
                messages.addAll(allMessages);
                myAdapter.notifyDataSetChanged();
            });
        });
    }
}
