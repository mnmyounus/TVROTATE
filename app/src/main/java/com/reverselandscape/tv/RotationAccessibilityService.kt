package com.reverselandscape.tv

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class RotationAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "RotationAccessibility"
        const val ROTATION_0 = 0      // Normal (0°)
        const val ROTATION_90 = 1     // Landscape (90°)
        const val ROTATION_180 = 2    // Reverse (180°)
        const val ROTATION_270 = 3    // Reverse Portrait (270°)
        
        private const val PREFS_NAME = "rotation_prefs_mnm"
        private const val KEY_ORIGINAL_ROTATION = "original_rotation"
        private const val KEY_ORIGINAL_AUTO_ROTATE = "original_auto_rotate"
        private const val KEY_CURRENT_MODE = "current_mode"
        
        const val MODE_NORMAL = 0
        const val MODE_ROTATE_180 = 180
        
        var isServiceRunning = false
            private set
            
        var currentRotationMode = MODE_NORMAL
            private set
    }

    private lateinit var prefs: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        Log.d(TAG, "Accessibility Service Connected - Created by MNM YOUNUS")
        
        // Save original settings before changing
        saveOriginalSettings()
        
        // Restore last rotation mode
        currentRotationMode = prefs.getInt(KEY_CURRENT_MODE, MODE_NORMAL)
        applyRotationMode(currentRotationMode)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Monitor window changes and maintain rotation
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // Re-apply rotation on window changes
            applyRotationMode(currentRotationMode)
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        Log.d(TAG, "Accessibility Service Destroyed")
        
        // Restore original settings when service is disabled
        restoreOriginalSettings()
    }

    private fun saveOriginalSettings() {
        try {
            val currentRotation = Settings.System.getInt(
                contentResolver,
                Settings.System.USER_ROTATION,
                ROTATION_90
            )
            
            val currentAutoRotate = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            )
            
            prefs.edit().apply {
                putInt(KEY_ORIGINAL_ROTATION, currentRotation)
                putInt(KEY_ORIGINAL_AUTO_ROTATE, currentAutoRotate)
                apply()
            }
            
            Log.d(TAG, "Saved original settings - Rotation: $currentRotation, AutoRotate: $currentAutoRotate")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving original settings", e)
        }
    }

    fun restoreOriginalSettings() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Log.e(TAG, "No permission to write system settings")
                    return
                }
            }
            
            val originalRotation = prefs.getInt(KEY_ORIGINAL_ROTATION, ROTATION_90)
            val originalAutoRotate = prefs.getInt(KEY_ORIGINAL_AUTO_ROTATE, 0)
            
            // Restore auto-rotation setting
            Settings.System.putInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                originalAutoRotate
            )
            
            // Restore rotation
            Settings.System.putInt(
                contentResolver,
                Settings.System.USER_ROTATION,
                originalRotation
            )
            
            // Clear mode
            prefs.edit().putInt(KEY_CURRENT_MODE, MODE_NORMAL).apply()
            currentRotationMode = MODE_NORMAL
            
            Log.d(TAG, "Restored original settings")
        } catch (e: Exception) {
            Log.e(TAG, "Error restoring original settings", e)
        }
    }

    fun applyRotationMode(mode: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Log.e(TAG, "No permission to write system settings")
                    return
                }
            }

            // Disable auto-rotation to lock the rotation
            Settings.System.putInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            )

            // Apply the rotation based on mode
            val rotationValue = when (mode) {
                MODE_ROTATE_180 -> ROTATION_180  // 180 degrees
                else -> ROTATION_90  // Normal landscape (90 degrees)
            }

            Settings.System.putInt(
                contentResolver,
                Settings.System.USER_ROTATION,
                rotationValue
            )
            
            // Save current mode
            currentRotationMode = mode
            prefs.edit().putInt(KEY_CURRENT_MODE, mode).apply()

            Log.d(TAG, "Applied rotation mode: $mode (value: $rotationValue)")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error applying rotation mode", e)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isServiceRunning = false
        return super.onUnbind(intent)
    }
}
