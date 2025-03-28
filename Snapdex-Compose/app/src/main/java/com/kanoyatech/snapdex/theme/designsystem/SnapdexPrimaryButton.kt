package com.kanoyatech.snapdex.theme.designsystem

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kanoyatech.snapdex.theme.AppTheme
import com.kanoyatech.snapdex.theme.SnapdexTheme

@Composable
fun SnapdexPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false,
    enabled: Boolean = true,
    isBusy: Boolean = false,
    onClick: () -> Unit
) {
    val colors = if (!isDestructive) {
        ButtonDefaults.buttonColors().copy(
            containerColor = SnapdexTheme.colorScheme.primary,
            contentColor = SnapdexTheme.colorScheme.onPrimary
        )
    } else {
        ButtonDefaults.buttonColors().copy(
            containerColor = SnapdexTheme.colorScheme.error,
            contentColor = SnapdexTheme.colorScheme.onError
        )
    }

    Button(
        onClick = onClick,
        enabled = enabled && !isBusy,
        shape = SnapdexTheme.shapes.regular,
        modifier = modifier
            .height(48.dp),
        colors = colors
    ) {
        if (isBusy) {
            SnapdexCircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
            )
        } else {
            Text(text)
        }
    }
}

@Preview
@Composable
private fun SnapdexPrimaryButtonEnabledPreview() {
    AppTheme {
        SnapdexPrimaryButton(
            text = "Click me",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SnapdexPrimaryButtonDisabledPreview() {
    AppTheme {
        SnapdexPrimaryButton(
            text = "Click me",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SnapdexPrimaryButtonBusyPreview() {
    AppTheme {
        SnapdexPrimaryButton(
            text = "Click me",
            isBusy = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SnapdexPrimaryButtonDestructivePreview() {
    AppTheme {
        SnapdexPrimaryButton(
            text = "Click me",
            isDestructive = true,
            onClick = {}
        )
    }
}