package com.lgdevs.recyclerviewwithstatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lgdevs.recyclerviewwithstatus.databinding.ActivityMainBinding
import com.lgdevs.recyclerviewwithstatus.model.Item

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val itemAdapter: ItemAdapter by lazy { ItemAdapter() }
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        itemAdapter.apply {
            onCheckListener = this@MainActivity::onCheckListener
            onItemLongClickListener = this@MainActivity::onItemLongClickListener
        }

        setupObservers()
        setupView()
        handleCheckbox()
    }

    private fun setupView() {
        binding?.let {
            it.list.apply {
                adapter = itemAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            it.btnSelecteds.setOnClickListener {
                Toast.makeText(
                    this,
                    itemAdapter.getAllSelectedItems().joinToString { item -> item.name },
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.items.observe(this) { items ->
            itemAdapter.items = items.also { it.firstOrNull()?.isEnabled = true }
        }
    }

    private fun handleCheckbox(value: Boolean = false) {
        binding?.checkbox?.let {
            it.setOnCheckedChangeListener(null)
            it.isChecked = value
            it.setOnCheckedChangeListener { _, b -> itemAdapter.onSelectedAll(b) }
        }
    }

    private fun onItemLongClickListener(item: Item) {
        itemAdapter.isSelectedEnabled = itemAdapter.isSelectedEnabled.not()
    }

    private fun onCheckListener(item: Item, checked: Boolean) {
        itemAdapter.setItemSelected(item, checked)

        if (!checked && binding?.checkbox?.isChecked == true) {
            handleCheckbox()
        }
    }
}