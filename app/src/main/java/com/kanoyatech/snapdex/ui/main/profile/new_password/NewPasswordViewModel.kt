package com.kanoyatech.snapdex.ui.main.profile.new_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanoyatech.snapdex.R
import com.kanoyatech.snapdex.domain.UserDataValidator
import com.kanoyatech.snapdex.domain.repositories.ChangePasswordError
import com.kanoyatech.snapdex.domain.repositories.UserRepository
import com.kanoyatech.snapdex.ui.UiText
import com.kanoyatech.snapdex.utils.TypedResult
import com.kanoyatech.snapdex.utils.textAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NewPasswordViewModel(
    private val userDataValidator: UserDataValidator,
    private val userRepository: UserRepository
): ViewModel() {
    var state by mutableStateOf(NewPasswordState())
        private set

    private val eventChannel = Channel<NewPasswordEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        state.newPassword.textAsFlow()
            .onEach {
                val isOldPasswordValid = it.isNotBlank()
                state = state.copy(
                    isOldPasswordValid = isOldPasswordValid,
                    canChangePassword = isOldPasswordValid && state.newPasswordValidationState.isValid
                )
            }
            .launchIn(viewModelScope)

        state.newPassword.textAsFlow()
            .onEach {
                val passwordValidationState = userDataValidator.validatePassword(it.toString())
                state = state.copy(
                    newPasswordValidationState = passwordValidationState,
                    canChangePassword = state.isOldPasswordValid && passwordValidationState.isValid
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NewPasswordAction) {
        when (action) {
            NewPasswordAction.OnToggleOldPasswordVisibilityClick -> {
                state = state.copy(isOldPasswordVisible = !state.isOldPasswordVisible)
            }
            NewPasswordAction.OnToggleNewPasswordVisibilityClick -> {
                state = state.copy(isNewPasswordVisible = !state.isNewPasswordVisible)
            }
            NewPasswordAction.OnSetPasswordClick -> setPassword()
            NewPasswordAction.OnPasswordChangedPopupDismiss -> {
                state = state.copy(showPasswordChangedPopup = false)
            }
            else -> Unit
        }
    }

    private fun setPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isChangingPassword = true)

            val result = userRepository.changePassword(
                oldPassword = state.oldPassword.text.toString(),
                newPassword = state.newPassword.text.toString()
            )

            state = state.copy(isChangingPassword = false)

            when (result) {
                is TypedResult.Error -> {
                    val message =
                        when (result.error) {
                            is ChangePasswordError.InvalidOldPassword -> UiText.StringResource(id = R.string.change_password_invalid_old_password)
                            is ChangePasswordError.InvalidNewPassword -> UiText.StringResource(id = R.string.change_password_invalid_new_password)
                            is ChangePasswordError.UpdatePasswordFailed -> UiText.StringResource(id = R.string.change_password_failed)
                        }

                    eventChannel.send(NewPasswordEvent.Error(message))
                }
                is TypedResult.Success -> {
                    eventChannel.send(NewPasswordEvent.PasswordChanged)
                    state = state.copy(showPasswordChangedPopup = true)
                }
            }
        }
    }
}