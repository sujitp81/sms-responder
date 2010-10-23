package com.bd.responder;

import java.util.ArrayList;

import com.bd.responder.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;


public class SMSResponder extends Activity {
    /** Called when the activity is first created. */
	
	ArrayList<String> settings;
	String[] profilesArray;
	DBAdapter db = new DBAdapter(this);
	String active = "";
	ListView profList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        EditText general = (EditText)findViewById(R.id.EditText01);
        
        profList = (ListView)findViewById(R.id.ListView01);
        
        profList.setOnItemClickListener(new OnItemClickListener() {

       	@Override
       	public void onItemClick(AdapterView a, View v, int position, long id) {
       		String itemText = (String)profList.getItemAtPosition(position);
       		if(itemText.equals(settings.get(1)))
       		{
       			Toast.makeText(getApplicationContext(), "Profile " + itemText + " deactivated.", Toast.LENGTH_SHORT).show();
       			settings.set(1, "");
       		}else
       		{
       			Toast.makeText(getApplicationContext(), "Profile " + itemText + " activated.", Toast.LENGTH_SHORT).show();
       			settings.set(1, itemText);
       		}
   			saveProfile();
		}});
        
        registerForContextMenu(profList);
        
		db.open();
		ArrayList<String> profiles = db.getProfiles();
		settings = db.getSettings();
		general.setText(db.getMessage("default", "general"));
		db.close();
		
		profilesArray = (String[])profiles.toArray(new String[profiles.size()]);
		
		profList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,profilesArray));
		
		if(!settings.get(1).equals(""))
		{
			active = settings.get(1);
			for(int ctr = 0; ctr < profilesArray.length;ctr++)
			{
				
			}
		}
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	if(super.isFinishing())
    	{
    		//saveSettings();
    	}
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenuInfo menuInfo) {
      if (v.getId()==R.id.ListView01) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle(profilesArray[info.position]);
        String[] menuItems = new String[1];
        //menuItems[1] = "Edit";
    	//menuItems[2] = "Delete";
        for (int i = 0; i<menuItems.length; i++) {
        	if(profilesArray[info.position].equals(settings.get(1)))
        	{
        		menuItems[0] = "Deactivate";
        	}
        	else
        	{
        		menuItems[0] = "Activate";
        	}
        	menu.add(Menu.NONE, i, i, menuItems[i]);
        }
      }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
      String menuItemName = item.getTitle().toString();
      String listItemName = profilesArray[info.position];
      if(menuItemName.equals("Deactivate"))
      {
    	  settings.set(1, "");
    	  Toast.makeText(getApplicationContext(), "Profile " + listItemName + " deactivated.", Toast.LENGTH_SHORT).show();
    	  saveProfile();
      }else if(menuItemName.equals("Activate"))
      {
    	  settings.set(1, listItemName);
    	  Toast.makeText(getApplicationContext(), "Profile " + listItemName + " activated.", Toast.LENGTH_SHORT).show();
    	  saveProfile(); 
      }else if(menuItemName.equals("Edit"))
      {
    	  //Load profile into edit mode
      }else if(menuItemName.equals("Delete"))
      {
    	  //Delete profile
      }
      return true;
    }
    
	private void saveProfile()
	{
		EditText general = (EditText)findViewById(R.id.EditText01);
		
		db.open();
		db.saveSettings(settings);
		db.saveMessage("default", "general", general.getText().toString());
		db.close();
	}
}