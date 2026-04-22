package com.reverselandscape.tv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
        private const val ROTATION_REVERSE_LANDSCAPE = 2
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            Log.d(TAG, "Boot completed - attempting to apply rotation")
            
            // Try to apply rotation directly if we have permission
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(context)) {
                        applyRotation(context)
                    } else {
                        Log.w(TAG, "No WRITE_SETTINGS permission")
                    }
                } else {
                    applyRotation(context)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error applying rotation on boot", e)
            }
        }
    }

    private fun applyRotation(context: Context) {
        try {
            // Disable auto-rotation
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            )

            // Force reverse landscape
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.USER_ROTATION,
                ROTATION_REVERSE_LANDSCAPE
            )

            Log.d(TAG, "Rotation applied successfully on boot")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to apply rotation", e)
        }
    }
}
