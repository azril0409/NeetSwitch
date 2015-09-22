package com.neetswitch.sample.neetswitchsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.neetoffice.library.widget.neetswitch.NeetSwitch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NeetSwitch.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NeetSwitch neetSwitch1 = (NeetSwitch) findViewById(R.id.neetSwitch1);
        NeetSwitch neetSwitch2 = (NeetSwitch) findViewById(R.id.neetSwitch2);
        NeetSwitch neetSwitch3 = (NeetSwitch) findViewById(R.id.neetSwitch3);
        NeetSwitch neetSwitch4 = (NeetSwitch) findViewById(R.id.neetSwitch4);
        neetSwitch1.setOnCheckedChangeListener(this);
        neetSwitch2.setOnCheckedChangeListener(this);
        neetSwitch3.setOnClickListener(this);
        neetSwitch4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("TAG","onClick");
        NeetSwitch neetSwitch = (NeetSwitch) v;
        neetSwitch.toggle();
    }

    @Override
    public void onCheckedChanged(NeetSwitch neetSwitch, boolean isChecked) {
        Toast.makeText(this,String.format("onCheckedChanged isChecked : "+isChecked),Toast.LENGTH_SHORT).show();
    }
}
