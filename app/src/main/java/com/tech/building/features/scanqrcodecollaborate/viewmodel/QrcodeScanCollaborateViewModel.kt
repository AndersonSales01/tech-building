package com.tech.building.features.scanqrcodecollaborate.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.building.domain.model.CollaboratorModel
import com.tech.building.domain.usecase.collaborator.GetCollaboratorWithQrcodeUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class QrcodeScanCollaborateViewModel(
    private val getCollaboratorWithQrcodeUseCase: GetCollaboratorWithQrcodeUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val actionMutableLiveData: MutableLiveData<QrcodeScanCollaborateUiAction> =
        MutableLiveData()
    val actionLiveData: LiveData<QrcodeScanCollaborateUiAction> = actionMutableLiveData

    private val stateMutableLiveData: MutableLiveData<QrcodeScanCollaborateUiState> =
        MutableLiveData()
    val stateLiveData: LiveData<QrcodeScanCollaborateUiState> = stateMutableLiveData

    fun checkPermission(context: Context) {
        if (hasCameraPermission(context)) {
            startCamera()
        } else {
            requestPermission()
        }
    }

    fun startCamera() {
        actionMutableLiveData.value = QrcodeScanCollaborateUiAction.StartCamera
    }

    private fun requestPermission() {
        actionMutableLiveData.value = QrcodeScanCollaborateUiAction.RequestPermission
    }

    private fun hasCameraPermission(context: Context) =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    fun qrCodeScannerValue(value: String) {
        if (value.isNotEmpty()) {
            getCollaboratorWithQrcode(value)
        }
    }

    private fun getCollaboratorWithQrcode(idRegistration: String) {
        viewModelScope.launch {
            getCollaboratorWithQrcodeUseCase.invoke(idRegistration)
                .flowOn(dispatcher)
                .onStart { }
                .onCompletion {}
                .collect {
                    getCollaboratorWithQrcodeHandleSuccess(it)
                }
        }
    }

    private fun     getCollaboratorWithQrcodeHandleSuccess(collaborator: CollaboratorModel?) {
        if (collaborator != null) {
            actionMutableLiveData.value =
                QrcodeScanCollaborateUiAction.ScanSuccess(collaborator = collaborator)
        } else {
            stateMutableLiveData.value = QrcodeScanCollaborateUiState(hasError = true)
        }

    }
}