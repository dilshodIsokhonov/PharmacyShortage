package com.example.pharmacyshortage.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pharmacyshortage.BaseApplication
import com.example.pharmacyshortage.R
import com.example.pharmacyshortage.databinding.FragmentAddShortageBinding
import com.example.pharmacyshortage.model.Shortage
import com.example.pharmacyshortage.viewmodel.ShortageViewModel
import com.example.pharmacyshortage.viewmodel.ShortageViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * A fragment to enter data for a new [Shortage] or edit data for an existing [Shortage].
 * [Shortage]s can be saved or deleted from this fragment.
 */
class AddShortageFragment : Fragment() {

    private val navigationArgs: AddShortageFragmentArgs by navArgs()

    private lateinit var shortage: Shortage

    private var _binding: FragmentAddShortageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShortageViewModel by activityViewModels {
        ShortageViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddShortageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.id
        if (id > 0) {
            viewModel.getShortage(id).observe(this.viewLifecycleOwner) {
                shortage = it
                bindShortage(shortage)
            }
            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.setOnClickListener {
                deleteShortage(shortage)
            }
        } else {
            binding.saveButton.setOnClickListener {
                addShortage()
            }
        }
    }

    private fun bindShortage(shortage: Shortage?) {
        binding.apply {
            nameInput.setText(shortage!!.name, TextView.BufferType.SPANNABLE)
            priceInput.setText(shortage.previousPrice.toString(), TextView.BufferType.SPANNABLE)
//            shortage.type.forEach{
//                binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
//                    val chip = binding.root.findViewById<Chip>(checkedId).text.toString()
//                    if (it != chip) group.check(it!!.toInt())
//                }
//            }

            pinToTopSwitch.isChecked = shortage.important
            saveButton.setOnClickListener {
                updateShortage()
            }
        }
    }
    private fun updateShortage() {
        if (isValidEntry()) {
            viewModel.update(
                id = navigationArgs.id,
                name = binding.nameInput.text.toString(),
                previousPrice = if (binding.priceInput.text!!.toString().trim().isNotEmpty()) binding.priceInput.text.toString().toInt() else 0,
                type = run(),
                importance = binding.pinToTopSwitch.isChecked
            )
            findNavController().navigate(
                R.id.action_addShortageFragment_to_shortageFragment
            )
        }
    }

    private fun addShortage() {
        if (isValidEntry()) {
            viewModel.insert(
                name = binding.nameInput.text.toString(),
                previousPrice = if (binding.priceInput.text!!.toString().trim().isNotEmpty()) binding.priceInput.text.toString().toInt() else 0,
                type = run(),
                importance = binding.pinToTopSwitch.isChecked
            )
            findNavController().navigate(
                R.id.action_addShortageFragment_to_shortageFragment
            )
        }
    }

    private fun deleteShortage(shortage: Shortage) {
        viewModel.delete(shortage)
        findNavController().navigate(
            R.id.action_addShortageFragment_to_shortageFragment
        )
    }

    private fun isValidEntry() = viewModel.isValidEntry(
        binding.nameInput.text.toString()
    )


    private fun run(): List<String> {
        val checkedChipsText = mutableListOf<String>()
        binding.chipGroup.checkedChipIds.forEach {
            val chip = binding.root.findViewById<Chip>(it).text.toString()
            checkedChipsText.add(chip)
        }
        return checkedChipsText
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}