package com.example.prison_app.ui.prison

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prison_app.data.database.Prison
import com.example.prison_app.data.database.PrisonDAO
import com.example.prison_app.data.database.PrisonEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrisonsScreenViewModel @Inject constructor(
    val prisonDAO : PrisonDAO,
) : ViewModel() {

    private val _prisons : StateFlow<List<Prison>> = prisonDAO.getAllPrisons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(PrisonState())

    val state = combine(_state, _prisons){ state, prisons ->
        state.copy(
            prisons = prisons
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrisonState())

    fun onEvent(event : PrisonEvent){
        when(event){
            is PrisonEvent.DeletePrison ->
                viewModelScope.launch {
                    prisonDAO.removePrison(event.prisonID)
                }

            PrisonEvent.SavePrison -> {
                val name = state.value.prisonName
                val address = state.value.prisonAddress

                if(name.isBlank() || address.isBlank())
                    return
                val prison = if(state.value.selectedPrison == null) Prison(prisonName = name, prisonAddress = address)
                else state.value.selectedPrison!!.copy(prisonName = name, prisonAddress = address)

                viewModelScope.launch {
                    prisonDAO.upsertPrison(prison)
                }
            }
            is PrisonEvent.SetPrisonAddress -> _state.update { it.copy(prisonAddress = event.prisonAddress) }
            is PrisonEvent.SetPrisonName -> _state.update { it.copy(prisonName = event.prisonName) }
            is PrisonEvent.SelectedPrisonChanged -> _state.update {
                it.copy(selectedPrison = event.prison,)
            }

            PrisonEvent.HideDialog -> {
                _state.update { it.copy(
                    isEditingPrison = false,
                    selectedPrison = null
                ) }
            }

            PrisonEvent.OpenDialog -> _state.update {
                it.copy(isEditingPrison = true)
            }
        }
    }
}