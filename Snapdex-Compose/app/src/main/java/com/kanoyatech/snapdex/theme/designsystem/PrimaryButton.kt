package com.kanoyatech.snapdex.theme.designsystem

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kanoyatech.snapdex.theme.AppTheme

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isBusy: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isBusy,
        modifier = modifier
    ) {
        if (isBusy) {
            CircularProgressIndicator()
        } else {
            Text(text)
        }
    }
}

@Preview
@Composable
private fun PrimaryButtonEnabledPreview() {
    AppTheme {
        PrimaryButton(
            text = "Click me",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonDisabledPreview() {
    AppTheme {
        PrimaryButton(
            text = "Click me",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonBusyPreview() {
    AppTheme {
        PrimaryButton(
            text = "Click me",
            isBusy = true,
            onClick = {}
        )
    }
}