package com.smartsorovnoma.presentation.screen

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smartsorovnoma.BuildConfig
import com.smartsorovnoma.R
import com.smartsorovnoma.presentation.common.AppTopBar
import com.smartsorovnoma.presentation.common.SettingsClickItem
import com.smartsorovnoma.presentation.common.SettingsSection
import com.smartsorovnoma.presentation.common.SettingsToggleItem
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(R.string.settings))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ko'rinish
            SettingsSection(title = stringResource(R.string.appearance)) {
                SettingsToggleItem(
                    title = stringResource(R.string.dark_mode),
                    subtitle = stringResource(R.string.dark_mode_desc),
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeChange
                )
                
                SettingsClickItem(
                    title = stringResource(R.string.language),
                subtitle = currentLanguageName(),
                    icon = Icons.Default.Settings, // Language icon alternative if needed
                    onClick = { showLanguageDialog = true }
                )
            }
            
            // Bildirishnomalar
            SettingsSection(title = stringResource(R.string.notifications)) {
                SettingsToggleItem(
                    title = stringResource(R.string.push_notifications),
                    subtitle = stringResource(R.string.push_notifications_desc),
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
            
            // Ilova haqida
            SettingsSection(title = stringResource(R.string.about_app)) {
                SettingsClickItem(
                    title = stringResource(R.string.version),
                    subtitle = BuildConfig.VERSION_NAME,
                    icon = Icons.Default.Info,
                    onClick = { }
                )
                SettingsClickItem(
                    title = stringResource(R.string.privacy_policy),
                    subtitle = stringResource(R.string.privacy_policy_desc),
                    icon = Icons.Default.Info, // Or Lock icon if available
                    onClick = { 
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW, 
                            android.net.Uri.parse("https://smartsorovnoma.uz/privacy")
                        )
                        context.startActivity(intent)
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { languageCode ->
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
                AppCompatDelegate.setApplicationLocales(appLocale)
                showLanguageDialog = false
            }
        )
    }
}

@Composable
fun LanguageSelectionDialog(
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf(
        stringResource(R.string.language_uzbek) to "uz",
        stringResource(R.string.language_russian) to "ru",
        stringResource(R.string.language_english) to "en"
    )
    val currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags().split("-")[0]
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.select_language)) },
        text = {
            Column {
                languages.forEach { (name, code) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(code) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLang.contains(code),
                            onClick = { onLanguageSelected(code) }
                        )
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    )
}

@Composable
private fun currentLanguageName(): String {
    val locales = AppCompatDelegate.getApplicationLocales()
    if (locales.isEmpty) return stringResource(R.string.language_uzbek)
    
    return when (locales.get(0)?.language) {
        "uz" -> stringResource(R.string.language_uzbek)
        "ru" -> stringResource(R.string.language_russian)
        "en" -> stringResource(R.string.language_english)
        else -> stringResource(R.string.language_uzbek)
    }
}
