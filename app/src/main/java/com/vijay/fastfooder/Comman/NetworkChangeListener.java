package com.vijay.fastfooder.Comman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.vijay.fastfooder.R;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        if(!NetworkDetails.isConnectedToInternet(context))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder (context);
            View layout_dialog = LayoutInflater.from (context).inflate (R.layout.check_internet_connection_dialog,null);
            ad.setView (layout_dialog);

            AppCompatButton btnRetry = layout_dialog.findViewById (R.id.acbtnCheckInternetConnection);
            AlertDialog alertDialog = ad.create ();
            alertDialog.show ();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside (false);



//            LayoutInflater = call layout

            btnRetry.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss ();
                    onReceive (context, intent);
                }
            });
        }
        else {
            Toast.makeText (context, "Your Internet is Connected ", Toast.LENGTH_SHORT).show ();
        }
    }
}

// Activity => NetworkChangeListener => BroadCastReciever => onReceive => NetworkDetails => isConnectedToInternet =>ConnectivityManager
// Services => Long Running Operation Performs in background
// Broadcast Receiver => System Communication and App Communicate
//Content Provider => Data Store, Data Pass
