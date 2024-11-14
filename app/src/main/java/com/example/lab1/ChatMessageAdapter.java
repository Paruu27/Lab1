package com.example.lab1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private List<ChatMessage> chatMessages;
    private OnDeleteMessageListener onDeleteMessageListener;

    public ChatMessageAdapter(List<ChatMessage> chatMessages, OnDeleteMessageListener onDeleteMessageListener) {
        this.chatMessages = chatMessages;
        this.onDeleteMessageListener = onDeleteMessageListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;
        private Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(ChatMessage message) {
            messageTextView.setText(message.getText()); // Use the getText method

            deleteButton.setOnClickListener(v -> {
                if (onDeleteMessageListener != null) {
                    onDeleteMessageListener.onDeleteMessage(message);
                }
            });
        }
    }

    public interface OnDeleteMessageListener {
        void onDeleteMessage(ChatMessage message);
    }
}
