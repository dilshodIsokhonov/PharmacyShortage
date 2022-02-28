package com.example.pharmacyshortage.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pharmacyshortage.BaseApplication
import com.example.pharmacyshortage.R
import com.example.pharmacyshortage.databinding.FragmentShortageDetailBinding
import com.example.pharmacyshortage.model.Shortage
import com.example.pharmacyshortage.model.getFormattedPrice
import com.example.pharmacyshortage.viewmodel.ShortageViewModel
import com.example.pharmacyshortage.viewmodel.ShortageViewModelFactory

/**
 * A fragment to display the details of a [Shortage] currently stored in the database.
 * The [AddShortageFragment] can be launched from this fragment to edit the [Shortage]
 */
class ShortageDetailFragment : Fragment() {

    private val navigationArgs: ShortageDetailFragmentArgs by navArgs()

    private val viewModel: ShortageViewModel by activityViewModels {
        ShortageViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }

    private lateinit var shortage: Shortage

    private var _binding: FragmentShortageDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShortageDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id

        viewModel.getShortage(id).observe(this.viewLifecycleOwner) {
            shortage = it
            bindShortage()
        }
    }

    private fun bindShortage() {
        binding.apply {
            name.text = shortage.name
            if (shortage.previousPrice > 0) {
                previousPrice.text = getString(R.string.price, shortage.getFormattedPrice())
            }else {
                previousPrice.visibility = View.GONE
                dividerPrice.visibility = View.GONE
            }
            type.text = getString(R.string.type,shortage.type.joinToString(","))
            if (shortage.important) {
                importance.text = getString(R.string.importance_top)
            } else {
                importance.text = getString(R.string.importance_default_kind)
            }
            editExtendedFab.setOnClickListener {
                val action = ShortageDetailFragmentDirections
                    .actionShortageDetailFragmentToAddShortageFragment(shortage.id)
                findNavController().navigate(action)
            }
            name.setOnClickListener{
                launchBrowser()
            }
        }
    }

    // Launches a browser to search a medicine(shortage name)
    private fun launchBrowser() {
        val name: String = binding.name.text.toString()
        val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
        searchIntent.putExtra(SearchManager.QUERY, name)
        startActivity(searchIntent)
    }
}