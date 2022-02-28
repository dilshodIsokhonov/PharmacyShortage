package com.example.pharmacyshortage.viewmodel

import androidx.lifecycle.*
import com.example.pharmacyshortage.data.ShortageRepository
import com.example.pharmacyshortage.model.Shortage
import kotlinx.coroutines.launch

/**
 * Shared [ViewModel] to provide data to the [ShortageFragment], [ShortageDetailFragment],
 * and [AddShortageFragment] and allow for interaction the the [ShortageDao]
 */
class ShortageViewModel(private val repository: ShortageRepository) : ViewModel() {

    // Using LiveData and caching what allShortages returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allShortagesByNameAsc: LiveData<List<Shortage>> =
        repository.allShortagesByNameAsc.asLiveData()
    val allShortagesByNameDesc: LiveData<List<Shortage>> =
        repository.allShortagesByNameDesc.asLiveData()
    val allShortagesByImportance: LiveData<List<Shortage>> =
        repository.allShortagesByImportance.asLiveData()
    val allShortagesByType: LiveData<List<Shortage>> =
        repository.allShortagesByType.asLiveData()
    val getCount: LiveData<Long> =
        repository.getCount.asLiveData()

    fun getShortage(id: Long): LiveData<Shortage> = repository.getShortage(id).asLiveData()

    fun insert(
        name: String,  previousPrice: Int, type: List<String>,
        importance: Boolean
    ) {
        val shortage = Shortage(
            name = name,  previousPrice = previousPrice, type = type,
            important = importance
        )
        viewModelScope.launch {
            repository.insert(shortage)
        }
    }

    fun update(
        id: Long, name: String, previousPrice: Int,type: List<String>, importance: Boolean
    ) {
        val shortage = Shortage(
            id = id, name = name, previousPrice = previousPrice, type = type,
            important = importance
        )
        viewModelScope.launch {
            repository.update(shortage)
        }
    }

    fun delete(shortage: Shortage) = viewModelScope.launch {
        repository.delete(shortage)
    }

    fun isValidEntry(name: String): Boolean {
        return name.isNotBlank()
    }


}

class ShortageViewModelFactory(private val repository: ShortageRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShortageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShortageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

