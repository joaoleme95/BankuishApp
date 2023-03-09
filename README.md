# BankuishApp
Bankuish Android Developer Technical Challenge

When the application starts, the layout of the main activity is defined and an empty list adapter is created.

When the user clicks on the "Search" button, the getRepositories function is called, which sends an HTTP GET request to the GitHub API
using the Retrofit library and a GitClient interface.

When the response is received, the onResponse function is called and the data is parsed using the Gson library.
If the response was successful, the list of repositories is filled in the list adapter and displayed on the screen.

When the user clicks on an item in the list, the onItemClickListener function is called and a repository details dialog is displayed.

The repository details dialog shows additional information about the selected repository, including ID, name, owner, HTML URL, description, 
size, and language. The information is provided through a GitRepos object and is passed through a Bundle at the time of dialog creation.

In the line val repoJson = reposJson?.getAsJsonArray("items")?.get(position), we are trying to get the JSON object that represents 
the selected repository in the list, based on its position. However, it is possible that this JSON object may not be present in reposJson, 
or something may have gone wrong while retrieving it, causing repoJson to be null.

Next, we are trying to convert this JSON object into a GitRepos object in the line 
val repo = reposJson?.getAsJsonArray("items")?.get(position)?.let { Gson().fromJson(it, GitRepos::class.java) }. 
Once again, if repoJson is null or something goes wrong during the conversion, repo will also be null.

If repo is null, then we will not be able to pass the repository information to the dialog, and therefore the dialog will not be displayed correctly.
