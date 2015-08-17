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
        NeetSwitch neetSwitch = (NeetSwitch) findViewById(R.id.neetSwitch);
        neetSwitch.setOnCheckedChangeListener(this);
        neetSwitch.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
