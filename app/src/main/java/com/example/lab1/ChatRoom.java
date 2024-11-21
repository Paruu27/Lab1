package com.example.lab1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoom extends AppCompatActivity {
    private static final String TAG = "ChatRoom";
    private ChatMessageDAO mDAO;
    private MessageDatabase db;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatMessageAdapter myAdapter;
    private Button sendButton, receiveButton;
    private EditText messageEditText;
    private ChatMessage recentlyDeletedMessage;
    private boolean isTablet;

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

        // Determine if the device is a tablet
        isTablet = findViewById(R.id.fragmentLocation) != null;
        Log.d(TAG, "Device type detected: " + (isTablet ? "Tablet" : "Phone"));

        // Load existing messages from the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            messages.addAll(mDAO.getAllMessages());
            Log.d(TAG, "Loaded messages on startup: " + messages.size());
            runOnUiThread(() -> {
                myAdapter = new ChatMessageAdapter(messages, this::onMessageClicked);
                recyclerView.setAdapter(myAdapter);
            });
        });

        // Handle send button click
        sendButton.setOnClickListener(v -> {
            String text = messageEditText.getText().toString();
            if (text.isEmpty()) return; // Prevent sending empty messages

            long timestamp = System.currentTimeMillis();
            ChatMessage newMessage = new ChatMessage(text, timestamp, false); // "false" means sent message
            addMessageToViewAndDatabase(newMessage);
        });

        // Handle receive button click
        receiveButton.setOnClickListener(v -> {
            String text = messageEditText.getText().toString();
            if (text.isEmpty()) return;

            long timestamp = System.currentTimeMillis();
            ChatMessage newMessage = new ChatMessage(text, timestamp, true); // "true" means received message
            addMessageToViewAndDatabase(newMessage);
        });
    }

    private void addMessageToViewAndDatabase(ChatMessage newMessage) {
        messages.add(newMessage);
        myAdapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            mDAO.insertMessage(newMessage);
            Log.d(TAG, "Message inserted into database: " + newMessage.getMessageText());
        });

        messageEditText.setText("");
    }

    private void onMessageClicked(int position) {
        ChatMessage selectedMessage = messages.get(position);
        Log.d(TAG, "Message clicked: " + selectedMessage.getMessageText());

        MessageDetailsFragment detailsFragment = MessageDetailsFragment.newInstance(selectedMessage);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (isTablet) {
            // For tablets, load the fragment on the right
            transaction.replace(R.id.fragmentLocation, detailsFragment)
                    .commit();
        } else {
            // For phones, load the fragment over the RecyclerView
            transaction.replace(R.id.recyclerView, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Message")
                .setMessage("Do you want to delete this message?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    recentlyDeletedMessage = messages.get(position);
                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        mDAO.deleteMessage(recentlyDeletedMessage);
                        Log.d(TAG, "Message deleted from database: " + recentlyDeletedMessage.getMessageText());
                    });

                    showUndoSnackbar();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showUndoSnackbar() {
        Snackbar.make(recyclerView, "Message deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> {
                    // Reinsert message into the list if recentlyDeletedMessage is not null
                    if (recentlyDeletedMessage != null) {
                        messages.add(recentlyDeletedMessage);
                        myAdapter.notifyItemInserted(messages.size() - 1);
                        recyclerView.scrollToPosition(messages.size() - 1);

                        // Reinsert message into the database
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            mDAO.insertMessage(recentlyDeletedMessage);
                            Log.d(TAG, "Message reinserted into database: " + recentlyDeletedMessage.getMessageText());
                        });
                    }
                }).show();
    }
}
