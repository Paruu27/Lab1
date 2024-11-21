package com.example.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageDetailsFragment extends Fragment {
    private static final String ARG_ID = "id";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_IS_RECEIVED = "isReceived";
    private static final String ARG_TIMESTAMP = "timestamp";

    public MessageDetailsFragment() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of the fragment with arguments
    public static MessageDetailsFragment newInstance(ChatMessage message) {
        MessageDetailsFragment fragment = new MessageDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, message.getId());
        args.putString(ARG_MESSAGE, message.getMessageText());
        args.putBoolean(ARG_IS_RECEIVED, message.isReceiveMessage());
        args.putLong(ARG_TIMESTAMP, message.getTimestamp());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.details_layout, container, false);

        // Initialize the TextViews
        TextView dbIdTextView = view.findViewById(R.id.dbId);
        TextView messageTextView = view.findViewById(R.id.message);
        TextView isSentOrReceiveTextView = view.findViewById(R.id.isSentOrReceive);
        TextView timeSentTextView = view.findViewById(R.id.timeSent);

        // Extract arguments from the Bundle
        if (getArguments() != null) {
            long id = getArguments().getLong(ARG_ID, -1);
            String messageText = getArguments().getString(ARG_MESSAGE, "No message");
            boolean isReceived = getArguments().getBoolean(ARG_IS_RECEIVED, false);
            long timestamp = getArguments().getLong(ARG_TIMESTAMP, 0);

            // Set the values in TextViews
            dbIdTextView.setText("Database ID: " + id);
            messageTextView.setText("Message: " + messageText);
            String sentStatus = isReceived ? "Received" : "Sent";
            isSentOrReceiveTextView.setText("Sent/Received: " + sentStatus);

            // Format and display timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedTime = sdf.format(new Date(timestamp));
            timeSentTextView.setText("Time Sent: " + formattedTime);
        } else {
            // Handle the case where arguments are missing
            dbIdTextView.setText("Database ID: Not available");
            messageTextView.setText("Message: Not available");
            isSentOrReceiveTextView.setText("Sent/Received: Unknown");
            timeSentTextView.setText("Time Sent: Unknown");
        }

        return view;
    }
}
