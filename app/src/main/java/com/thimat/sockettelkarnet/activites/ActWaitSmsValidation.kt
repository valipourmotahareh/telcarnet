package com.thimat.sockettelkarnet.activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.thimat.sockettelkarnet.classes.SendSMS
import com.thimat.sockettelkarnet.classes.SessionManager
import com.thimat.sockettelkarnet.classes.ShowDialog
import com.thimat.sockettelkarnet.localDb.LocalDB
import com.thimat.sockettelkarnet.R
import com.thimat.sockettelkarnet.socket.Socket_Connect
import kotlinx.android.synthetic.main.act_wait_sms_validation.*


class ActWaitSmsValidation : AppCompatActivity() {
    var sessionManager: SessionManager? = null
    var context: Context? = null
    internal lateinit var sendSMS: SendSMS
    internal lateinit var localDB: LocalDB
    internal lateinit var showDialog: ShowDialog
    lateinit var countdowntimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_wait_sms_validation)
        context = applicationContext
        sessionManager = SessionManager(context)
        sendSMS = SendSMS(this@ActWaitSmsValidation)
        showDialog = ShowDialog(this@ActWaitSmsValidation)
        localDB = LocalDB(context)
        //--------------------------------

        countdowntimer=object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                txt_count_down_timer.text = "" + millisUntilFinished / 1000 +
                        getString(R.string.txt_wait_sms_validation_countdown)
            }

            override fun onFinish() {
                if (sessionManager!!.user.equals("")) {
                    linear_dont_recive_sms.visibility = View.VISIBLE
                }
            }
        }.start()


    }

    /** Called when the user taps the continue button */
    fun Btn_Continue(view: View) {
        val intent = Intent(this, ActMainActivity::class.java)
        startActivity(intent)
    }

    /** called when the user taps the btn_back_first_setting button */
    fun Btn_BackFirstSetting(view: View) {
        if (sessionManager!!.phonenumbr != "") {
            Socket_Connect.SendMessage("*ID*"+sessionManager!!.user,"*rebot",true)

            Socket_Connect.SendMessage("*ans*"+sessionManager!!.user+"*"+sessionManager!!.car_code +"*"+sessionManager!!.password +"*rebot"
                ,"*rebot",false)

            localDB.DeleteAll()
            val intentback = Intent(applicationContext, ActEntt::class.java)
            startActivity(intentback)
            sessionManager!!.setfirstrun(false)
            sessionManager!!.tcn_v200 = false
            sessionManager!!.tcn_v300 = false
            sessionManager!!.user = ""
            finish()
        } else {
            showDialog.ShowDialog(getString(R.string.dialog_no_phonenumber))
        }
    }

    /** called when the user taps the btn_sms_request_again button */
    fun Btn_SmsRequestAgain(view: View){

        Socket_Connect.SendMessage("*ID*"+sessionManager!!.user,"*pas?",true)

        Socket_Connect.SendMessage("*ans*"+sessionManager!!.user +"*"+sessionManager!!.car_code +"*"+sessionManager!!.password +"*pas?"
            ,"*pas?",false)

        countdowntimer.start()

    }

}
