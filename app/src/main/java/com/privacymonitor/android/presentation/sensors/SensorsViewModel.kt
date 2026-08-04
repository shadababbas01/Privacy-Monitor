package com.privacymonitor.android.presentation.sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privacymonitor.android.domain.model.SensorStatus
import com.privacymonitor.android.domain.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SensorsViewModel @Inject constructor(
    sensorRepository: SensorRepository
) : ViewModel() {

    val sensorStatuses: StateFlow<List<SensorStatus>> = sensorRepository.observeSensorStatuses()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}
