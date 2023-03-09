package com.example.bankuishapplication.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.example.bankuishapplication.Api.GitRepos
import com.example.bankuishapplication.R


class RepositoryDetailsDialog(private val repo: GitRepos) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.repository_details_dialog, null)

        val repoDetails = "ID: ${repo.getId()}\n" +
                "NODE ID: ${repo.getNodeId()}\n" +
                "NAME: ${repo.getName()}\n" +
                "PRIVATE: ${repo.getPrivate()}\n" +
                "OWNER NAME: ${repo.getOwner().getLogin()}\n" +
                "OWNER ID: ${repo.getOwner().getUserID()}\n" +
                "HTML URL: ${repo.getHtmlURL()}\n" +
                "DESCRIPTION: ${repo.getDescription()}\n" +
                "SIZE: ${repo.getSize()}\n" +
                "LANGUAGE: ${repo.getLanguage()}\n"

        dialogView.findViewById<TextView>(R.id.tvRepoDetails).text = repoDetails

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }

        return builder.create()
    }
}