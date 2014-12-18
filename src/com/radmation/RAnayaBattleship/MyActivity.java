package com.radmation.RAnayaBattleship;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.Header;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyActivity extends BaseActivity {

    private EditText username, password;
    private Button login, getUsers, logout;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Spinner ships, direction;


    List<String> userList = new ArrayList<String>();
    ListView userListView;
    ArrayAdapter adapter = null;



    //moved to base activity made public should still work
    List<String> userShipsNameList = new ArrayList<String>();
    ArrayAdapter adapterShips = null;

    ArrayAdapter adapterDirections = null;


    String base64EncodedCredentials = "";

    String loginUrl = "http://battlegameserver.com/api/v1/login.json";
    String logoutUrl = "http://battlegameserver.com/api/v1/logout.json";

    //String usersUrl = "http://battlegameserver.com/api/v1/available_users.json";  //this is usually empty
    String usersUrl = "http://battlegameserver.com/api/v1/all_users.json"; //using all users for test

    String shipsUrl = "http://battlegameserver.com/api/v1/available_ships.json";
    String directionsUrl = "http://battlegameserver.com/api/v1/available_directions.json";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        username = (EditText) findViewById(R.id.edtUsername);
        password = (EditText) findViewById(R.id.edtPassword);
        login = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getUsers = (Button)findViewById(R.id.btnGetUsers);
        logout = (Button)findViewById(R.id.btnLogout);
        imageView = (ImageView) findViewById(R.id.imageView);
        ships = (Spinner) findViewById(R.id.spinnerShips);
        direction = (Spinner) findViewById(R.id.spinnerDirection);

        userListView = (ListView)findViewById( R.id.listView );

    }

    public void loginToServer(View view) {
        try {
            String credentials = username.getText().toString() + ":" + password.getText().toString();
            base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            ServerRequest serverRequest = new ServerRequest(loginUrl, BaseActivity.ServerCommands.LOGIN);
            GetJSONAsync jsonAsync = new GetJSONAsync();
            jsonAsync.execute(new ServerRequest[]{serverRequest}); //passing the url and the command to method
        } catch (Exception e){
            e.printStackTrace();
            toastIt("Log in failed - check your credentials");
        }

    }

    public void getShips() {
        ServerRequest serverRequest = new ServerRequest(shipsUrl, BaseActivity.ServerCommands.GET_SHIPS);
        GetJSONAsync jsonAsync = new GetJSONAsync();
        jsonAsync.execute(new ServerRequest[]{serverRequest}); //passing the url and the command to method
    }
    public void getDirections() {
        ServerRequest serverRequest = new ServerRequest(directionsUrl, BaseActivity.ServerCommands.GET_DIRECTIONS);
        GetJSONAsync jsonAsync = new GetJSONAsync();
        jsonAsync.execute(new ServerRequest[]{serverRequest}); //passing the url and the command to method
    }

    public void getUsersOnClick( View v ) {
        progressBar.setVisibility( View.VISIBLE );
        ServerRequest sr = new ServerRequest( usersUrl, ServerCommands.GET_USERS );
        GetJSONAsync task = new GetJSONAsync();
        task.execute( new ServerRequest[] { sr } );
    }

    public void logoutOfServer(View view) {
        String credentials = username.getText().toString() + ":" + password.getText().toString();
        base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        ServerRequest sr = new ServerRequest( logoutUrl, ServerCommands.LOGOUT);
        GetJSONAsync task = new GetJSONAsync();
        task.execute( new ServerRequest[] { sr } );
    }

    //New Class Here
    private class GetJSONAsync extends AsyncTask<ServerRequest, Integer, ServerRequest> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected ServerRequest doInBackground(ServerRequest... params) {
            ServerRequest serverRequest = params[0];
            String requestUrl = serverRequest.getUrl();
            URL url = null;
            HttpURLConnection urlConnection = null;
            InputStream in = null;
            String jsonData = "";
            Integer progress = 0;

            try {
                url = new URL(requestUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + base64EncodedCredentials);
                urlConnection.connect();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                toastIt("ERROR");
            }

            publishProgress(progress += 33);

            try {
                if (urlConnection != null) {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
                publishProgress(progress += 33);
                jsonData = sb.toString();
                //parse data in JSON Object
                publishProgress(progress += 34);

            } catch (Exception e) {
                e.printStackTrace();
                toastIt("SERVER ERROR");
            } finally {
                urlConnection.disconnect();
            }
            serverRequest.setJsonDataResult(jsonData);
            return serverRequest;
        }

        @Override
        protected void onProgressUpdate(Integer... values) { //this method runs in UI thread
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ServerRequest sr) { // executes in UI thread
            progressBar.setVisibility(progressBar.getRootView().INVISIBLE);
            String jsonData = sr.getJsonDataResult();
            switch (sr.getCommand()) {


                case GET_USERS:
                    // Parse the jsonData into a JSONObject
                    try {
                        JSONArray allUsers = new JSONArray(jsonData);
                        float numUsers = allUsers.length();
                        for (int i = 0; i < numUsers; i++) {
                            JSONObject user = (JSONObject) allUsers.get(i);
                            Log.i("JSON", user.getString("first_name") + " " + user.getString("last_name") + "\n");
                            // TODO: Put data into a ListAdapter to display!!
                            userList.add(user.getString("avatar_name"));
                        }
                        adapter = new ArrayAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_1, userList);
                        userListView.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        toastIt(e.getLocalizedMessage());
                    }
                    progressBar.setVisibility(progressBar.getRootView().INVISIBLE);
                    break;

                case GET_SHIPS:
                    try {
                        String cow = "";
                        JSONObject allShips = new JSONObject(sr.getJsonDataResult());
                        for(int i = 0; i < allShips.names().length(); i++){
                            //make ships here
                            cow = "" + allShips.get(allShips.names().getString(i));
                            userShipsList.add(new Ship( allShips.names().getString(i), Integer.parseInt(cow) ));
                            userShipsNameList.add( allShips.names().getString(i) );

                        }
                        adapterShips = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, userShipsNameList);
                        adapterShips.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ships.setAdapter(adapterShips);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //getDirections();
                    break;

                case GET_DIRECTIONS:
                    try {
                        JSONObject directions = new JSONObject(sr.getJsonDataResult());
                        for(int i = 0; i < directions.names().length(); i++){
                            //get directions here
                            userDirections.add( directions.names().getString(i) );

                        }
                        adapterDirections = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, userDirections);
                        adapterDirections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        direction.setAdapter(adapterDirections);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case LOGOUT:
                    getUsers.setEnabled(false);
                    logout.setEnabled(false);
                    login.setEnabled(true);
                    imageView.setImageDrawable(null);
                    //get rid of list of available players
                    userList.removeAll(userList);
                    userListView.setAdapter(null);
                    //get rid of spinner directions
                    userDirections.clear();
                    adapterDirections.notifyDataSetChanged();
                    //get rid of spinner ships
                    userShipsNameList.clear();
                    adapterShips.notifyDataSetChanged();
                    //set some properties for the user
                    //need to contact server and update status - logout url already visited, but properties do not update - maybe create new instance of gamerobject
                    currentUser.setAvailable(false);//manually update properties
                    currentUser.setGaming(false);//manually update properties
                    currentUser.setOnline(false);//manually update properties
                    break;

                case LOGIN:
                    try {
                        JSONObject user = new JSONObject(sr.getJsonDataResult());
                        currentUser = new Gamer(
                                user.getString("first_name"),
                                user.getString("last_name"),
                                user.getString("email"),
                                user.getBoolean("online"),
                                user.getBoolean("available"),
                                user.getBoolean("gaming"),
                                user.getString("avatar_name"),
                                user.getString("avatar_image"),
                                user.getInt("level"),
                                user.getInt("coins"),
                                user.getInt("battles_won"),
                                user.getInt("battles_lost"),
                                user.getInt("battles_tied"),
                                user.getInt("experience_points")
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DownloadAvatarImage(currentUser.getAvatarPath());
                    getUsers.setEnabled(true);
                    logout.setEnabled(true);
                    login.setEnabled(false);
                    loginUsername = username.getText().toString();
                    loginPassword = password.getText().toString();
                    //getShips();
                    getDirections();
                    getShips();
                    break;
            }
        }

    }
//end class GetJSONAsync



    public void DownloadAvatarImage( String image ) {
        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedTypes = new String[]{"image/png", "image/jpeg", "image/jpg"};
        client.get("http://battlegameserver.com/" + image, new BinaryHttpResponseHandler(allowedTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] imageData) {
                // Successfully got a response
                Drawable d = new BitmapDrawable(getApplicationContext().getResources(), BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
                imageView.setImageDrawable(d);
            }

            @Override
            public void onFailure( int statusCode, Header[] headers, byte[] errorMsg, Throwable error ) {
                // Response failed :(
                try {
                    toastIt(new String(errorMsg, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}