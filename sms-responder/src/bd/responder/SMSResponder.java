package bd.responder;

import java.io.*;
import java.util.ArrayList;

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
        
        
        ArrayList<String> settings = new ArrayList<String>();
        
        CheckBox activeButton = (CheckBox)findViewById(R.id.CheckBox01);
        
        TextView test = (TextView)findViewById(R.id.TextView01);
		TextView profile = (TextView)findViewById(R.id.TextView02);
		
		db.open();
		ArrayList<String> profiles = db.getProfiles();
		
		profile.setText(profiles.get(0));
		
		activeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton btn, boolean isChecked)
			{
				
			}
		});
		
		loadSettings(db, settings);
		test.setText(settings.get(0));
		/*if(settings.get(1).equals("1"))
		{
			//test.setText("Active");
			activeButton.setChecked(true);
		}
		else
		{
			//test.setText("Inactive");
			activeButton.setChecked(false);
		}*/
		db.close();
    }
    
    private void loadSettings(DBAdapter db, ArrayList<String> settings)
    {
    	settings = db.getSettings();
    }

	private void saveSettings(DBAdapter db, ArrayList<String> settings)
	{

	}
}