package com.radmation.RAnayaBattleship;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Radley on 11/9/2014.
 */
public class BaseActivity extends Activity {


    //public static Gamer currentUser = null;
    public final int gridTop = 50;
    public static String loginUsername = "";
    public static String loginPassword = "";
    public static int gameID = 0;
    public static Gamer currentUser = null;

    public static List<Ship> userShipsList = new ArrayList<Ship>();  //list of ship objects

    public List<String> userDirections = new ArrayList<String>();  //list of string Directions


    public enum ServerCommands {
        LOGIN, GET_USERS, GET_AVATAR, LOGOUT, GET_SHIPS, GET_DIRECTIONS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Restore preferences
        //SharedPreferences settings = getSharedPreferences( PREFS_NAME, 0 );
    }

    public static Drawable LoadImageFromWeb(String name, String url){
        try {
            InputStream is = (InputStream)new URL(url).getContent();
            return Drawable.createFromStream(is, name);
        } catch (Exception e) {
            return null ;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.mastermenu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.game :
                startActivity(new Intent(this, Game.class));
                break;
            case R.id.preferences :
                startActivity(new Intent(this, Preferences.class));
                break;
            case R.id.touch :
                //startActivity(new Intent(this, Touch.class));
                break;
            default:
                return super.onOptionsItemSelected( item );
        }
        return true;
    }


    public void savePreferences(){
/*        SharedPreferences setting = getSharedPreferences("BattleShipPrefs", 0);
        SharedPreferences.Editor editor = setting.edit();

        editor.putString("doctorEmail", DOCTOR_EMAIL);

        editor.commit(); // actually writes this to file*/

        //default calcs here
    }

/*    public File copyFileToExternal( String filename){
        File file = null;
        String newPath = Environment.getExternalStorageDirectory() + EXT_FOLDERNAME;
        try{
            Log.e("FileName", filename);
            Log.e("New Path", newPath);

            File f = new File( newPath );
            f.mkdirs(); //make all the folder structure in the path

            FileInputStream fin = openFileInput( filename );
            Log.e("File Name From Fin", fin.toString());


            FileOutputStream fout = new FileOutputStream( newPath + filename );
            Log.e("File Name From Fout", fout.toString());

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while( (len1 = fin.read(buffer)) != -1 ){
                fout.write(buffer, 0, len1);
            }
            fin.close();
            fout.close();
            file = new File( newPath + filename );
            if(file.exists())
                return file;

        }catch(Exception e){
            toastIt("Help! File Error " + e.toString());
        }
        return null;
    }*/

    //TOAST IT
    public void toastIt(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
