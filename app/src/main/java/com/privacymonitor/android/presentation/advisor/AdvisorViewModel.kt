package com.privacymonitor.android.presentation.advisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privacymonitor.android.domain.repository.AdvisorResponse
import com.privacymonitor.android.domain.repository.PrivacyAdvisor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val recommendation: String? = null
)

enum class MessageSender { USER, ADVISOR }

@HiltViewModel
class AdvisorViewModel @Inject constructor(
    private val privacyAdvisor: PrivacyAdvisor
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "welcome",
                sender = MessageSender.ADVISOR,
                text = "Namaste! Main aapka AI Privacy Advisor hoon. Kisi bhi app ki permission ya risk level ke baare mein sawaal poochhein."
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun askQuestion(question: String) {
        if (question.isBlank()) return

        val userMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            sender = MessageSender.USER,
            text = question
        )
        _messages.value = _messages.value + userMsg

        viewModelScope.launch {
            val response: AdvisorResponse = privacyAdvisor.explain(question)
            val advisorMsg = ChatMessage(
                id = (System.currentTimeMillis() + 1).toString(),
                sender = MessageSender.ADVISOR,
                text = response.answer,
                recommendation = response.recommendedAction
            )
            _messages.value = _messages.value + advisorMsg
        }
    }
}
