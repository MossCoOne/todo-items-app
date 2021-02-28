package com.mossco.todoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.mossco.todoapp.database.TodoItem
import com.mossco.todoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), AddToDoDialogFragment.TodoItemDialogListener,
    TodoItemsAdapter.ListItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoItemsViewModel
    private lateinit var todoItemsListAdapter: TodoItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        val viewModelFactory = TodoItemsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoItemsViewModel::class.java)

        binding.fab.setOnClickListener {
            showNoticeDialog()
        }

        setUpRecyclerView()

        viewModel.todoItemList.observe(this) {
            todoItemsListAdapter.submitList(it)
            viewModel.updateProgressBar(it)
        }

        viewModel.percentageOfCompletedItems.observe(this) {
            binding.contentLayout.itemsProgressBar.progress = it
            binding.contentLayout.percentageCompletedTextView.text = "$it%\nDone"
        }

        viewModel.showFeedbackMessageEvent.observe(this) {
            displaySnackBar(it)
        }

    }

    private fun setUpRecyclerView() {
        todoItemsListAdapter = TodoItemsAdapter(this)
        val todoItemsRecyclerView = binding.contentLayout.notesRecyclerView
        todoItemsRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = todoItemsListAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                viewModel.onDeleteAllClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, item: TodoItem) {
        viewModel.onAddItemClicked(item)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
    }

    override fun onNothingAdded() {
        displaySnackBar(getString(R.string.item_not_added))
    }

    private fun showNoticeDialog() {
        val dialog = AddToDoDialogFragment()
        dialog.show(supportFragmentManager, "AddToDoDialogFragment")
    }

    override fun onCheckBoxToggled(todoItem: TodoItem) {
        viewModel.onCheckBoxToggled(todoItem)
    }

    override fun onDeleteButtonClicked(itemId: Int) {
        viewModel.onDeleteButtonClicked(itemId)
    }

    private fun displaySnackBar(message: String) {
        val snackbar = Snackbar
            .make(binding.contentLayout.progressBarContainer, message, Snackbar.LENGTH_SHORT)
        snackbar.show()

    }
}