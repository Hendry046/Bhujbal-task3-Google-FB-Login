package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

public class fbact extends AppCompatActivity {

    TextView name;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbact);

        name = findViewById(R.id.textview2);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if (accessToken != null && !accessToken.isExpired()) {
//            // User is logged in with a valid access token, show 'fbact'
//            startActivity(new Intent(fbact.this, fbact.class)); // Change 'MainActivity' to 'fbact'
//        } else {
//            // User is not logged in, show 'MainActivity'
//            startActivity(new Intent(fbact.this, MainActivity.class));
//        }
//        finish();

//        GraphRequest request = GraphRequest.newMeRequest(
//                accessToken,
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(
//                            JSONObject object,
//                            GraphResponse response) {
//
//                        try {
//                            String fullName = object.getString("name");
//                            name.setText(fullName);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        // Application code
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,link");
//        request.setParameters(parameters);
//        request.executeAsync();

        logout = findViewById(R.id.button2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(fbact.this,MainActivity.class));
                finish();
            }
        });
    }
}