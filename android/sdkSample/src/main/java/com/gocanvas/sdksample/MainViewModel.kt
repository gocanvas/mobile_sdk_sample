package com.gocanvas.sdksample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SdkApiUiState(
    val licenseKey: String? = null,
    val formJson: String? = null,
    val referenceDataJson: String? = null,
    val prefilledEntriesJson: String? = null,
    val responseJson: String? = null
)

class MainViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SdkApiUiState())
    val uiState: StateFlow<SdkApiUiState> = _uiState.asStateFlow()

    fun setSdkApiUiState(state: SdkApiUiState) {
        _uiState.update { state }
    }

    fun setResponseUiState(responseJson: String?) {
        _uiState.update { it.copy(responseJson = responseJson) }
    }
}