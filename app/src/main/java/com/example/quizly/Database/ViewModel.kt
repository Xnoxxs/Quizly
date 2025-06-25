
package com.example.quizly.Database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(private val dao: CardDao) : ViewModel() {
    val cards = dao.getAllCards().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addCard(text: String, value: String) {
        viewModelScope.launch {
            dao.insertCard(Card(card = text, value = value))
        }
    }

    fun updateCardValue(id: Int, newValue: String) {
        viewModelScope.launch {
            dao.updateCardValue(id, newValue)
        }
    }
}
