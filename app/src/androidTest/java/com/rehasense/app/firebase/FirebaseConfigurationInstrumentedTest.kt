package com.rehasense.app.firebase

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rehasense.app.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseConfigurationInstrumentedTest {
    @Test
    fun firebaseIsInitializedFromTheRegisteredAndroidApp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val firebaseApp = FirebaseApp.getInstance()

        assertEquals("com.rehasense.app", context.packageName)
        assertEquals(context.getString(R.string.google_app_id), firebaseApp.options.applicationId)
        assertTrue(!firebaseApp.options.projectId.isNullOrBlank())
        assertNotNull(FirebaseAuth.getInstance(firebaseApp))
        assertNotNull(FirebaseFirestore.getInstance(firebaseApp))
    }
}
