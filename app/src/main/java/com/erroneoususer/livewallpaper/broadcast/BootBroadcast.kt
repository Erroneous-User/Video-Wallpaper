package com.erroneoususer.livewallpaper.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.erroneoususer.livewallpaper.service.VideoLiveWallpaperService

internal class BootBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, VideoLiveWallpaperService::class.java)
        context.startService(service)
    }
}
