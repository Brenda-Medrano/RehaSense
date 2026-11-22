package com.rehasense.app.presentation.welcome

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.rehasense.app.R
import com.rehasense.app.core.designsystem.components.RehaSenseButton
import com.rehasense.app.core.designsystem.components.RehaSenseCard
import com.rehasense.app.presentation.common.OnboardingLayout

@Composable
internal fun WelcomeScreen(onStart: () -> Unit) {
    OnboardingLayout {
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary)
        Text(stringResource(R.string.nav_welcome_title), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(R.string.nav_welcome_description), style = MaterialTheme.typography.bodyLarge)
        RehaSenseCard(modifier = Modifier.fillMaxWidth().testTag("nav_start_button")) {
            Text(stringResource(R.string.nav_local_title), style = MaterialTheme.typography.titleLarge)
            Text(stringResource(R.string.nav_local_description), style = MaterialTheme.typography.bodyMedium)
        }
        RehaSenseButton(text = stringResource(R.string.nav_start), onClick = onStart,
            modifier = Modifier.fillMaxWidth().testTag("nav_start_button"))
    }
}
