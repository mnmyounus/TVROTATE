package com.reverselandscape.tv

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class RotationAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "RotationAccessibility"
        const val ROTATION_REVERSE_LANDSCAPE = 2
        var isServiceRunning = false
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true
        Log.d(TAG, "Accessibility Service Connected")
        
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
        Log.d(TAG, "Accessibility Service Destroyed")
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

            // Disable auto-rotation first
            Settings.System.putInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0 // Disable auto-rotate
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
        return super.onUnbind(intent)
    }
}
