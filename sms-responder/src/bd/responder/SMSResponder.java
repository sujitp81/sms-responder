package bd.responder;

import java.io.*;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class SMSResponder extends Activity {
    /** Called when the activity is first created. */
	
	ArrayList<String> settings;
	DBAdapter db = new DBAdapter(this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        CheckBox activeCheck = (CheckBox)findViewById(R.id.CheckBox01);
        
        Button saveButton = (Button)findViewById(R.id.Button01);
        
        TextView test = (TextView)findViewById(R.id.TextView01);
		TextView profile = (TextView)findViewById(R.id.TextView02);

		activeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton btn, boolean isChecked)
			{
				if(isChecked){
					settings.set(1, "1");
				}else{
					settings.set(1, "0");
				}
			}
		});
		
		saveButton.setOnClickListener(new CompoundButton.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				saveSettings();				
			}
			
		});
		
		db.open();
		ArrayList<String> profiles = db.getProfiles();
		
		profile.setText(profiles.get(0));

		settings = db.getSettings();
		test.setText(settings.get(0) + " " + settings.get(1));
		db.close();
		if(settings.get(1).equals("1"))
		{
			activeCheck.setChecked(true);
		}
		else
		{
			activeCheck.setChecked(false);
		}
		
    }
    
	private void saveSettings()
	{
		TextView test = (TextView)findViewById(R.id.TextView01);
		db.open();
		db.saveSettings(settings);
		db.close();
	}
}