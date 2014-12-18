package com.radmation.RAnayaBattleship;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.loopj.android.http.*;
import org.apache.http.*;
import org.json.*;



public class BattleShipAPI extends BaseActivity {
    final String loginUrl = "http://battlegameserver.com/api/v1/login.json";
    String userName, userPassword;


    /**
     * @param _userName
     * @param _password
     */
    BattleShipAPI( String _userName, String _password ) {
        userName = _userName;
        userPassword = _password;
    }


    public void challengeComputer() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth( loginUsername, loginPassword );
        String challengeUrl = "http://battlegameserver.com/api/v1/challenge_computer.json";
        client.get( challengeUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess( int statusCode, org.apache.http.Header[] headers, org.json.JSONObject game) {
                // Successfully got a response so parse JSON object
                Game.challengeComputerSuccess( game );
            }


            @Override
            public void onFailure( int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse ) {
                apiFailure( statusCode, headers, throwable, errorResponse );
            }
        } );
    }




    public void apiFailure( int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse ) {
        // Response failed :(String decodedResponse = ;
        try {
            toastIt( "Error getting game id");
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
}