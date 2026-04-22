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
        const val ROTATION_REVERSE_LANDSCAPE = 2
        const val ROTATION_LANDSCAPE = 1
        const val ROTATION_PORTRAIT = 0
        private const val PREFS_NAME = "rotation_prefs"
        private const val KEY_ORIGINAL_ROTATION = "original_rotation"
        private const val KEY_ORIGINAL_AUTO_ROTATE = "original_auto_rotate"
        
        var isServiceRunning = false
            private set
    }

    private lateinit var prefs: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        Log.d(TAG, "Accessibility Service Connected")
        
        // Save original settings before changing
        saveOriginalSettings()
        
        // Force rotation immediately when service connects
        forceReverseRotation()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Monitor window changes and maintain rotation
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
            event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            
            // Re-apply rotation on window changes
            forceReverseRotation()
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        Log.d(TAG, "Accessibility Service Destroyed - Restoring original rotation")
        
        // Restore original settings when service is disabled
        restoreOriginalSettings()
    }

    private fun saveOriginalSettings() {
        try {
            val currentRotation = Settings.System.getInt(
                contentResolver,
                Settings.System.USER_ROTATION,
                ROTATION_LANDSCAPE
            )
            
            val currentAutoRotate = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                1
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

    private fun restoreOriginalSettings() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Log.e(TAG, "No permission to write system settings")
                    return
                }
            }
            
            val originalRotation = prefs.getInt(KEY_ORIGINAL_ROTATION, ROTATION_LANDSCAPE)
            val originalAutoRotate = prefs.getInt(KEY_ORIGINAL_AUTO_ROTATE, 1)
            
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
            
            Log.d(TAG, "Restored original settings - Rotation: $originalRotation, AutoRotate: $originalAutoRotate")
        } catch (e: Exception) {
            Log.e(TAG, "Error restoring original settings", e)
        }
    }

    private fun forceReverseRotation() {
        try {
            // Check if we have permission to write settings
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Log.e(TAG, "No permission to write system settings")
                    return
                }
            }

            // Disable auto-rotation
            Settings.System.putInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            )

            // Force reverse landscape orientation (180 degrees)
            Settings.System.putInt(
                contentResolver,
                Settings.System.USER_ROTATION,
                ROTATION_REVERSE_LANDSCAPE
            )

            Log.d(TAG, "Rotation forced to reverse landscape")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error forcing rotation", e)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isServiceRunning = false
        // Restore settings when service unbinds
        restoreOriginalSettings()
        return super.onUnbind(intent)
    }
}
