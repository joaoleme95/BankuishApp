package com.example.bankuishapplication.Api

import com.google.gson.annotations.SerializedName

class GitRepoResponse {
    @SerializedName("items")
    private val repos: List<GitRepos> = emptyList()

    fun getRepos(): List<GitRepos> {
        return repos
    }
}