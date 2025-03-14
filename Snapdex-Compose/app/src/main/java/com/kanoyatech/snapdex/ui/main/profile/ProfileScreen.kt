package com.kanoyatech.snapdex.ui.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kanoyatech.snapdex.R
import com.kanoyatech.snapdex.theme.AppTheme
import com.kanoyatech.snapdex.theme.designsystem.PrimaryButton
import com.kanoyatech.snapdex.ui.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    paddingValues: PaddingValues,
    viewModel: ProfileViewModel = koinViewModel(),
    onLoggedOut: () -> Unit
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ProfileEvent.LoggedOut -> onLoggedOut()
        }
    }

    ProfileScreen(
        paddingValues = paddingValues,
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ProfileScreen(
    paddingValues: PaddingValues,
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = state.email
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(id = R.string.logout),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            onAction(ProfileAction.OnLogoutClick)
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            paddingValues = PaddingValues(0.dp),
            state = ProfileState(email = "test@example.com"),
            onAction = {}
        )
    }
}