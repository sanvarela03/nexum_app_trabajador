package com.example.nexum_trabajador.ui.presenter.conversations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.model.Conversation
import com.example.nexum_trabajador.domain.use_cases.conversation.ConversationUseCases
import com.example.nexum_trabajador.domain.use_cases.profile.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.1
 */
@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val conversationUseCases: ConversationUseCases,
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    val profiles = profileUseCases.observeProfile()

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _uiState = MutableStateFlow<ConversationsUiState>(ConversationsUiState.Loading)
    val uiState: StateFlow<ConversationsUiState> = _uiState.asStateFlow()

    private val _unreadCount = MutableStateFlow(0L)
    val unreadCount: StateFlow<Long> = _unreadCount.asStateFlow()

    private val _showNewChatDialog = MutableStateFlow(false)
    val showNewChatDialog: StateFlow<Boolean> = _showNewChatDialog.asStateFlow()

    private val _isSearchingProfiles = MutableStateFlow(false)
    val isSearchingProfiles: StateFlow<Boolean> = _isSearchingProfiles.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20
    private var canLoadMore = true

    init {
        loadConversations()
        loadUnreadCount()
        observeIncomingMessages()
    }

    fun loadConversations(refresh: Boolean = false) {
        if (refresh) {
            currentPage = 0
            canLoadMore = true
        }

        if (!canLoadMore && !refresh) return

        viewModelScope.launch {
            _uiState.value = if (refresh) {
                ConversationsUiState.Refreshing
            } else {
                ConversationsUiState.Loading
            }

            conversationUseCases.getConversations(currentPage, pageSize)
                .onSuccess { response ->
                    _conversations.update { current ->
                        val newContent = response.content
                        if (refresh) {
                            newContent.distinctBy { it.id }
                        } else {
                            (current + newContent).distinctBy { it.id }
                        }
                    }
                    
                    val updatedConversations = _conversations.value
                    // Obtener perfiles para los participantes de las conversaciones cargadas
                    fetchProfilesForConversations(updatedConversations)

                    currentPage++
                    canLoadMore = response.content.size == pageSize
                    _uiState.value = ConversationsUiState.Success
                }
                .onFailure { error ->
                    Log.e("ConversationsViewModel", "Error loading conversations", error)
                    _uiState.value = ConversationsUiState.Error(
                        error.message ?: "Error loading conversations"
                    )
                }
        }
    }

    private fun fetchProfilesForConversations(conversations: List<Conversation>) {
        val userIds = conversations
            .flatMap { it.participantIds }
            .mapNotNull { it.toLongOrNull() }
            .distinct()

        if (userIds.isNotEmpty()) {
            viewModelScope.launch {
                profileUseCases.updateProfile(userIds, fetchFromRemote = true).collect { response ->
                    if (response is ApiResponse.Error || response is ApiResponse.Failure) {
                        Log.e("ConversationsViewModel", "Error updating profiles")
                    }
                }
            }
        }
    }

    fun onOpenNewChatDialog() {
        _showNewChatDialog.value = true
        loadAllAvailableProfiles()
    }

    fun onCloseNewChatDialog() {
        _showNewChatDialog.value = false
    }

    private fun loadAllAvailableProfiles() {
        viewModelScope.launch {
            _isSearchingProfiles.value = true
            // Al pasar lista vacía, tu API trae todos los perfiles
            profileUseCases.updateProfile(emptyList(), fetchFromRemote = true).collect { response ->
                if (response !is ApiResponse.Loading) {
                    _isSearchingProfiles.value = false
                }
            }
        }
    }

    fun loadUnreadCount() {
        viewModelScope.launch {
            conversationUseCases.getUnreadCount()
                .onSuccess { count ->
                    _unreadCount.value = count
                }
        }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            conversationUseCases.observeIncomingMessages().collect { newMessage ->
                newMessage?.let {
                    // Actualizar la conversación correspondiente
                    updateConversationWithNewMessage(it.conversationId)

                    // Actualizar contador de no leídos
                    loadUnreadCount()
                }
            }
        }
    }

    private fun updateConversationWithNewMessage(conversationId: String) {
        viewModelScope.launch {
            // Recargar conversaciones para obtener datos actualizados
            loadConversations(refresh = true)
        }
    }

    fun clearError() {
        _uiState.value = ConversationsUiState.Success
    }
}

sealed class ConversationsUiState {
    object Loading : ConversationsUiState()
    object Refreshing : ConversationsUiState()
    object Success : ConversationsUiState()
    data class Error(val message: String) : ConversationsUiState()
}
