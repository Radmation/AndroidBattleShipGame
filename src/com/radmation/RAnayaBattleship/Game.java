package com.radmation.RAnayaBattleship;

import android.app.*;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.*;

import java.util.*;


public class Game extends BaseActivity {

    private SoundPool soundPool;
    private int rotationSound, rotationErrorSound, moveSound;
    boolean loaded = false;
    static boolean waiting = true, game_start = false;


    public static GameCell[][] gameGrid = new GameCell[11][11];
    View gameBoard = null;
    Button right, left, up, down, rotate, add_ship;
    List<Ship> ships = userShipsList;
    Activity customGrid = this;
    public static boolean gameStarted = false;
    int shipOn = 0;

    static int xCoord = 4, yCoord = 4;
    static int shipSize;
    static String shipName;
    int rotation;
    static Boolean north = true, south = false, east = false, west = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApp();
        right = (Button) findViewById(R.id.right);
        left = (Button) findViewById(R.id.left);
        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);
        rotate = (Button) findViewById(R.id.rotate);
        add_ship = (Button) findViewById(R.id.add_ship);

        setContentView(R.layout.gameboard);
        gameBoard = findViewById(R.id.boardView);
        gameStarted = false;
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        rotationSound = soundPool.load(this, R.raw.rotation, 1);
        rotationErrorSound = soundPool.load(this, R.raw.rotation_error, 1);
        moveSound = soundPool.load(this, R.raw.move, 1);

        // Getting the user sound settings

    }

    private void initializeApp() {
        // Initialize the gameGrid
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                gameGrid[x][y] = new GameCell();
            }
        }
        //gameGrid[1][1].setHas_ship(true);   // For debugging
        //gameGrid[1][2].setHit(true);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        // API Call to create a game.
        ChallengeComputer();
        waiting = true;
        addShips();

    }


    public static void challengeComputerSuccess(JSONObject game) {
        try {
            gameID = Integer.parseInt(game.getString("game_id"));
            waiting = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void ChallengeComputer() {
        new BattleShipAPI(loginUsername, loginPassword).challengeComputer();
    }


    public void addShips() {
        if(shipOn > 4) {
            game_start = true;
            for (int y = 0; y < 11; y++) {
                for (int x = 0; x < 11; x++) {
                    gameGrid[x][y].setPlacing_ship(false);
                }
            }
            setContentView(R.layout.gameboard);
            toastIt("GAME STARTED");  //FOR DEBUGGING
            return;
        }
        if(waiting){
            toastIt("waiting");
            return;
        }
        //create a ship object fro the ship list
        //store the name and size in static variables to use in other methods
        Ship ship = ships.get(shipOn); //int to increment to add next ship - comes from list
        shipName = ship.getShipName();
        shipSize = ship.getShipSize();

        //empties all "SHADOW CELLS"
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                gameGrid[x][y].setPlacing_ship(false);
            }
        }
        //find empty spot to draw new ship in for loops check if cell has ship, if cell has ship go to next if three y's have no ships then draw ship
        try {
            if (north) {
                for (int p = 0; p < shipSize; p++) {
                    gameGrid[xCoord][yCoord + p].setPlacing_ship(true);
                }
            }
            if (south) {
                for (int p = 0; p < shipSize; p++) {
                    gameGrid[xCoord][yCoord - p].setPlacing_ship(true);
                }
            }
            if (east) {
                for (int p = 0; p < shipSize; p++) {
                    gameGrid[xCoord - p][yCoord].setPlacing_ship(true);
                }
            }
            if (west) {
                for (int p = 0; p < shipSize; p++) {
                    gameGrid[xCoord + p][yCoord].setPlacing_ship(true);
                }
            }
            setContentView(R.layout.gameboard);
        } catch (Exception e) {
            e.printStackTrace();
            this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = actualVolume / maxVolume;
            if (loaded) {
                soundPool.play(moveSound, volume, volume, 1, 0, 1f); //this is the rotation sound
                Log.e("Test", "Played sound");
            }
        }
    }

    //AddShipClickButton
    public void onClickAddShip(View v) {
        boolean can_place = true; //set flag to true
        if(waiting){
            toastIt("waiting");
            return;
        }

        waiting = true;
        //check if 5 ships are places
        if (shipOn == 5) {
            //all ships added ready to play NOW
            //START GAME START GAME START GAME
            gameStarted = true;
        }
        if(gameStarted) {
            toastIt("Game Started");
            //make some method cal here to change view to attacking board
            return;
        }

        int placeY = 50;
        int placeX = 50;
        String num = "0";
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        //iterate through each cell, and if has placing ship there, then ship has ship there
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                //check to see if placing a ship is true
                if (gameGrid[x][y].getPlacing_ship() == true) {
                    //check if ship is already placed in cells
                    if (gameGrid[x][y].getHas_ship() == true) {
                        can_place = false; //if ship id there raise flag
                    }
                }
            }
        }
        if(can_place == true) { //if flag not raised then add ship
            for (int y = 0; y < 11; y++) {
                for (int x = 0; x < 11; x++) {
                    if (gameGrid[x][y].getPlacing_ship() == true) {

                        if (north) {
                            num = "0";
                            if (placeY > y)
                                placeY = y; //get the smallest number
                            placeX = x;
                        }
                        if (south) {
                            placeY = 0;
                            num = "4";
                            if (placeY < y)
                                placeY = y; //get the largest number
                            placeX = x;
                        }
                        if (east) {
                            num = "2";
                            placeX = 0;
                            if (placeX < x) //get the largest number
                                placeX = x; //get the largest number
                            placeY = y;
                        }

                        if (west) {
                            num = "6";
                            if (placeX > x) //get the smallest number
                                placeX = x; //get the smallest number
                            placeY = y;
                        }


                    }
                }
            } //end the Y loop outside loop

            String col = letters[placeX - 1]; //use in url
            //http://battlegameserver.com/api/v1/game/gameID/add_ship/ship_name/b/8/0.json;

            //build url
            String placeshipurl = "http://battlegameserver.com/api/v1/game/" + gameID + "/add_ship/" + shipName + "/" + col + "/" + placeY + "/" + num + ".json";

            AsyncHttpClient client = new AsyncHttpClient();
            client.setBasicAuth(loginUsername, loginPassword);
            toastIt("Placing Ship...");

                             /*north":0,
                            "east":2,
                            "south":4,
                            "west":6*/

            client.get(placeshipurl, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject game) {
                    // Successfully got a response so parse JSON object
                    JSONObject array = game;
                    try {
                        String worked = array.getString("status");
                        if (worked != "illegal ship placement") {
                            shipOn += 1;
                            toastIt("Ship Placed");
                            shipAdd(); //draw the ship if server accepts
                        }
                    } catch (Exception e) {
                        waiting = false;
                        toastIt("Error Placing Ship - try again - illegal ship placement");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {

                }
            });
        } else {
            toastIt("Error - Ship Already There");
            waiting = false;
         //END CAN PLACE - no ships already there //if skiped
        }

        addShips();//call the ship placing control again //draws the ship SHADOW
    }

    //NOTE BUG FOUND REPORT TO TEACHER
    //WHEN PLACING THE DESTROYER IN CELL A-4 FACING WEST SERVER ACCEPTS ex URL IS http://battlegameserver.com/api/v1/2807/gameID/add_ship/destroyer/a/4/6.json;
    //WHEN PLACING THE CARRIER IN CELL A-5 FACING WEST SERVER DENIES ex URL IS http://battlegameserver.com/api/v1/2807/gameID/add_ship/carrier/a/5/6.json;
    //THIS IS A BUG
    //MY SHIPS ARE CREATED FROM THE SERVERS RESPONSE TO GET SHIPS
    public void shipAdd() {
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (gameGrid[x][y].getPlacing_ship() == true) {
                    gameGrid[x][y].setHas_ship(true);  //place ship in cell
                    gameGrid[x][y].setPlacing_ship(false);  //remove shadow ship - gets removed anyways in addShip Method
                }
            }
        } //end the Y loop outside loop

        waiting = false;
    }


    public void onClickRotate(View v) {
        if(waiting){
            toastIt("waiting");
            return;
        }

        rotation += 1;
        if (rotation > 4) {
            rotation = 1;
        }
        if (rotation == 1) {
            north = true;
            south = false;
            east = false;
            west = false;
        }
        if (rotation == 2) {
            north = false;
            south = false;
            east = true;
            west = false;
        }
        if (rotation == 3) {
            north = false;
            south = true;
            east = false;
            west = false;
        }
        if (rotation == 4) {
            north = false;
            south = false;
            east = false;
            west = true;
        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (loaded) {
            soundPool.play(rotationSound, volume, volume, 1, 0, 1f); //this is the rotation sound
            Log.e("Test", "Played sound");
        }
        addShips();
    }

    public void onClickMoveUp(View v) {
        if(waiting){
            toastIt("waiting");
            return;
        }
        //checks to makse sure ship isnt out of bounds
        yCoord--;
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (gameGrid[x][y].getPlacing_ship() == true) {
                    if (y == 1) {
                        yCoord++;
                    }
                }
            }
        }
        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (loaded) {
            soundPool.play(rotationErrorSound, volume, volume, 1, 0, 1f); //this is the rotation sound
            Log.e("Test", "Played sound");
        }
        addShips();
    }

    public void onClickMoveDown(View v) {
        if(waiting){
            toastIt("waiting");
            return;
        }

        yCoord++;
        //NORTH TRUE DEFAULT is bottom
        //check every cell is placing is in it then dont go below 10 for y
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (gameGrid[x][y].getPlacing_ship() == true) {
                    if (y == 10) {
                        yCoord--;
                    }
                }
            }
        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (loaded) {
            soundPool.play(rotationErrorSound, volume, volume, 1, 0, 1f); //this is the rotation sound
            Log.e("Test", "Played sound");
        }
        addShips();
    }

    public void onClickMoveLeft(View v) {
        if(waiting){
            toastIt("waiting");
            return;
        }

        xCoord--;
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (gameGrid[x][y].getPlacing_ship() == true) {
                    if (x == 1) {
                        xCoord++;
                    }
                }
            }
        }
        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (loaded) {
            soundPool.play(rotationErrorSound, volume, volume, 1, 0, 1f); //this is the rotation sound
            Log.e("Test", "Played sound");
        }
        addShips();
    }

    public void onClickMoveRight(View v) {
        if(waiting){
            toastIt("waiting");
            return;
        }

        xCoord++;
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (gameGrid[x][y].getPlacing_ship() == true) {
                    if (x == 10) {
                        xCoord--;
                    }
                }
            }
        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (loaded) {
            soundPool.play(rotationErrorSound, volume, volume, 1, 0, 1f); //this is the rotation sound
            Log.e("Test", "Played sound");
        }
        addShips();
    }


}

/**
 * @param event
 *
 * @return TRUE to consume the event
 */
/*    @Override
    public boolean onTouchEvent( MotionEvent event ) {
        int eventAction = event.getAction();

        switch( eventAction ) {
            case MotionEvent.ACTION_DOWN:
                // finger touches the screen
                break;

            case MotionEvent.ACTION_MOVE:
                // finger moves on the screen
                break;

            case MotionEvent.ACTION_UP:
                // finger leaves the screen
                Log.i( "TOUCH", "x:" + event.getX() + " y:" + event.getY() );
                float[] xy = getRelativeCoords( customGrid, event );
//        Point indicies = findXYIndexes( event.getX(), event.getY() );
                Point indicies = findXYIndexes( xy[0], xy[1] );
                Log.i( "TOUCH", "ix: " + indicies.x + " iy: " + indicies.y );
                gameGrid[indicies.x][indicies.y].setWaiting( true );
                //getWindow().getDecorView().getRootView().invalidate();
                gameBoard.invalidate();
                break;
        }

        // tell the system that we handled the event and no further processing is required
        return true;
    }*/

/*    Point findXYIndexes( float x, float y ) {
        // Given X, Y, find the grid indexes that contain that point
        int height = gameGrid[0][0].getCellHeight();
        int width = gameGrid[0][0].getCellWidth();
        int xo = Game.gameGrid[0][0].getViewOrigin().x;
        int yo = Game.gameGrid[0][0].getViewOrigin().y;

        return new Point( (int)( ( x - xo ) / width ),
            (int)( ( y - gridTop - yo ) / height ) );
    }*/
