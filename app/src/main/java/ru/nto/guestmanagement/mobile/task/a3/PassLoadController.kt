package ru.nto.guestmanagement.mobile.task.a3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nto.guestmanagement.mobile.data.PassRepository
import ru.nto.guestmanagement.mobile.model.PassData
import ru.nto.guestmanagement.mobile.model.PassScreenState
import java.util.concurrent.atomic.AtomicBoolean

/** A3. Логика загрузки и обновления данных пропуска. */
class PassLoadController(
    private val repository: PassRepository,
    private val scope: CoroutineScope,
) {
    private val _state = MutableStateFlow<PassScreenState>(PassScreenState.InitialLoading)
    val state: StateFlow<PassScreenState> = _state.asStateFlow()

    private val started = AtomicBoolean(false)
    private val inFlight = AtomicBoolean(false)

    fun start() {
        if (!started.compareAndSet(false, true)) return
        loadInitial()
    }

    fun refresh() {
        val current = _state.value as? PassScreenState.Content ?: return
        loadRefresh(current)
    }

    fun retry() {
        if (_state.value !is PassScreenState.InitialError) return
        loadInitial()
    }

    private fun loadInitial() {
        if (!inFlight.compareAndSet(false, true)) return
        _state.value = PassScreenState.InitialLoading
        scope.launch {
            try {
                val result = safeLoad()
                _state.value = result.fold(
                    onSuccess = { data -> PassScreenState.Content(data, false, false) },
                    onFailure = { error ->
                        val message = error.message?.takeIf { it.isNotBlank() }
                            ?: "Не удалось загрузить пропуск"
                        PassScreenState.InitialError(message)
                    },
                )
            } finally {
                inFlight.set(false)
            }
        }
    }

    private fun loadRefresh(current: PassScreenState.Content) {
        if (!inFlight.compareAndSet(false, true)) return
        _state.value = current.copy(isRefreshing = true)
        scope.launch {
            try {
                val result = safeLoad()
                result.fold(
                    onSuccess = { data -> _state.value = PassScreenState.Content(data, false, false) },
                    onFailure = { _state.value = current.copy(isRefreshing = false, isOffline = true) },
                )
            } finally {
                inFlight.set(false)
            }
        }
    }

    private suspend fun safeLoad(): Result<PassData> = try {
        repository.load()
    } catch (error: Throwable) {
        Result.failure(error)
    }
}
