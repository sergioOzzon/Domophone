package ru.sergioozzon.domophone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.button.MaterialButton
import ru.sergioozzon.domophone.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TWO: Long = 2 * 60 * 1000
        private const val FIVE: Long = 5 * 60 * 1000
        private const val TEN: Long = 10 * 60 * 1000
        private const val FIFTEEN: Long = 15 * 60 * 1000
        private const val THIRTEEN: Long = 30 * 60 * 1000
        private const val SIXTEEN: Long = 60 * 60 * 1000
    }

    private lateinit var binding: ActivityMainBinding

    private val durationsWithButton by lazy {
        with(binding) {
            listOf(
                Pair(TWO, twoMin),
                Pair(FIVE, fiveMin),
                Pair(TEN, tenMin),
                Pair(FIFTEEN, fifteenMin),
                Pair(THIRTEEN, thirteenMin),
                Pair(SIXTEEN, sixteenMin)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        durationsWithButton.forEach { pair ->
            pair.second.setOnClickListener {
                onDurationButtonClick(pair.second)
            }
        }
        binding.cancelButton.setOnClickListener {
            WorkManager.getInstance(this)
                .cancelAllWork()
            durationsWithButton.forEach {
                it.second.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            }
        }
    }

    private fun onDurationButtonClick(clickedButton: MaterialButton) {
        durationsWithButton.forEach {
            val button = it.second
            val duration = it.first
            if (button.id == clickedButton.id) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_700))
                setRepeatingAlarm(duration)
            } else {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            }
        }
    }

    private fun setRepeatingAlarm(duration: Long) {
        val work =
            PeriodicWorkRequestBuilder<SoundWorker>(duration, TimeUnit.MILLISECONDS)
                .build()

        doSoundWork(work)

        // TODO if duration < 15 min, then set alarm by AlarmManager
        /* val alarmManager =
             getSystemService(Context.ALARM_SERVICE) as? AlarmManager
         val alarmIntent =
             PendingIntent.getService(
                 this, 9877, intent,
                 PendingIntent.FLAG_NO_CREATE
             )

         alarmManager?.setInexactRepeating(
             ELAPSED_REALTIME_WAKEUP,
             SystemClock.elapsedRealtime(),
             duration,
             alarmIntent
         )*/
    }

    private fun doSoundWork(work: PeriodicWorkRequest) {
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "wuf-wuf",
                ExistingPeriodicWorkPolicy.REPLACE,
                work
            )
    }
}