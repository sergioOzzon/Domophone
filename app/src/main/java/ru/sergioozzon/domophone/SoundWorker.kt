package ru.sergioozzon.domophone

import android.content.Context
import android.media.MediaPlayer
import androidx.work.Worker
import androidx.work.WorkerParameters

class SoundWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {

        val player = MediaPlayer.create(context, R.raw.sound)
        player.start()
        player.setOnCompletionListener {
            it.release()
        }
        return Result.success()
    }


}