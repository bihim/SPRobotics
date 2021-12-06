package com.sprobotics.network.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class NetWorkChecker {
    static NetworkInfo wifi,mobile;
    public static Boolean check(Context c){

        ConnectivityManager cm=(ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        try{
            wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobile=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        if(wifi!=null && wifi.isConnected() && wifi.isAvailable()){

            // Toast.makeText(c, "wifi on", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(mobile!=null && mobile.isAvailable() && mobile.isConnected()){

            // Toast.makeText(c, "mobile data", Toast.LENGTH_SHORT).show();
            return true;
        }

        else{

            Toast.makeText(c, "No Network Connection", Toast.LENGTH_SHORT).show();
            ((Activity) c).finish();
            return false;
        }

    }

    public static AlertDialog displayMobileDataSettingsDialog(final Activity activity, final Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                context.startActivity(intent);
            }
        });

        builder.show();

        return builder.create();
    }

}
