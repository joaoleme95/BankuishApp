package com.example.bankuishapplication

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bankuishapplication.Api.GitClient
import com.example.bankuishapplication.Api.GitRepos
import com.example.bankuishapplication.ui.RepositoryDetailsDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val githubURL = "https://api.github.com/"

private var reposJson: JsonObject? = null
private val htmlURLList: MutableList<String> = ArrayList()

class MainActivity : AppCompatActivity() {

    private lateinit var requestAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        // Initialize listView adapter
        val initialList: List<String> = ArrayList()
        requestAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, initialList)
        findViewById<ListView>(R.id.lvRepositories).adapter = requestAdapter

        // When clicking on list item/repository, redirect to the details dialog
        findViewById<ListView>(R.id.lvRepositories).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val repoJson = reposJson?.getAsJsonArray("items")?.get(position)
                Log.d("RepositoryDetails", "repoJson: $repoJson")
                val repo = reposJson?.getAsJsonArray("items")?.get(position)?.let {
                    Gson().fromJson(it, GitRepos::class.java)
                }
                Log.d("RepositoryDetails", "repo: $repo")
                val detailsFragment = repo?.let {
                    RepositoryDetailsDialog(it).apply {
                        arguments = Bundle().apply {
                            putString("repoDetails", Gson().toJson(repo))
                            putString(
                                "additionalInfo",
                                "ID: ${repo?.getId()}\n" +
                                        "NODE ID: ${repo?.getNodeId()}\n" +
                                        "NAME: ${repo?.getName()}\n" +
                                        "PRIVATE: ${repo?.getPrivate()}\n" +
                                        "OWNER NAME: ${repo?.getOwner()?.getLogin()}\n" +
                                        "OWNER ID: ${repo?.getOwner()?.getUserID()}\n" +
                                        "HTML URL: ${repo?.getHtmlURL()}\n" +
                                        "DESCRIPTION: ${repo?.getDescription()}\n" +
                                        "SIZE: ${repo?.getSize()}\n" +
                                        "LANGUAGE: ${repo?.getLanguage()}\n"
                            )
                        }
                    }
                }
                Log.d("RepositoryDetails", "detailsFragment: $detailsFragment")
                detailsFragment?.show(supportFragmentManager, null)
            }
        // Search for all repositories
        findViewById<Button>(R.id.bSearch).setOnClickListener{
            requestAdapter.clear()
            getRepositories(String())
        }
    }

    private fun getRepositories(username : String) {
        try {
            val builder: Retrofit.Builder = Retrofit.Builder()
                .baseUrl(githubURL)
                .addConverterFactory(GsonConverterFactory.create())

            val retrofit: Retrofit = builder.build()
            val githubService = retrofit.create(GitClient::class.java)
            val call = githubService.searchRepositories("language:kotlin", 20, 2)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (!response.isSuccessful) {
                        val gson = Gson()
                        val reposJson = response.body()?.getAsJsonArray("items")
                        val repos = reposJson?.map { gson.fromJson(it, GitRepos::class.java) } ?: emptyList()

                        Log.e("Request", response.code().toString())
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Error code: ${response.code()}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        return
                    }
                    // Else if request is a success
                    val gson = Gson()
                    val reposJson = response.body()?.getAsJsonArray("items")
                    val repos = reposJson?.map {gson.fromJson(it, GitRepos::class.java) } ?: emptyList()

                    for (repo in repos) {
                        var reposData = ""
                        reposData += "ID: " + repo.getId() + "\n"
                        reposData += "NODE ID: " + repo.getNodeId() + "\n"
                        reposData += "NAME: " + repo.getName() + "\n"
                        reposData += "PRIVATE: " + repo.getPrivate() + "\n"
                        reposData += "OWNER NAME: " + repo.getOwner().getLogin() + "\n"
                        reposData += "OWNER ID: " + repo.getOwner().getUserID() + "\n"
                        reposData += "HTML URL: " + repo.getHtmlURL() + "\n"
                        reposData += "DESCRIPTION: " + repo.getDescription() + "\n"
                        reposData += "SIZE: " + repo.getSize() + "\n"
                        reposData += "LANGUAGE: " + repo.getLanguage() + "\n\n"

                        //myAdapter.add(reposData)
                        htmlURLList.add(repo.getHtmlURL())
                        requestAdapter.add(repo.getName() + "\n" + "Author: "+repo.getOwner().getLogin())

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Request", t.message.toString())
                    Snackbar.make(findViewById(android.R.id.content), "Oops, something went wrong.", Snackbar.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Log.e("Request", e.toString())
            Snackbar.make(findViewById(android.R.id.content), "Oops, something went wrong.", Snackbar.LENGTH_SHORT).show()
        }
    }
}