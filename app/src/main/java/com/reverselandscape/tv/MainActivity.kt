package com.reverselandscape.tv

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

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
        if (isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "Accessibility Service enabled! Rotation active.", Toast.LENGTH_LONG).show()
        } else {
            showPermissionDeniedDialog("Accessibility Service is required for rotation control")
        }
        updateServiceStatus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        startButton.setOnClickListener {
            checkPermissionsAndStart()
        }

        stopButton.setOnClickListener {
            disableRotation()
        }

        updateServiceStatus()
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    private fun checkPermissionsAndStart() {
        // Check WRITE_SETTINGS permission first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                showWriteSettingsDialog()
                return
            }
        }

        // Then check Accessibility Service
        checkAccessibilityAndStart()
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
            Toast.makeText(this, "Rotation is active!", Toast.LENGTH_SHORT).show()
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

    private fun disableRotation() {
        AlertDialog.Builder(this)
            .setTitle("Disable Rotation Control")
            .setMessage("To disable rotation control:\n\n" +
                    "1. Go to Settings → Accessibility\n" +
                    "2. Find 'Reverse Landscape TV'\n" +
                    "3. Turn it OFF\n\n" +
                    "Or restore auto-rotation in Display settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                openAccessibilitySettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPermissionDeniedDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage(message)
            .setPositiveButton("Try Again") { _, _ ->
                checkPermissionsAndStart()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateServiceStatus() {
        val isRunning = isAccessibilityServiceEnabled()
        statusText.text = if (isRunning) {
            getString(R.string.status_running)
        } else {
            getString(R.string.status_stopped)
        }
        
        startButton.isEnabled = !isRunning
        stopButton.isEnabled = isRunning
    }
}
