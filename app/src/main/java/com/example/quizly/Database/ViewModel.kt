package com.example.quizly.Database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun updateCard(id: Int, newCard: String, newValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateCard(id, newCard, newValue)
        }
    }

    fun getCardById(id: Int, onResult: (Card?) -> Unit) {
        viewModelScope.launch {
            val card = withContext(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    dao.getCardById(id)
                }
            }
            onResult(card)
        }
    }

    fun deleteCard(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCardById(id)
        }
    }


}
