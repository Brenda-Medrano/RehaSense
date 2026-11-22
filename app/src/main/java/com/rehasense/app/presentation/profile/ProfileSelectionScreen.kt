package com.rehasense.app.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.rehasense.app.R
import com.rehasense.app.core.designsystem.components.RehaSenseButton
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.presentation.common.OnboardingLayout

@Composable
internal fun ProfileSelectionScreen(onBack: () -> Unit, onContinue: (UserRole) -> Unit) {
    var selectedRole by rememberSaveable { mutableStateOf<UserRole?>(null) }
    OnboardingLayout {
        TextButton(onClick = onBack) { Text(stringResource(R.string.nav_back)) }
        Text(stringResource(R.string.nav_select_title), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(R.string.nav_select_description), style = MaterialTheme.typography.bodyLarge)
        Column(modifier = Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            RoleOption(
                title = stringResource(R.string.nav_patient),
                tag = "nav_role_patient",
                description = stringResource(R.string.nav_patient_description),
                selected = selectedRole == UserRole.PATIENT,
                onSelect = { selectedRole = UserRole.PATIENT }
            )
            RoleOption(
                title = stringResource(R.string.nav_physiotherapist),
                tag = "nav_role_physiotherapist",
                description = stringResource(R.string.nav_physiotherapist_description),
                selected = selectedRole == UserRole.PHYSIOTHERAPIST,
                onSelect = { selectedRole = UserRole.PHYSIOTHERAPIST }
            )
        }
        RehaSenseButton(
            text = stringResource(R.string.nav_continue),
            enabled = selectedRole != null,
            onClick = { selectedRole?.let(onContinue) },
            modifier = Modifier.fillMaxWidth().testTag("nav_continue_button")
        )
    }
}

@Composable
private fun RoleOption(title: String, description: String, tag: String, selected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp)
                .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
                .testTag(tag)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RadioButton(selected = selected, onClick = null)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
