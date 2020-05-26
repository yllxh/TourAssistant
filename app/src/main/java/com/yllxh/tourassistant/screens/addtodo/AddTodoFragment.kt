package com.yllxh.tourassistant.screens.addtodo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yllxh.tourassistant.R
import com.yllxh.tourassistant.databinding.FragmentAddTodoBinding
import kotlinx.android.synthetic.main.fragment_add_todo.*

class AddTodoFragment : Fragment() {
    private lateinit var binding: FragmentAddTodoBinding
    private val todo by lazy {
        AddTodoFragmentArgs.fromBundle(requireArguments()).todo
    }
    private val viewModel by lazy {
        val factory = AddToDoViewModelFactory(todo, requireActivity().application)
        ViewModelProvider(this, factory).get(AddToDoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoBinding.inflate(inflater)
        binding.todo = todo
        binding.importanceSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.setImportance(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_todo_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            viewModel.setNote(binding.todoEditText.text.toString())
            viewModel.save()
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
