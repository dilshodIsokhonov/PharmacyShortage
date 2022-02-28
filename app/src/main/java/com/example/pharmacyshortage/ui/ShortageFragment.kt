package com.example.pharmacyshortage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pharmacyshortage.BaseApplication
import com.example.pharmacyshortage.R
import com.example.pharmacyshortage.adapter.ShortageListAdapter
import com.example.pharmacyshortage.databinding.FragmentShortageBinding
import com.example.pharmacyshortage.model.Shortage
import com.example.pharmacyshortage.viewmodel.ShortageViewModel
import com.example.pharmacyshortage.viewmodel.ShortageViewModelFactory

/**
 * A fragment to view the list of [Shortage]s stored in the database.
 * Clicking on a [Shortage] list item launches the [ShortageDetailFragment].
 * Clicking the [ExtendedFloatingActionButton] launches the [AddShortageFragment]
 */
class ShortageFragment : Fragment() {

    private val viewModel: ShortageViewModel by activityViewModels {
        ShortageViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }

    private var _binding: FragmentShortageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShortageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ShortageListAdapter { shortage ->
            val action = ShortageFragmentDirections
                .actionShortageFragmentToShortageDetailFragment(shortage.id)
            findNavController().navigate(action)

        }
        viewModel.allShortagesByImportance.observe(this.viewLifecycleOwner) { shortage ->
            shortage.let {
                adapter.submitList(it)
            }
        }
        binding.apply {
            shortageRecyclerView.adapter = adapter
            extendedFab.setOnClickListener {
                findNavController().navigate(
                    R.id.action_shortageFragment_to_addShortageFragment
                )
            }
        }
    }


}