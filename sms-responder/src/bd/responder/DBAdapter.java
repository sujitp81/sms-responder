package bd.responder;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
    //private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "autoresponse";
    private static final int DATABASE_VERSION = 1;
    private static final String PROFILE_TEMPLATE = " (responsetype text not null, id text, response text);";
    private static final String DATABASE_CREATE = 
    	"create table settings (setting text not null, value text);" +
    	"create table profiles (_id integer primary key autoincrement, profile text not null);" +
    	"create table default" + PROFILE_TEMPLATE;
        
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
            db.execSQL(DATABASE_CREATE);
            
            ContentValues initialValues = new ContentValues();
            initialValues.put("setting", "enabled");
            initialValues.put("value", "1");
            db.insert("settings", null, initialValues);
            
            initialValues.clear();
            initialValues.put("setting", "active");
            initialValues.put("value", "0");
            db.insert("settings", null, initialValues);
        	
            initialValues.clear();
            initialValues.put("responsetype", "general");
            initialValues.put("value", "I am unable to respond at this time.");
            db.insert("default", null, initialValues);

            initialValues.clear();
            initialValues.put("profile", "default");
            db.insert("profiles", null, initialValues);
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
    public String[] getProfiles()
    {
    	ArrayList<String> profilesList = new ArrayList<String>();
    	Cursor cursor = db.query("profiles", new String[] {"profile"}, null, null, null, null, null);
    	String[] profiles= (String[])profilesList.toArray();
    	return profiles;
    }
}