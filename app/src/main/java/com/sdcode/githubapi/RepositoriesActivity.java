package com.sdcode.githubapi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdcode.githubapi.adapters.ReposAdapter;
import com.sdcode.githubapi.apiclasses.models.GitHubRepo;
import com.sdcode.githubapi.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class RepositoriesActivity extends AppCompatActivity {
    String receivedUserName;
    TextView userNameTV;
    RecyclerView mRecyclerView;
    List<GitHubRepo> myDataSource = new ArrayList<>();
    ProgressDialog progressDialog;
    RecyclerView.Adapter<ReposAdapter.ReposViewHolder> myAdapter;
    UserViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        initData();

        Bundle extras = getIntent().getExtras();
        receivedUserName = extras.getString("username");

        userNameTV = findViewById(R.id.userNameTV);
        userNameTV.setText("User: " + receivedUserName);

        mRecyclerView = findViewById(R.id.repos_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ReposAdapter(myDataSource, R.layout.list_item_repo, getApplicationContext());
        mRecyclerView.setAdapter(myAdapter);
        loadRepositories();
    }

    private void initData() {
        progressDialog = new ProgressDialog(RepositoriesActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(() -> {
            try {
                Thread.sleep(10000000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }).start();
    }

    public void loadRepositories() {

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getRepoDataObserver().observe(this, new Observer<List<GitHubRepo>>() {
            @Override
            public void onChanged(List<GitHubRepo> gitHubRepos) {
                if (gitHubRepos != null) {
                    progressDialog.dismiss();
                    myDataSource.clear();
                    myDataSource.addAll(gitHubRepos);
                    myAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewModel.makeApiCallRepo(receivedUserName);
    }
}

