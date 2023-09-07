package vc.ivan.grayscaletoggle

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import vc.ivan.grayscaletoggle.ui.theme.GrayscaleToggleTheme
import androidx.compose.material3.Card
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            toggleGrayscale(applicationContext.contentResolver)
            finishAndRemoveTask()
            return
        }

        val onExit = {
            this.finishAndRemoveTask()
        }
        setContent {
            GrayscaleToggleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ConfigurationCard(context = LocalContext.current, onExit = onExit)
                    }
                }
            }
        }
    }

    private fun toggleGrayscale(cr: ContentResolver) {
        if (Settings.Secure.getInt(cr, "accessibility_display_daltonizer_enabled") == 1) {
            Settings.Secure.putString(cr, "accessibility_display_daltonizer_enabled", "0")
            Settings.Secure.putString(cr, "accessibility_display_daltonizer", "-1")
        } else {
            Settings.Secure.putString(cr, "accessibility_display_daltonizer_enabled", "1")
            Settings.Secure.putString(cr, "accessibility_display_daltonizer", "0")
        }
    }
}

@Composable
fun ConfigurationCard(context: Context?, onExit: () -> Unit) {
    val adbText = "adb shell pm grant vc.ivan.grayscaletoggle android.permission.WRITE_SECURE_SETTINGS"
    Card(modifier = Modifier.padding(16.dp))  {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Permission required",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            val annotatedString = buildAnnotatedString {
                append("To use this application you need to grant the ")
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily.Monospace
                    )
                ) {
                    append("android.permission.WRITE_SECURE_SETTINGS")
                }
                append(" permission using ADB. ")
                pushStringAnnotation(
                    tag = "adb",
                    annotation = "https://developer.android.com/tools/adb")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Read how to install and configure ADB.")
                }
                pop()
            }
            val uriHandler = LocalUriHandler.current
            ClickableText(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "adb",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        uriHandler.openUri(it.item)
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface)) {
                Text(
                    text = adbText,
                    maxLines = 1,
                    //overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(10.dp)
                        .horizontalScroll(rememberScrollState()),
                    fontFamily = FontFamily.Monospace,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right) {
                TextButton(colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                    onClick = {
                    if (context != null) {
                        val clipDataText = "Download and configure ADB following the guide from " +
                                "https://developer.android.com/tools/adb " +
                                "then run in your terminal:\n" + adbText
                        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData: ClipData = ClipData.newPlainText("text", clipDataText)
                        clipboardManager.setPrimaryClip(clipData)
                    }
                }) {
                    Text("Copy")
                }

                TextButton(onClick = {
                    onExit()
                }) {
                    Text(color = MaterialTheme.colorScheme.secondary, text = "Exit")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigurationCardPreview() {
    GrayscaleToggleTheme {
        ConfigurationCard(context = null, onExit = {})
    }
}