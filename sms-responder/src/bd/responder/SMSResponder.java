package bd.responder;

import java.io.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;


public class SMSResponder extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DBAdapter db = new DBAdapter(this);
        
        
        byte[] settings = new byte[1];
        String FILENAME = "RRSettings";
        
        CheckBox activeButton = (CheckBox)findViewById(R.id.CheckBox01);
        
        TextView test = (TextView)findViewById(R.id.TextView01);
		TextView profile = (TextView)findViewById(R.id.TextView02);
		
		db.open();
		String[] profiles = db.getProfiles();
		
		//profile.setText(profiles[0]);
		
		activeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton btn, boolean isChecked)
			{
				
			}
		});
		
		loadSettings(FILENAME, settings);
		if(settings[0] == '1')
		{
			//test.setText("Active");
			activeButton.setChecked(true);
		}
		else
		{
			//test.setText("Inactive");
			activeButton.setChecked(false);
		}

    }
    
    private void loadSettings(String FILENAME, byte[] settings)
    {
    	TextView test = (TextView)findViewById(R.id.TextView01);
    	try{
			FileInputStream fis = openFileInput(FILENAME);
			fis.read(settings);
			fis.close();
			test.setText("Loaded");
		}catch(Exception e)
		{
			settings[0] = '1';
			saveSettings(FILENAME, settings);
		}
	}

	private void saveSettings(String FILENAME, byte[] settings)
	{
		TextView test = (TextView)findViewById(R.id.TextView01);
		try{
			FileOutputStream fos = openFileOutput(FILENAME, 0);
			fos.write(settings);
			fos.close();
			test.setText("Saved");
		}catch(Exception e)
		{
			test.setText("Error");
		}
	}
	}