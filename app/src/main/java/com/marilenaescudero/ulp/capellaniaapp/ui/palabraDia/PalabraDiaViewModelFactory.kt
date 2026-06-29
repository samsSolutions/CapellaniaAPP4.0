package com.marilenaescudero.ulp.capellaniaapp.ui.palabraDia

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PalabraDiaViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PalabraDiaViewModel::class.java)) {
            return PalabraDiaViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
