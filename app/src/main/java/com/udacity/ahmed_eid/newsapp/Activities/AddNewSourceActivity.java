package com.udacity.ahmed_eid.newsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.ahmed_eid.newsapp.R;
import com.udacity.ahmed_eid.newsapp.Utilies.AppConstants;

public class AddNewSourceActivity extends AppCompatActivity {
    private Button btnNext;
    private TextView textClose ;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_source);
        setScreenSize();
        initializeUI();
        setTitle(R.string.add_source);

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                goTOMainScreen(radioButtonId,v);
            }
        });
    }
    private void initializeUI(){
        btnNext = findViewById(R.id.btn_add);
        radioGroup = findViewById(R.id.radio_group);
        textClose = findViewById(R.id.text_close);
    }

    public void goTOMainScreen(int radioButtonId,View view) {
        if (radioButtonId > 0) {
            Intent data = new Intent();
            data.putExtra(AppConstants.INTENT_RadioBtnKey, radioButtonId);
            setResult(AppConstants.REQUEST_CODE_ADD, data);
            finish();
            //startActivity(getIntent());
        } else {
             Snackbar.make(view, R.string.error_massage_notselect, Snackbar.LENGTH_LONG).show();
        }
    }

    public void setScreenSize(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.7));
    }
}
