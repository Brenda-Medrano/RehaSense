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
import com.rehasense.app.domain.model.LocalUserProfile
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.presentation.common.OnboardingLayout

@Composable
internal fun ProfileDashboard(profile: LocalUserProfile, onChangeProfile: () -> Unit) {
    val role = profile.profile.role
    val title = when (role) {
        UserRole.PATIENT -> R.string.nav_patient_home
        UserRole.PHYSIOTHERAPIST -> R.string.nav_physiotherapist_home
    }
    val features = when (role) {
        UserRole.PATIENT -> listOf(R.string.profile_feature_exercises, R.string.profile_feature_sessions,
            R.string.profile_feature_history, R.string.profile_feature_sensors)
        UserRole.PHYSIOTHERAPIST -> listOf(R.string.profile_feature_patients, R.string.profile_feature_programs,
            R.string.profile_feature_results, R.string.profile_feature_compare)
    }
    OnboardingLayout {
        Text(stringResource(title), style = MaterialTheme.typography.headlineSmall)
        RehaSenseCard(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.profile_greeting, profile.user.name), style = MaterialTheme.typography.titleLarge)
            Text(stringResource(R.string.profile_temporary_notice), style = MaterialTheme.typography.bodyMedium)
        }
        Text(stringResource(R.string.profile_future_features), style = MaterialTheme.typography.titleMedium)
        features.forEach { feature ->
            RehaSenseCard(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(feature), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.profile_not_available), style = MaterialTheme.typography.bodyMedium)
            }
        }
        RehaSenseButton(text = stringResource(R.string.nav_change_profile), onClick = onChangeProfile,
            modifier = Modifier.fillMaxWidth())
    }
}
