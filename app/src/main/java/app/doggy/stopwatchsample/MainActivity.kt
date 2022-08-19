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
        private const val format = "%1$02d:%2$02d.%3$01d"
    }

    private lateinit var binding: ActivityMainBinding

    // タイマーの変数
    private var timer: Timer? = null

    // 秒数を数え上げるための変数（ミリ秒）
    private var count: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // スタートボタンの処理
        binding.startButton.setOnClickListener {
            // タイマーをスタート
            timer = Timer()
            val timerTask = CountUpTimerTask()
            timer?.schedule(timerTask, delay, period)

            // カウントをリセット
            count = 0

            // timer_text の表示をリセット
            binding.timerText.text = convertTimerText(0, 0, 0)

            // ボタンを押せるかどうかを変更
            binding.startButton.isEnabled = false
            binding.stopButton.isEnabled = true
        }

        // ストップボタンの処理
        binding.stopButton.setOnClickListener {
            // タイマーをキャンセル
            timer?.cancel()
            timer = null

            // ボタンを押せるかどうかを変更
            binding.startButton.isEnabled = true
            binding.stopButton.isEnabled = false
        }
    }

    // タイマーの具体的な処理の実装
    private inner class CountUpTimerTask : TimerTask() {
        override fun run() {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                count += 1
                val mm: Long = count * 100 / 1000 / 60
                val ss: Long = count * 100 / 1000 % 60
                val ms: Long = (count * 100 - ss * 1000 - mm * 1000 * 60) / 100
                binding.timerText.text = convertTimerText(mm, ss, ms)
            }
        }
    }

    // Long型（整数）の値を、 timer_text に表示するための文字列に変換する処理
    private fun convertTimerText(mm: Long, ss: Long, ms: Long) =
        String.format(Locale.JAPAN, format, mm, ss, ms)
}
