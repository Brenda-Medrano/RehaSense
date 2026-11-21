package com.rehasense.app.presentation.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.platform.testTag
import com.rehasense.app.R
import com.rehasense.app.core.designsystem.components.RehaSenseButton
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.presentation.common.OnboardingLayout

@Composable
internal fun ProfileRegistrationScreen(
    role: UserRole,
    state: ProfileUiState,
    onNameChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onRestore: () -> Unit,
    onBack: () -> Unit
) {
    OnboardingLayout {
        TextButton(onClick = onBack) { Text(stringResource(R.string.nav_back)) }
        Text(stringResource(R.string.profile_create_title), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(when (role) {
            UserRole.PATIENT -> R.string.nav_patient
            UserRole.PHYSIOTHERAPIST -> R.string.nav_physiotherapist
        }), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(R.string.profile_demo_notice), style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChanged,
            label = { Text(stringResource(R.string.profile_name_label)) },
            isError = state.showNameError,
            supportingText = if (state.showNameError) {
                { Text(stringResource(R.string.profile_name_required)) }
            } else null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
        )
        if (state.saveError) {
            Text(
                stringResource(R.string.profile_save_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (state.restoreNotFound) {
            Text(stringResource(R.string.profile_restore_empty), style = MaterialTheme.typography.bodyMedium)
        }
        if (state.isBusy) {
            Text(stringResource(R.string.profile_busy), style = MaterialTheme.typography.bodyMedium)
        }
        RehaSenseButton(
            text = stringResource(R.string.profile_create_action),
            onClick = onSubmit,
            enabled = !state.isBusy,
            modifier = Modifier.fillMaxWidth()
        )
        RehaSenseButton(
            text = stringResource(R.string.profile_restore_action),
            onClick = onRestore,
            enabled = !state.isBusy,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
