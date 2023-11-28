// Patryk Malinowski
// R00210173
// HealthAI Android App
/*

Followed tutorial by "Easy Tuto"
Youtube Link: https://www.youtube.com/watch?v=ahhze_u5ZUs

*/

package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton, backBtn, homeBtn;
    FloatingActionButton logoutBtn;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        homeBtn = findViewById(R.id.homeBtn2);
        backBtn = findViewById(R.id.backBtn2);
        logoutBtn = findViewById(R.id.logoutBtn2);

        // Setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        // Set onClickListener for the send button
        sendButton.setOnClickListener((v) -> {
            // Get the user's question
            String question = messageEditText.getText().toString().trim();
            // Add user's message to the chat
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            // Make a call to the OpenAI API
            callURL(question);
            welcomeTextView.setVisibility(View.GONE);
        });

        backBtn.setOnClickListener(view -> finish());

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ChatbotActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuthentication.SignOut(this);

            // Navigate back to the LogInActivity
            Intent intent = new Intent(ChatbotActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }


    // Method to add a message to the chat
    @SuppressLint("NotifyDataSetChanged")
    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            // Add the message to the list
            messageList.add(new Message(message, sentBy));
            // Notify the adapter that the data set has changed
            messageAdapter.notifyDataSetChanged();
            // Scroll to the latest message
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    // Method to add a response from the chatbot
    void addResponse(String response) {
        // Remove the "Typing..." message and add the actual response
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    // Method to make a call to the server with the user's question
    void callURL(String question) {
        String url = "https://healthai-heroku-1a596fab2241.herokuapp.com/api/ask-gpt3";

        // Add a "..." message to indicate that the chatbot is processing
        messageList.add(new Message("... ", Message.SENT_BY_BOT));

        // Construct the JSON body for the API request
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("input", question);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the request and enqueue it
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // If the URL call fails, add an error response to the chat
                addResponse("Failed due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    // Parse the response and extract the content
                    String result = response.body().string();

                    try {
                        // Assuming the response is a JSON object with a key "answer"
                        JSONObject jsonResponse = new JSONObject(result);
                        String answer = jsonResponse.getString("answer");

                        // Add the response to the chat
                        addResponse(answer.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                        addResponse("Failed to parse response: " + result);
                    }
                } else {
                    // If the URL call is not successful, add an error response to the chat
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }

}