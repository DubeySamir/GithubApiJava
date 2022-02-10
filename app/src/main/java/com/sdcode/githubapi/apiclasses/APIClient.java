package com.sdcode.githubapi.apiclasses;

import com.sdcode.githubapi.apiclasses.models.GitHubRepo;
import com.sdcode.githubapi.apiclasses.models.GitHubUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class APIClient {
//    User Info - https://api.github.com//users/ssdcode
    public static final String BASE_URL = "https://api.github.com/";
    private static GitHubService gitHubService = null;

    public static GitHubService getService() {
        if (gitHubService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitHubService = retrofit.create(GitHubService.class);
        }
        return gitHubService;
    }

    public interface GitHubService {
        @GET("/users/{user}/repos")
        Call<List<GitHubRepo>> getRepo(@Path("user") String name);

        @GET("/users/{user}")
        Call<GitHubUser> getUser(@Path("user") String user);
    }
}


