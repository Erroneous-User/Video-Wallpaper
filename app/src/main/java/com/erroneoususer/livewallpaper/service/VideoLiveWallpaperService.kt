package com.erroneoususer.livewallpaper.service

import android.app.WallpaperManager
import android.content.*
import android.media.MediaPlayer
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import java.io.File
import java.io.IOException

class VideoLiveWallpaperService : WallpaperService() {
    internal inner class VideoEngine : Engine() {
        private var mediaPlayer: MediaPlayer? = null
        private var musicControlReceiver: BroadcastReceiver? = null
        // FIX: Initialize screenUnlockReceiver to null here
        private var screenUnlockReceiver: BroadcastReceiver? = null
        private var videoFilePath: String? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            // Load video file path from internal storage
            videoFilePath =
                this@VideoLiveWallpaperService.openFileInput("video_live_wallpaper_file_path")
                    .bufferedReader().readText()

            // Existing music control receiver for mute/unmute functionality
            val musicIntentFilter = IntentFilter(VIDEO_PARAMS_CONTROL_ACTION)
            registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val action = intent.getBooleanExtra(KEY_ACTION, false)
                    if (action) {
                        mediaPlayer?.setVolume(0f, 0f) // Mute
                    } else {
                        mediaPlayer?.setVolume(1.0f, 1.0f) // Unmute
                    }
                }
            }.also { musicControlReceiver = it }, musicIntentFilter)

            // Register BroadcastReceiver for screen unlock event
            val screenIntentFilter = IntentFilter(Intent.ACTION_USER_PRESENT)
            screenUnlockReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    // Check if the received action is ACTION_USER_PRESENT (phone unlocked)
                    if (intent?.action == Intent.ACTION_USER_PRESENT) {
                        mediaPlayer?.apply {
                            // Ensure the video starts from the beginning
                            seekTo(0)
                            // Start playing the video
                            start()
                        }
                    }
                }
            }
            registerReceiver(screenUnlockReceiver, screenIntentFilter)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            mediaPlayer = MediaPlayer().apply {
                setSurface(holder.surface) // Set the display surface for the video
                setDataSource(videoFilePath) // Set the video source
                setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
                prepare() // Prepare the media player for playback

                // Start the video momentarily to load the first frame and ensure the player is ready.
                start()
                // Then immediately pause it, so it waits for the explicit unlock trigger.
                pause()

                // Listener to pause the video when it finishes playing
                setOnCompletionListener { mp ->
                    mp.pause() // Pause at the last frame
                }
            }

            // Apply initial volume settings (mute/unmute) based on saved preference
            try {
                val file = File("$filesDir/unmute")
                if (file.exists()) {
                    mediaPlayer?.setVolume(1.0f, 1.0f) // Unmute
                } else {
                    mediaPlayer?.setVolume(0f, 0f) // Mute
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            // This method is called when the wallpaper becomes visible or invisible.
            // We only handle pausing when it becomes invisible to save battery.
            // Starting the video is now solely handled by the screenUnlockReceiver on ACTION_USER_PRESENT.
            if (!visible) {
                mediaPlayer?.pause() // Pause playback when wallpaper is not visible
            }
            // No 'start()' call when 'visible' is true, to prevent video playing
            // when merely closing an app (and not truly unlocking the phone).
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            // Stop and release media player resources when the surface is destroyed
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.release()
            mediaPlayer = null
        }

        override fun onDestroy() {
            super.onDestroy()
            // Release media player resources and unregister receivers when the engine is destroyed
            mediaPlayer?.release()
            mediaPlayer = null
            musicControlReceiver?.let { unregisterReceiver(it) } // Unregister music control receiver
            screenUnlockReceiver?.let { unregisterReceiver(it) } // Unregister screen unlock receiver
        }
    }

    override fun onCreateEngine(): Engine {
        return VideoEngine()
    }

    // Companion object for static helper functions (like mute, unmute, set wallpaper)
    companion object {
        const val VIDEO_PARAMS_CONTROL_ACTION = "com.erroneoususer.livewallpaper"
        private const val KEY_ACTION = "music"
        private const val ACTION_MUSIC_UNMUTE = false
        private const val ACTION_MUSIC_MUTE = true

        fun muteMusic(context: Context) {
            Intent(VIDEO_PARAMS_CONTROL_ACTION).apply {
                putExtra(KEY_ACTION, ACTION_MUSIC_MUTE)
            }.also { context.sendBroadcast(it) }
        }

        fun unmuteMusic(context: Context) {
            Intent(VIDEO_PARAMS_CONTROL_ACTION).apply {
                putExtra(KEY_ACTION, ACTION_MUSIC_UNMUTE)
            }.also { context.sendBroadcast(it) }
        }

        fun setToWallPaper(context: Context) {
            Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(context, VideoLiveWallpaperService::class.java)
                )
            }.also {
                context.startActivity(it)
            }
            try {
                // Clear any existing wallpaper settings if needed
                WallpaperManager.getInstance(context).clear()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
