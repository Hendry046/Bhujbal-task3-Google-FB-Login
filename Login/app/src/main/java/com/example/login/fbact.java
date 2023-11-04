package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class fbact extends AppCompatActivity {

    ImageView imageView;
    TextView name;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbact);

        imageView = findViewById(R.id.imageview);
        name = findViewById(R.id.name);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();


        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                if (object != null) {
//                                    String fullName = object.optString("name", "Name not available");
//                                    name.setText(fullName);

                                    if (object.has("picture")) {
                                        JSONObject pictureData = object.getJSONObject("picture").optJSONObject("data");
                                        if (pictureData != null) {
                                            String url = pictureData.optString("url");
                                            Picasso.get().load(url).into(imageView);
                                        }
                                    }
                                } else {
//                                    name.setText("Name not available");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                name.setText("Error occurred");
                            }
                            // Your application-specific code here
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }

        logout = findViewById(R.id.button2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(fbact.this, MainActivity.class));
                finish();
            }
        });
    }
}
