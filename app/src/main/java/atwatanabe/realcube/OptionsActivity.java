package atwatanabe.realcube;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.content.SharedPreferences;
/**
 * Created by Anthony on 4/16/2015.
 */
public class OptionsActivity extends ActionBarActivity
{
    private static final String PREFERENCES = "PreferencesFile";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        int cubeManipProgress = settings.getInt("cubeManip", 50),
            cubeRotateProgress = settings.getInt("cubeRotate", 50);


        final SeekBar manipBar = (SeekBar) findViewById(R.id.cubeManip);
        manipBar.setProgress(cubeManipProgress);

        final SeekBar rotateBar = (SeekBar) findViewById(R.id.cubeRotate);
        rotateBar.setProgress(cubeRotateProgress);

        Button defaultsButton = (Button)findViewById(R.id.restoreDefaultsButton);
        defaultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                manipBar.setProgress(50);
                rotateBar.setProgress(50);
            }
        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        SeekBar manipBar = (SeekBar) findViewById(R.id.cubeManip);
        SeekBar rotateBar = (SeekBar) findViewById(R.id.cubeRotate);

        editor.putInt("cubeManip", manipBar.getProgress());
        editor.putInt("cubeRotate", rotateBar.getProgress());
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }
}
