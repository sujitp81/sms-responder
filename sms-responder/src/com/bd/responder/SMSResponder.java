package com.bd.responder;

import java.util.ArrayList;

import com.bd.responder.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.*;


public class SMSResponder extends Activity {
    /** Called when the activity is first created. */
	
	ArrayList<String> settings;
	DBAdapter db = new DBAdapter(this);
	String active = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        CheckBox activeCheck = (CheckBox)findViewById(R.id.CheckBox01);
        
        Button saveButton = (Button)findViewById(R.id.Button01);
        
        EditText general = (EditText)findViewById(R.id.EditText01);
        
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
				Toast.makeText(getApplicationContext(),"settings saved",Toast.LENGTH_LONG).show();
			}
			
		});

		db.open();
		//ArrayList<String> profiles = db.getProfiles();
		
		settings = db.getSettings();
		active = settings.get(3);
		general.setText(db.getMessage(active, "general"));
		Editable thingy = general.getText();
		general.setText(thingy.toString());
		
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
		db.open();
		db.saveSettings(settings);
		
		EditText general = (EditText)findViewById(R.id.EditText01);
		Editable seriously = general.getText();
		
		db.saveMessage(active, "general", seriously.toString());
		db.close();
	}
}