package com.matosa.cineversemobile.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.matosa.cineversemobile.data.SessionManager
import com.matosa.cineversemobile.databinding.FragmentChatBinding
import com.matosa.cineversemobile.model.ChatMessage
import com.matosa.cineversemobile.model.ChatMessageDTO
import com.matosa.cineversemobile.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatAdapter

    private var myUserId: Int = -1
    private var currentConversationId: Int = -1

    private lateinit var stompClient: StompClient
    private val gson = Gson()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        myUserId = session.fetchUserId()

        adapter = ChatAdapter(myUserId, mutableListOf())
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()

            if (text.isNotEmpty()) {
                if (::stompClient.isInitialized && stompClient.isConnected) {
                    enviarMensajePorWebSocket(text)
                } else {
                    Toast.makeText(context, "Conectando al chat...", Toast.LENGTH_SHORT).show()
                    Log.w("CHAT", "Intento de envío sin conexión. Reconectando...")

                    val idParaConectar = if(currentConversationId > 0) currentConversationId else 0
                    conectarWebSocket(idParaConectar)
                }
            }
        }

        cargarHistorialInicial()
    }

    private fun cargarHistorialInicial() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getChatHistory(myUserId)

                if (response.isSuccessful && response.body() != null) {
                    val historial = response.body()!!
                    adapter.updateMessages(historial)

                    if (historial.isNotEmpty()) {
                        currentConversationId = historial[0].conversationId
                        conectarWebSocket(currentConversationId)
                    } else {
                        crearNuevaConversacion()
                    }
                } else {
                    crearNuevaConversacion()
                }
            } catch (e: Exception) {
                Log.e("CHAT", "Fallo al cargar historial: ${e.message}")
                crearNuevaConversacion()
            }
        }
    }

    private fun crearNuevaConversacion() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val request = mapOf("userId" to myUserId)
                val response = RetrofitClient.instance.createConversation(request)

                if (response.isSuccessful && response.body() != null) {
                    currentConversationId = response.body()!!.id
                    Log.d("CHAT", "Conversación creada con ID: $currentConversationId")

                    conectarWebSocket(currentConversationId)
                } else {
                    Log.e("CHAT", "Error servidor al crear conv: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "Error de red al crear conv", e)
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun conectarWebSocket(conversationId: Int) {
        if (::stompClient.isInitialized && stompClient.isConnected) return

        val url = "ws://10.0.2.2:8081/ws"
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> Log.d("CHAT", " WebSocket Conectado")
                    LifecycleEvent.Type.ERROR -> Log.e("CHAT", " Error WebSocket", lifecycleEvent.exception)
                    LifecycleEvent.Type.CLOSED -> Log.d("CHAT", " WebSocket Cerrado")
                    else -> {}
                }
            }

        val topic = "/topic/conversation/$conversationId"
        stompClient.topic(topic)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                try {
                    val dto = gson.fromJson(topicMessage.payload, ChatMessageDTO::class.java)

                    val visualMsg = ChatMessage(
                        id = dto.id ?: 0,
                        conversationId = conversationId,
                        senderId = dto.senderId ?: 0,
                        message = dto.message ?: "",
                        // Protección anti-nulls en la fecha
                        sentAt = dto.sentAt ?: "Ahora"
                    )
                    adapter.addMessage(visualMsg)
                    binding.rvChat.smoothScrollToPosition(adapter.itemCount - 1)
                } catch (e: Exception) {
                    Log.e("CHAT", "Error procesando mensaje", e)
                }
            }, { error -> Log.e("CHAT", "Error suscripción", error) })

        stompClient.connect()
    }

    @SuppressLint("CheckResult")
    private fun enviarMensajePorWebSocket(text: String) {
        val msgDTO = ChatMessageDTO(
            senderId = myUserId,
            message = text,
            conversationId = if (currentConversationId > 0) currentConversationId else null
        )

        val json = gson.toJson(msgDTO)

        stompClient.send("/app/chat.send", json)
            .compose { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
            .subscribe({
                Log.d("CHAT", "Mensaje enviado")
                binding.etMessage.text.clear()
            }, { error ->
                Toast.makeText(context, "Error al enviar", Toast.LENGTH_SHORT).show()
                Log.e("CHAT", "Error envío", error)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::stompClient.isInitialized && stompClient.isConnected) {
            stompClient.disconnect()
        }
        _binding = null
    }
}