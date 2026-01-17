package com.k4wa1.wuwatool

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku

// --- ACTIVIDAD PRINCIPAL ---
class MainActivity : ComponentActivity() {
    private val requestPermissionResultListener = Shizuku.OnRequestPermissionResultListener { _, _ ->
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shizuku.addRequestPermissionResultListener(requestPermissionResultListener)
        
        setContent {
            val context = LocalContext.current
            val prefs = remember { context.getSharedPreferences("wuwa_prefs", Context.MODE_PRIVATE) }
            val systemDark = isSystemInDarkTheme()
            
            var isDarkTheme by remember { 
                mutableStateOf(prefs.getBoolean("dark_theme", systemDark)) 
            }

            fun toggleTheme(isDark: Boolean) {
                isDarkTheme = isDark
                prefs.edit().putBoolean("dark_theme", isDark).apply()
            }

            val colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            
            MaterialTheme(colorScheme = colorScheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainAppNavigation(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { toggleTheme(it) }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
    }
}

// --- NAVEGACIÓN ---
@Composable
fun MainAppNavigation(isDarkTheme: Boolean, onThemeToggle: (Boolean) -> Unit) {
    var hasShizukuPermission by remember { mutableStateOf(false) }
    var hasStoragePermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (true) {
            try {
                val shizukuOk = Shizuku.pingBinder() && 
                    Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
                
                val storageOk = ContextCompat.checkSelfPermission(
                    context, 
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

                if (shizukuOk != hasShizukuPermission) hasShizukuPermission = shizukuOk
                if (storageOk != hasStoragePermission) hasStoragePermission = storageOk

            } catch (e: Exception) { 
                hasShizukuPermission = false 
            }
            delay(2000)
        }
    }

    if (hasShizukuPermission && hasStoragePermission) {
        DashboardScreen(isDarkTheme, onThemeToggle)
    } else {
        PermissionRequestScreen(
            shizukuOk = hasShizukuPermission,
            storageOk = hasStoragePermission
        )
    }
}

// --- PANTALLA DE PERMISOS ---
@Composable
fun PermissionRequestScreen(shizukuOk: Boolean, storageOk: Boolean) {
    val storageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.perm_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.perm_subtitle), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(40.dp))

        PermissionStatusItem(
            name = stringResource(R.string.perm_shizuku_title),
            desc = stringResource(R.string.perm_shizuku_desc),
            isOk = shizukuOk
        ) {
            try { Shizuku.requestPermission(0) } catch (e: Exception) {}
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        PermissionStatusItem(
            name = stringResource(R.string.perm_storage_title),
            desc = stringResource(R.string.perm_storage_desc),
            isOk = storageOk
        ) {
            storageLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}

@Composable
fun PermissionStatusItem(name: String, desc: String, isOk: Boolean, onRequest: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(desc, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (isOk) {
                Text(stringResource(R.string.status_ready), style = MaterialTheme.typography.bodyMedium, color = Color.Green)
            } else {
                Button(onClick = onRequest) { Text(stringResource(R.string.btn_allow)) }
            }
        }
    }
}

// --- DASHBOARD PRINCIPAL ---
@Composable
fun DashboardScreen(isDarkTheme: Boolean, onThemeToggle: (Boolean) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val prefs = remember { context.getSharedPreferences("wuwa_prefs", Context.MODE_PRIVATE) }
    val uriHandler = LocalUriHandler.current

    // Estados
    var showMenu by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showDeleteButtons by remember { mutableStateOf(prefs.getBoolean("show_delete", true)) }

    var selectedPath by remember { mutableStateOf(context.getString(R.string.selected_folder)) }
    var rootUri by remember { mutableStateOf<Uri?>(null) }
    var configFolders by remember { mutableStateOf<List<DocumentFile>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Dialogos
    var showDisclaimerDialog by remember { mutableStateOf(false) }
    var showBackupInfoDialog by remember { mutableStateOf(false) }
    var folderToDelete by remember { mutableStateOf<DocumentFile?>(null) }

    fun refreshFolders(uri: Uri) {
        val docFile = DocumentFile.fromTreeUri(context, uri)
        selectedPath = docFile?.name ?: context.getString(R.string.selected_folder)
        configFolders = docFile?.listFiles()
            ?.filter { file -> file.isDirectory && !file.name!!.startsWith(".") && file.name != "backup" }
            ?.toList() ?: emptyList()
    }

    LaunchedEffect(Unit) {
        val savedUriString = prefs.getString("saved_uri", null)
        val disclaimerShown = prefs.getBoolean("disclaimer_shown_v1", false)
        
        if (!disclaimerShown) showDisclaimerDialog = true

        if (savedUriString != null) {
            val uri = Uri.parse(savedUriString)
            try {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                rootUri = uri
                refreshFolders(uri)
            } catch (e: Exception) {
                selectedPath = context.getString(R.string.permission_lost)
            }
        } else {
            selectedPath = context.getString(R.string.no_folder_selected)
        }
    }

    LaunchedEffect(rootUri) {
        while (rootUri != null) {
            delay(10000)
            refreshFolders(rootUri!!)
        }
    }

    val folderLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                prefs.edit().putString("saved_uri", it.toString()).apply()
                rootUri = it
                refreshFolders(it)
                showBackupInfoDialog = true
            } catch (e: Exception) { }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        
        // --- DIALOGOS ---
        
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                title = { Text(stringResource(R.string.menu_settings)) },
                text = {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.settings_dark_mode))
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { onThemeToggle(it) }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.settings_show_delete))
                            Switch(
                                checked = showDeleteButtons,
                                onCheckedChange = { 
                                    showDeleteButtons = it
                                    prefs.edit().putBoolean("show_delete", it).apply()
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSettingsDialog = false }) { Text(stringResource(R.string.btn_close)) }
                }
            )
        }

        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                icon = { Icon(Icons.Default.Info, contentDescription = null) },
                title = { Text(stringResource(R.string.about_title)) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.about_desc), textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(stringResource(R.string.about_ai), style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { uriHandler.openUri("https://github.com/k4wai1") }) {
                            Text(stringResource(R.string.btn_github))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) { Text(stringResource(R.string.btn_close)) }
                }
            )
        }

        if (folderToDelete != null) {
            AlertDialog(
                onDismissRequest = { folderToDelete = null },
                icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                title = { Text(stringResource(R.string.dialog_delete_title)) },
                text = { Text(stringResource(R.string.dialog_delete_msg, folderToDelete?.name ?: "")) },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        onClick = {
                            val name = folderToDelete?.name
                            val msgDeleted = context.getString(R.string.msg_folder_deleted, name)
                            val msgError = context.getString(R.string.msg_delete_error, "")
                            try {
                                folderToDelete?.delete()
                                folderToDelete = null
                                rootUri?.let { refreshFolders(it) }
                                scope.launch { snackbarHostState.showSnackbar(msgDeleted) }
                            } catch (e: Exception) {
                                scope.launch { snackbarHostState.showSnackbar("$msgError ${e.message}") }
                            }
                        }
                    ) { Text(stringResource(R.string.btn_delete)) }
                },
                dismissButton = {
                    TextButton(onClick = { folderToDelete = null }) { Text(stringResource(R.string.btn_cancel)) }
                }
            )
        }

        if (showDisclaimerDialog) {
            AlertDialog(
                onDismissRequest = {},
                icon = { Icon(Icons.Default.Info, contentDescription = null) },
                title = { Text(stringResource(R.string.disclaimer_title)) },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(stringResource(R.string.disclaimer_risk_title), fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.disclaimer_risk_body))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(stringResource(R.string.disclaimer_howto_title), fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.disclaimer_howto_body))
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        showDisclaimerDialog = false
                        prefs.edit().putBoolean("disclaimer_shown_v1", true).apply()
                    }) { Text(stringResource(R.string.btn_understood)) }
                }
            )
        }

        if (showBackupInfoDialog) {
            AlertDialog(
                onDismissRequest = { showBackupInfoDialog = false },
                title = { Text(stringResource(R.string.backup_info_title)) },
                text = { Text(stringResource(R.string.backup_info_msg)) },
                confirmButton = { Button(onClick = { showBackupInfoDialog = false }) { Text(stringResource(R.string.btn_ok)) } }
            )
        }

        // --- UI PRINCIPAL ---
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_settings)) },
                                onClick = { showMenu = false; showSettingsDialog = true }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_about)) },
                                onClick = { showMenu = false; showAboutDialog = true }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.panel_title), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { rootUri?.let { refreshFolders(it) } }) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.btn_refresh))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.source_configs), style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedPath, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { folderLauncher.launch(null) }) {
                        Text(if (rootUri == null) stringResource(R.string.btn_select_folder) else stringResource(R.string.btn_change_folder))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            if (rootUri == null) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) { Text(stringResource(R.string.select_folder_hint), color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                if (configFolders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                stringResource(R.string.empty_list_title),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                stringResource(R.string.empty_list_desc),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            val msgRestored = stringResource(R.string.msg_backup_restored)
                            val msgMissing = stringResource(R.string.msg_backup_missing)
                            ConfigItemCard(
                                name = stringResource(R.string.item_backup_name),
                                description = stringResource(R.string.item_backup_desc),
                                isBackup = true,
                                showDelete = false,
                                onApply = {
                                    scope.launch {
                                        isLoading = true
                                        val backupPath = "/storage/emulated/0/.WuWa_Backup"
                                        val result = WuWaShell.applyConfig(backupPath)
                                        snackbarHostState.showSnackbar(if (result) msgRestored else msgMissing)
                                        isLoading = false
                                    }
                                },
                                onDelete = null
                            )
                        }

                        items(configFolders) { folder ->
                            val msgBackupErr = stringResource(R.string.msg_backup_create_error)
                            val msgApplied = stringResource(R.string.msg_config_applied, folder.name ?: "")
                            val msgError = stringResource(R.string.msg_config_error)

                            ConfigItemCard(
                                name = folder.name ?: "???",
                                description = stringResource(R.string.item_config_desc),
                                isBackup = false,
                                showDelete = showDeleteButtons,
                                onApply = {
                                    scope.launch {
                                        isLoading = true
                                        val basePath = WuWaShell.getRawPathFromUri(rootUri!!)
                                        val configPath = "$basePath/${folder.name}"

                                        val backupOk = WuWaShell.ensureBackupExists()
                                        
                                        if (!backupOk) {
                                            snackbarHostState.showSnackbar(msgBackupErr)
                                        } else {
                                            val applied = WuWaShell.applyConfig(configPath)
                                            snackbarHostState.showSnackbar(if (applied) msgApplied else msgError)
                                        }
                                        isLoading = false
                                    }
                                },
                                onDelete = { folderToDelete = folder }
                            )
                        }
                    }
                }
            }
        }
        
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha=0.5f)), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ConfigItemCard(
    name: String, 
    description: String, 
    isBackup: Boolean,
    showDelete: Boolean,
    onApply: () -> Unit,
    onDelete: (() -> Unit)?
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isBackup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(description, style = MaterialTheme.typography.bodySmall)
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isBackup && showDelete && onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Button(onClick = onApply, contentPadding = PaddingValues(horizontal = 12.dp)) {
                    Text(if (isBackup) stringResource(R.string.btn_restore) else stringResource(R.string.btn_apply))
                }
            }
        }
    }
}

// =========================================================
// === SHELL (Sin cambios) ===
// =========================================================
object WuWaShell {
    private const val GAME_CONFIG_PATH = "/storage/emulated/0/Android/data/com.kurogame.wutheringwaves.global/files/UE4Game/Client/Client/Saved/Config/Android"
    private const val BACKUP_ROOT_PATH = "/storage/emulated/0/.WuWa_Backup"

    private fun exec(command: String): Boolean {
        return try {
            val method = Shizuku::class.java.getDeclaredMethod(
                "newProcess", 
                Array<String>::class.java, 
                Array<String>::class.java, 
                String::class.java
            )
            method.isAccessible = true
            
            val process = method.invoke(null, arrayOf("sh", "-c", command), null, null) as Process
            process.waitFor() == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getRawPathFromUri(uri: Uri): String {
        val path = uri.path ?: return ""
        val split = path.split(":")
        return if (split.size > 1) {
            "/storage/emulated/0/${split[1]}"
        } else {
            ""
        }
    }

    suspend fun ensureBackupExists(): Boolean = withContext(Dispatchers.IO) {
        if (exec("[ -d \"$BACKUP_ROOT_PATH\" ]")) {
            return@withContext true
        }
        
        exec("mkdir -p \"$BACKUP_ROOT_PATH\"")
        
        val cmd = "cp -f \"$GAME_CONFIG_PATH/Engine.ini\" \"$BACKUP_ROOT_PATH/\"; " +
                  "cp -f \"$GAME_CONFIG_PATH/GameUserSettings.ini\" \"$BACKUP_ROOT_PATH/\"; " +
                  "cp -f \"$GAME_CONFIG_PATH/DeviceProfiles.ini\" \"$BACKUP_ROOT_PATH/\""
        exec(cmd)
    }

    suspend fun applyConfig(sourceFolder: String): Boolean = withContext(Dispatchers.IO) {
        val cmd = "find \"$sourceFolder\" -name \"*.ini\" -exec cp -f {} \"$GAME_CONFIG_PATH/\" \\;"
        
        if (!exec("[ -d \"$GAME_CONFIG_PATH\" ]")) {
            exec("mkdir -p \"$GAME_CONFIG_PATH\"")
        }
        exec(cmd)
    }
}