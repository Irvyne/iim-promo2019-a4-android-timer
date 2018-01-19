package ninja.irvyne.iimtimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast

class MainActivity : AppCompatActivity() {

    private var isCountDownActive = false
    private var currentSeekBarPosition = 0
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(TAG, "progress: " + progress.toString())
                updateTimer(progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d(TAG, "onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d(TAG, "onStopTrackingTouch")
            }

        })

        seekBar.max = 5 * 60

        /*
        btnTimer.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                v?.visibility = View.GONE
            }
        })
        */

        btnTimer.setOnClickListener { toggleTimer() }
    }

    @SuppressLint("SetTextI18n")
    fun updateTimer(progress: Int, fromUser: Boolean) {
        val minutes = progress / 60
        val seconds = progress % 60

        timer.text = "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"

        if (fromUser.not()) {
            seekBar.progress = progress
        }
    }

    fun resetTimer() {
        countDownTimer.cancel()

        isCountDownActive = false
        seekBar.isEnabled = true
        btnTimer.text = getString(R.string.start_timer)

        updateTimer(currentSeekBarPosition, false)
    }

    fun toggleTimer() {
        if (isCountDownActive) {
            resetTimer()
        } else {
            countDownTimer = object : CountDownTimer((seekBar.progress * 1000).toLong() + 100, 1000) {
                override fun onFinish() {
                    resetTimer()
                }

                override fun onTick(millisUntilFinished: Long) {
                    updateTimer((millisUntilFinished / 1000).toInt(), false)
                }
            }

            currentSeekBarPosition = seekBar.progress
            isCountDownActive = true
            seekBar.isEnabled = false
            btnTimer.text = getString(R.string.stop_timer)

            countDownTimer.start()
        }
    }

    companion object {
        internal const val TAG = "MainActivity"
    }
}
