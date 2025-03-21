package com.kanoyatech.snapdex.ui.main.profile

import java.util.Locale

sealed interface ProfileAction {
    object OnLogoutClick : ProfileAction
    object OnResetProgressClick : ProfileAction
    object OnDeleteAccountClick : ProfileAction
    object OnAccountDeletionConfirm: ProfileAction
    object OnAccountDeletionCancel: ProfileAction
    object OnProgressResetConfirm : ProfileAction
    object OnProgressResetCancel : ProfileAction
    object OnChangeNameClick : ProfileAction
    object OnChangePasswordClick : ProfileAction
    object OnChangeAiModelClick : ProfileAction
    object OnChangeLanguageClick : ProfileAction
    object OnChangeNotificationsClick: ProfileAction
    object OnCreditsClick : ProfileAction
    object OnPrivacyPolicyClick : ProfileAction
    data class OnLanguageChange(val language: Locale): ProfileAction
}