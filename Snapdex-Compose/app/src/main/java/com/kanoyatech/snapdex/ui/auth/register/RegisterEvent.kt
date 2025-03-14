package com.kanoyatech.snapdex.ui.auth.register

import com.kanoyatech.snapdex.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent
}