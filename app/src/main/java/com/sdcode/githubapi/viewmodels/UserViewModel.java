package com.sdcode.githubapi.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdcode.githubapi.apiclasses.APIClient;
import com.sdcode.githubapi.apiclasses.models.GitHubRepo;
import com.sdcode.githubapi.apiclasses.models.GitHubUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    MutableLiveData<GitHubUser> userData;
    MutableLiveData<List<GitHubRepo>> repoData;

    public UserViewModel() {
        userData = new MutableLiveData<>();
        repoData = new MutableLiveData<>();
    }

    public MutableLiveData<GitHubUser> getUserDataObserver() {
        return userData;
    }

    public MutableLiveData<List<GitHubRepo>> getRepoDataObserver() {
        return repoData;
    }

    public void makeApiCallUser(String name) {
        Call<GitHubUser> call = APIClient.getService().getUser(name);
        call.enqueue(new Callback<GitHubUser>() {

            @Override
            public void onResponse(@NonNull Call<GitHubUser> call, @NonNull Response<GitHubUser> response) {
                userData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<GitHubUser> call, @NonNull Throwable t) {
                userData.postValue(null);
            }
        });
    }

    public void makeApiCallRepo(String name) {
        Call<List<GitHubRepo>> call = APIClient.getService().getRepo(name);

        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(@NonNull Call<List<GitHubRepo>> call, @NonNull Response<List<GitHubRepo>> response) {
                repoData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<GitHubRepo>> call, @NonNull Throwable t) {
                repoData.postValue(null);
            }
        });
    }
}
