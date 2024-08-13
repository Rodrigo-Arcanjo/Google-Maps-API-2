package com.example.mapinha;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class SnackBarUtil {

    private View view;
    private CoordinatorLayout coordinator;

    public SnackBarUtil(View view) {
        this.view = view;
    }

    public SnackBarUtil(CoordinatorLayout coordinator) {
        this.coordinator = coordinator;
    }

    public void createMessageError(String message, TabLayout mTabLayout, int position, EditText editText) {
        message = "\n" + message;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#e94e36"));

        TextView textView = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMaxLines(5);
        textView.setTypeface(null, Typeface.BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            textView.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbar.show();

        if (mTabLayout != null) {
            mTabLayout.getTabAt(position).select();
        }

        if (editText != null) {
            editText.requestFocus();
        }
    }

    public void createMessage(String message, View viewXd) {

        message = "\n" + message;

        Snackbar snackbar = Snackbar.make(viewXd, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        //sbView.setBackgroundColor(Color.parseColor("#FF018786"));
        sbView.setBackgroundColor(Color.parseColor("#e94e36"));

        TextView textView = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(17);
        textView.setMaxLines(2);
        //textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        //textView.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        textView.setTypeface(null, Typeface.BOLD);

        snackbar.show();

    }

    public void createMessageGreen(String message, TabLayout mTabLayout, int position, EditText editText) {
        message = "\n" + message;

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(19);
        textView.setMaxLines(5);
        textView.setTypeface(null, Typeface.BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            textView.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbar.show();

        if (mTabLayout != null) {
            mTabLayout.getTabAt(position).select();
        }

        if (editText != null) {
            editText.requestFocus();
        }
    }

    // Uso do botao somente na tela de Splash
    public Snackbar getSnackbarIndefinitivo(String titulo) {

        Snackbar snackbar = Snackbar
                .make(coordinator, titulo, Snackbar.LENGTH_INDEFINITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#e94e36"));
        TextView textView = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(17);
        textView.setMaxLines(2);
        textView.setTypeface(null, Typeface.BOLD);
        Button snackViewButton = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_action);
        snackViewButton.setTextColor(Color.WHITE);
        snackViewButton.setTextSize(13);
        snackViewButton.setTypeface(null, Typeface.BOLD);

        return snackbar;
    }
    
}
