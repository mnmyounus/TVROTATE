package com.reverselandscape.tv

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var creatorText: TextView
    private lateinit var rotate0Button: Button
    private lateinit var rotate180Button: Button
    private lateinit var factoryResetButton: Button

    private val writeSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                checkAccessibilityAndStart()
            } else {
                showPermissionDeniedDialog("Write Settings permission is required")
            }
        }
    }

    private val accessibilityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        updateServiceStatus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        creatorText = findViewById(R.id.creatorText)
        rotate0Button = findViewById(R.id.rotate0Button)
        rotate180Button = findViewById(R.id.rotate180Button)
        factoryResetButton = findViewById(R.id.factoryResetButton)

        rotate0Button.setOnClickListener {
            applyRotation(RotationAccessibilityService.MODE_NORMAL, "Normal (0°)")
        }

        rotate180Button.setOnClickListener {
            applyRotation(RotationAccessibilityService.MODE_ROTATE_180, "Rotate 180°")
        }

        factoryResetButton.setOnClickListener {
            performFactoryReset()
        }

        updateServiceStatus()
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    private fun applyRotation(mode: Int, modeName: String) {
        // Check WRITE_SETTINGS permission first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                showWriteSettingsDialog()
                return
            }
        }

        // Check Accessibility Service
        if (!isAccessibilityServiceEnabled()) {
            showAccessibilityDialog()
            return
        }

        // Apply rotation via system settings directly
        try {
            Settings.System.putInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0 // Disable auto-rotate
            )

            val rotationValue = when (mode) {
                RotationAccessibilityService.MODE_ROTATE_180 -> 2 // 180 degrees
                else -> 1 // Normal landscape (90 degrees)
            }

            Settings.System.putInt(
                contentResolver,
                Settings.System.USER_ROTATION,
                rotationValue
            )

            // Save mode to preferences
            getSharedPreferences("rotation_prefs_mnm", Context.MODE_PRIVATE)
                .edit()
                .putInt("current_mode", mode)
                .apply()

            Toast.makeText(this, "✓ Applied: $modeName", Toast.LENGTH_SHORT).show()
            updateServiceStatus()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun performFactoryReset() {
        AlertDialog.Builder(this)
            .setTitle("Factory Reset")
            .setMessage("This will:\n\n" +
                    "✓ Reset rotation to default\n" +
                    "✓ Clear all saved settings\n" +
                    "✓ Restore original configuration\n\n" +
                    "Everything created by this app will be removed.\n\n" +
                    "Continue?")
            .setPositiveButton("Factory Reset") { _, _ ->
                executeFactoryReset()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun executeFactoryReset() {
        try {
            // Check if we have permission to write settings
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Toast.makeText(this, "Need Write Settings permission first", Toast.LENGTH_LONG).show()
                    requestWriteSettingsPermission()
                    return
                }
            }

            // Reset to default settings
            Settings.System.putInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0 // Keep auto-rotate off for consistency
            )

            Settings.System.putInt(
                contentResolver,
                Settings.System.USER_ROTATION,
                1 // Normal landscape
            )

            // Clear SharedPreferences
            getSharedPreferences("rotation_prefs_mnm", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            Toast.makeText(this, "✓ Factory Reset Complete!\nRotation restored to default", Toast.LENGTH_LONG).show()
            updateServiceStatus()
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showWriteSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("This app needs permission to modify system settings to control screen rotation.\n\nPlease enable 'Modify system settings' on the next screen.")
            .setPositiveButton("Grant Permission") { _, _ ->
                requestWriteSettingsPermission()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:$packageName")
            )
            writeSettingsLauncher.launch(intent)
        }
    }

    private fun checkAccessibilityAndStart() {
        if (!isAccessibilityServiceEnabled()) {
            showAccessibilityDialog()
        } else {
            Toast.makeText(this, "Rotation service is ready!", Toast.LENGTH_SHORT).show()
            updateServiceStatus()
        }
    }

    private fun showAccessibilityDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Accessibility Service")
            .setMessage("To control screen rotation, please enable the Accessibility Service:\n\n" +
                    "1. Tap 'Open Settings'\n" +
                    "2. Find 'Reverse Landscape TV'\n" +
                    "3. Turn it ON\n" +
                    "4. Confirm the permission")
            .setPositiveButton("Open Settings") { _, _ ->
                openAccessibilitySettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        accessibilityLauncher.launch(intent)
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        
        val expectedComponentName = "${packageName}/${RotationAccessibilityService::class.java.name}"
        
        return enabledServices.any { service ->
            service.resolveInfo.serviceInfo.let { 
                "${it.packageName}/${it.name}" == expectedComponentName
            }
        }
    }

    private fun showPermissionDeniedDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage(message)
            .setPositiveButton("Try Again") { _, _ ->
                checkAccessibilityAndStart()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateServiceStatus() {
        val isRunning = isAccessibilityServiceEnabled()
        val prefs = getSharedPreferences("rotation_prefs_mnm", Context.MODE_PRIVATE)
        val currentMode = prefs.getInt("current_mode", RotationAccessibilityService.MODE_NORMAL)
        
        statusText.text = if (isRunning) {
            val modeText = when (currentMode) {
                RotationAccessibilityService.MODE_ROTATE_180 -> "180°"
                else -> "Normal"
            }
            "Status: Active ($modeText)"
        } else {
            "Status: Not Active"
        }
        
        // Enable buttons only when service is active
        rotate0Button.isEnabled = isRunning
        rotate180Button.isEnabled = isRunning
        factoryResetButton.isEnabled = true // Always enabled
        
        // Visual feedback for active mode
        if (isRunning) {
            val isNormalMode = currentMode == RotationAccessibilityService.MODE_NORMAL
            val is180Mode = currentMode == RotationAccessibilityService.MODE_ROTATE_180
            
            rotate0Button.alpha = if (isNormalMode) 1.0f else 0.6f
            rotate180Button.alpha = if (is180Mode) 1.0f else 0.6f
        } else {
            rotate0Button.alpha = 0.5f
            rotate180Button.alpha = 0.5f
        }
    }
}
