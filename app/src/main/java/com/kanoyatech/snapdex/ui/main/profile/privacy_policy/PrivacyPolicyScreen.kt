package com.kanoyatech.snapdex.ui.main.profile.privacy_policy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kanoyatech.snapdex.R
import com.kanoyatech.snapdex.theme.AppTheme
import com.kanoyatech.snapdex.theme.designsystem.SnapdexScaffold
import com.kanoyatech.snapdex.theme.designsystem.SnapdexTopAppBar
import com.kanoyatech.snapdex.theme.pagePadding
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val markdown = remember {
        context.resources.openRawResource(R.raw.privacy_policy)
            .bufferedReader()
            .use { it.readText() }
    }

    SnapdexScaffold(
        topBar = {
            SnapdexTopAppBar(
                title = stringResource(id = R.string.privacy_policy),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .clipToBounds()
            ) {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier
                        .pagePadding()
                )
            }
        }
    }
}

@Preview
@Composable
private fun PrivacyPolicyScreenPreview() {
    AppTheme {
        PrivacyPolicyScreen(
            onBackClick = {}
        )
    }
}