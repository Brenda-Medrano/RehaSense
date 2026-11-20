package com.rehasense.app.presentation.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rehasense.app.R
import com.rehasense.app.core.designsystem.components.RehaSenseButton
import com.rehasense.app.core.designsystem.components.RehaSenseCard
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.presentation.common.OnboardingLayout

/** Initial navigation destination only; actual dashboards are implemented in stage 1 patch 0006. */
@Composable
internal fun ProfileHomeScreen(role: UserRole, onChangeProfile: () -> Unit) {
    val title = when (role) {
        UserRole.PATIENT -> R.string.nav_patient_home
        UserRole.PHYSIOTHERAPIST -> R.string.nav_physiotherapist_home
    }
    OnboardingLayout {
        Text(stringResource(title), style = MaterialTheme.typography.headlineSmall)
        RehaSenseCard(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.nav_home_pending_title), style = MaterialTheme.typography.titleLarge)
            Text(stringResource(R.string.nav_home_pending_description), style = MaterialTheme.typography.bodyMedium)
        }
        RehaSenseButton(text = stringResource(R.string.nav_change_profile), onClick = onChangeProfile,
            modifier = Modifier.fillMaxWidth())
    }
}
