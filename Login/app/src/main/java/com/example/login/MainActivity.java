package com.example.login;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private GoogleSignInClient client;
    ImageView googlebtn;
    ImageView fbbtn;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Facebook SDK with the App ID
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token));
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken!=null && accessToken.isExpired()==false){
            startActivity(new Intent(MainActivity.this, fbact.class));
            finish();
        }
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        showFacebookLoginSuccessfulNotification();
                        startActivity(new Intent(MainActivity.this, fbact.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        fbbtn = findViewById(R.id.fbbtn);
        fbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login with read permissions
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
            }
        });

        googlebtn = findViewById(R.id.googlebtn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            navigateToSecondActivity();
        }

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    // Handle the successful Google Sign-In
                    showLoginSuccessfulNotification();
                    navigateToSecondActivity();
                }
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Google Sign-In: Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        // Handle the Facebook callback
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void navigateToSecondActivity() {
        Intent intent = new Intent(MainActivity.this, home.class);
        startActivity(intent);
        finish();
    }

    private void showLoginSuccessfulNotification() {
        // Create a notification channel (for Android 8 and above)
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your icon resource
                .setContentTitle("Login Successful")
                .setContentText("You have successfully logged in with Google");

        // Create an Intent for the notification to launch the home activity
        Intent resultIntent = new Intent(this, home.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the content intent for the notification
        builder.setContentIntent(resultPendingIntent);

        // Get an instance of the NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notify using a unique ID to distinguish different notifications
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        // Create the notification channel for Android 8 and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Login Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Login notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showFacebookLoginSuccessfulNotification() {
        // Create a notification channel (for Android 8 and above)
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your Facebook notification icon resource
                .setContentTitle("Facebook Login Successful")
                .setContentText("You have successfully logged in with Facebook");

        // Create an Intent for the notification to launch the home activity
        Intent resultIntent = new Intent(this, home.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the content intent for the notification
        builder.setContentIntent(resultPendingIntent);

        // Get an instance of the NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notify using a unique ID to distinguish different notifications
        int notificationId = 2; // Use a different ID to distinguish from Google login notification
        notificationManager.notify(notificationId, builder.build());
    }
}