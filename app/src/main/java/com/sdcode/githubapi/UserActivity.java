package com.sdcode.githubapi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sdcode.githubapi.apiclasses.models.GitHubUser;
import com.sdcode.githubapi.viewmodels.UserViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserActivity extends AppCompatActivity {
    ImageView avatarImg;
    TextView userNameTV, followersTV, followingTV, logIn, email;
    Button ownedRepos;
    Bundle extras;
    Bitmap myImage;
    String newString;
    ProgressDialog progressDialog;
    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initUi();
        initData();

        extras = getIntent().getExtras();
        newString = extras.getString("STRING_I_NEED");

        System.out.println(newString);
        loadData();
    }

    private void initUi() {
        avatarImg = findViewById(R.id.avatar);
        userNameTV = findViewById(R.id.username);
        followersTV = findViewById(R.id.followers);
        followingTV = findViewById(R.id.following);
        logIn = findViewById(R.id.logIn);
        email = findViewById(R.id.email);
        ownedRepos = findViewById(R.id.ownedReposBtn);

        ownedRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, RepositoriesActivity.class);
                intent.putExtra("username", newString);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        progressDialog = new ProgressDialog(UserActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    public void loadData() {
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUserDataObserver().observe(this, new Observer<GitHubUser>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(GitHubUser gitHubUser) {
                if (gitHubUser != null) {

                    ImageDownloader task = new ImageDownloader();
                    try {
                        myImage = task.execute(gitHubUser.getAvatar()).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();

                    avatarImg.setImageBitmap(myImage);

                    if (gitHubUser.getName() == null) {
                        userNameTV.setText("No name provided");
                    } else {
                        userNameTV.setText("Username: " + gitHubUser.getName());
                    }

                    followersTV.setText("Followers: " + gitHubUser.getFollowers());
                    followingTV.setText("Following: " + gitHubUser.getFollowing());
                    logIn.setText("LogIn: " + gitHubUser.getLogin());

                    if (gitHubUser.getEmail() == null) {
                        email.setText("No email provided");
                    } else {
                        email.setText("Email: " + gitHubUser.getEmail());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewModel.makeApiCallUser(newString);
    }


    public static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

