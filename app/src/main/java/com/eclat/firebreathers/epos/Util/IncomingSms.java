package com.eclat.firebreathers.epos.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.eclat.firebreathers.epos.Modeclasses.Config;

import static android.content.ContentValues.TAG;

public class IncomingSms extends BroadcastReceiver {
    Config config = new Config();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] obj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < obj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) obj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    String verificationCode = getVerificationCode(message);
                    Log.e(TAG, "OTP received: " + verificationCode);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: " + verificationCode + ", message: " + message, duration);
                    toast.show();
                    try {
                        if (senderNum.equals("DM-PRABIN") || senderNum.equals("IM-PRABIN")) {
                            Config.msg = verificationCode;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(config.OTP_DELIMITER);
        if (index != -1) {
            int start = index + 3;
            int length = 6;
            code = message.substring(start, start + length);
            return code;
        } else return code;
    }
}
