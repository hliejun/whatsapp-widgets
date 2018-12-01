package com.hliejun.dev.whatsappwidgets;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.support.design.widget.Snackbar;

import android.view.View;

import android.widget.TextView;

public class NotificationManager {

    private static final int SNACKBAR_SIZE_TEXT = 12;
    private static final int SNACKBAR_SIZE_BUTTON = 11;

    public static void showDialog(Context context,
                                  String title,
                                  String message,
                                  String acceptLabel,
                                  DialogInterface.OnClickListener acceptListener,
                                  String neglectLabel,
                                  DialogInterface.OnClickListener neglectListener,
                                  String cancelLabel,
                                  DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);

        if (acceptLabel != null && acceptListener != null) {
            dialog.setPositiveButton(acceptLabel, acceptListener);
        }

        if (neglectLabel != null && neglectListener != null) {
            dialog.setNegativeButton(neglectLabel, neglectListener);
        }

        if (cancelListener != null) {
            dialog.setNeutralButton(cancelLabel, cancelListener);
        }

        dialog.create().show();
    }

    public static void showSnackbar(Context context,
                                    String message,
                                    String actionLabel,
                                    View.OnClickListener actionListener) {
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                                    .setAction(actionLabel, actionListener);
        styleSnackbar(snackbar);
        snackbar.show();
    }

    private static void styleSnackbar(Snackbar snackbar) {
        TextView snackbarTextView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbarTextView.setTextSize(SNACKBAR_SIZE_TEXT);
        TextView snackbarActionTextView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackbarActionTextView.setTextSize(SNACKBAR_SIZE_BUTTON);
    }

}
