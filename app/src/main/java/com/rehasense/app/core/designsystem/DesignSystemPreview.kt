package com.rehasense.app.core.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rehasense.app.core.designsystem.components.RehaSenseButton
import com.rehasense.app.core.designsystem.components.RehaSenseCard
import com.rehasense.app.core.designsystem.theme.RehaSenseTheme

// Design-time sample only. It is not an application screen or a clinical workflow.
@Composable
private fun DesignSystemSample(darkTheme: Boolean) {
    RehaSenseTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("RehaSense", style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground)
                RehaSenseCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Mi recuperación", style = MaterialTheme.typography.titleLarge)
                    Text("Sistema de diseño", style = MaterialTheme.typography.bodyLarge)
                    Text("Vista previa de componentes", color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium)
                    RehaSenseButton("Continuar", onClick = {}, modifier = Modifier.fillMaxWidth())
                    RehaSenseButton("No disponible", onClick = {}, enabled = false,
                        modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Preview(name = "RehaSense claro", showBackground = true, widthDp = 360)
@Composable
private fun LightDesignPreview() = DesignSystemSample(darkTheme = false)

@Preview(name = "RehaSense oscuro", showBackground = true, widthDp = 360)
@Composable
private fun DarkDesignPreview() = DesignSystemSample(darkTheme = true)

@Preview(name = "Pantalla compacta y texto grande", showBackground = true,
    widthDp = 320, fontScale = 1.3f)
@Composable
private fun CompactDesignPreview() = DesignSystemSample(darkTheme = false)
