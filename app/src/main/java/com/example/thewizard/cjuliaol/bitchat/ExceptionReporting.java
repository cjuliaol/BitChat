package com.example.thewizard.cjuliaol.bitchat;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by cjuliaol on 05-Sep-15.
 */
public class ExceptionReporting {


    public static void sendCrashReportFile (Context context) {
        String line="";
        String trace= null;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context
                            .openFileInput("stack.trace")));
            while((line = reader.readLine()) != null) {
                trace += line+"\n";
            }
        } catch(FileNotFoundException fnfe) {
// ...
        } catch(IOException ioe) {
// ...
        }
        if (trace != null) {

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            String subject = "Error report";
            String body =
                    "Mail this to cjuliaol@gmail.com: "+
                            "\n\n"+
                            trace+
                            "\n\n";

            sendIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[] {"cjuliaol@gmail.com"});
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendIntent.setType("message/rfc822");

            context.startActivity(
                    Intent.createChooser(sendIntent, "Title:"));

            context.deleteFile("stack.trace");

        }


    }

}
