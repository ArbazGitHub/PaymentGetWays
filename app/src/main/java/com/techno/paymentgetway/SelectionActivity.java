package com.techno.paymentgetway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        findViewById(R.id.btnAdyen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SelectionActivity.this, AdyenActivity.class);

            }
        });
        findViewById(R.id.btnPaytm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SelectionActivity.this, PaytmActivity.class);

            }
        });
    }

    public void startActivity(Context context, Class startClass) {
        try {
            Intent intent = new Intent(context, startClass);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
