package bd.responder;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter 
{
	private static final String TAG = "DBAdapter";
	
    private static final String DATABASE_NAME = "autoresponse";
    private static final int DATABASE_VERSION = 1;
    private static final String PROFILE_TEMPLATE = "(responsetype text not null, name text, response text)";
    private static final String DATABASE_CREATE1 = 
    	"create table settings (setting text not null, value text);";
    private static final String DATABASE_CREATE2 =	
    	"create table profiles (_id integer primary key autoincrement, profile text not null);";
    private static final String DATABASE_CREATE3 =
    	"create table new " + PROFILE_TEMPLATE + ";";
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
    	this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	Log.w(TAG, "DB Creation started");
        	db.execSQL(DATABASE_CREATE1);
        	db.execSQL(DATABASE_CREATE2);
        	db.execSQL(DATABASE_CREATE3);
            
            ContentValues initialValues = new ContentValues();
            initialValues.put("setting", "enabled");
            initialValues.put("value", "1");
            db.insert("settings", null, initialValues);
            
            initialValues.clear();
            initialValues.put("setting", "active");
            initialValues.put("value", "new");
            db.insert("settings", null, initialValues);
        	
            initialValues.clear();
            initialValues.put("responsetype", "general");
            initialValues.put("response", "I am unable to respond at this time.");
            db.insert("new", null, initialValues);

            initialValues.clear();
            initialValues.put("profile", "new");
            db.insert("profiles", null, initialValues);
            Log.w(TAG, "DB Creation finished");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    

    //---creates a new profile from PROFILE_TEMPLATE
    public void createProfile(String profile)
    {
    	db.execSQL("create table " + profile + PROFILE_TEMPLATE);
        ContentValues initialValues = new ContentValues();
        initialValues.put("responsetype", "general");
        initialValues.put("value", "I am unable to respond at this time.");
        db.insert(profile, null, initialValues);
    }
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---returns a list of all available profiles---
    public ArrayList<String> getProfiles()
    {
    	ArrayList<String> profilesList = new ArrayList<String>();
    	Cursor cursor = db.query("profiles", new String[] {"profile"}, null, null, null, null, null);
    	if (cursor.moveToFirst())
        {
            do {          
            	profilesList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

    	return profilesList;
    }
    
    public String sayHi()
    {
    	return "hi";
    }
    
    public ArrayList<String> getSettings()
    {
    	Log.w(TAG,"=================Get Settings=================");
    	ArrayList<String> settings = new ArrayList<String>();
    	Cursor cursor = db.query("settings", new String[] {"setting","value"}, null, null, null, null, null);
    	if (cursor.moveToFirst())
        {
    		Log.w(TAG,"=================STARTING READ=================");
    		Log.w(TAG, cursor.getString(0));
            do {
            	settings.add(cursor.getString(0));
            	settings.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

    	return settings;    	
    }
}