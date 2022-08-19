package app.doggy.stopwatchsample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import app.doggy.stopwatchsample.databinding.ActivityMainBinding
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    companion object {
        private const val delay: Long = 0L
        private const val period: Long = 100L
    }

    private lateinit var binding: ActivityMainBinding
    private var timer: Timer? = null
    private var count: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            timer?.cancel()

            timer = Timer()
            val timerTask = CountUpTimerTask()

            timer?.schedule(timerTask, delay, period)
            count = 0
            binding.timerText.text = "00:00.0"

            // ボタンを押せるかどうかを変更
            binding.startButton.isEnabled = false
            binding.stopButton.isEnabled = true
        }

        binding.stopButton.setOnClickListener {
            timer?.cancel()
            timer = null

            // ボタンを押せるかどうかを変更
            binding.startButton.isEnabled = true
            binding.stopButton.isEnabled = false
        }
    }

    private inner class CountUpTimerTask : TimerTask() {
        override fun run() {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                count += 1
                val mm: Long = count * 100 / 1000 / 60
                val ss: Long = count * 100 / 1000 % 60
                val ms: Long = (count * 100 - ss * 1000 - mm * 1000 * 60) / 100
                binding.timerText.text =
                    String.format(Locale.JAPAN, "%1$02d:%2$02d.%3$01d", mm, ss, ms)
            }
        }
    }
}
