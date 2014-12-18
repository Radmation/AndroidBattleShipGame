package com.radmation.RAnayaBattleship;

/**
 * Created by Radmation on 11/12/2014.
 */
public class Ship {

    private String ship_name;
    private int ship_size;
//    private String[] directions = {"North", "East", "South", "West"};

   public Ship(String _ship_name, int _ship_size) {
       ship_name = _ship_name;
       ship_size = _ship_size;

   }

    // first_name Getter
    public String getShipName() {
        return ship_name;
    }

    // first_name Setter
    public void setShipName( String _ship_name ) {
        ship_name = _ship_name;
    }

    public int getShipSize() {
        return ship_size;
    }

    public void setShipSize(int _ship_size) {
        ship_size = _ship_size;
    }

//    public void changeDirection() {
//
//    }

}
