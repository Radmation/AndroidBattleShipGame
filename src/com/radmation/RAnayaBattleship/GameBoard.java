package com.radmation.RAnayaBattleship;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;



public class GameBoard extends ImageView {
    //MY VARS
    private static Paint paintLine, paintText, paint;
    //MY VARS


    int border = 10;    // Width of the outer border
    int topBoardX = border;
    int topBoardY = 50;
    int fontSize = 30;
    float cow;
    Point[][] grid = new Point[11][11];
    String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    String[] numbers = { " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", "10" };
    Point origin = new Point( this.getLeft(), this.getTop() );

    public GameBoard( Context context, AttributeSet attrs ) {
        super( context, attrs );


        //MY VARS
        paintLine = new Paint();
        paintLine.setStrokeWidth(1);
        paintLine.setColor(Color.BLUE);
        paintLine.setStyle(Paint.Style.STROKE);

        paintText = new Paint();
        paintText.setTextSize(30);
        paintText.setColor(Color.WHITE);
        //MY VARS


        paint = new Paint();
        paint.setStrokeWidth( 10 );
        paint.setColor( Color.BLUE );
        paint.setStyle( Paint.Style.FILL_AND_STROKE );
        paint.setTextSize( fontSize );
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw( canvas );

        //MY VARS
        //drawing in here
        //int screenWidth = this.getMeasuredWidth();
        //int screenHeight = this.getMeasuredHeight();
        int borderOffSet = 10;
        int boardSpacing = 50;
        int boardWidth = this.getMeasuredWidth() - (borderOffSet);
        int boardHeight = this.getMeasuredHeight() - (borderOffSet) - boardSpacing;
        int cellWidthM = this.getMeasuredWidth()  / 11;
        int cellHeightM = (this.getMeasuredHeight() - boardSpacing - borderOffSet - 50) / 11;
        //MY VARS


        this.getLeft();
        int screenHeight = this.getMeasuredHeight();
        int screenWidth = this.getMeasuredWidth();
        int middle = screenHeight / 2;
        // Erase the view first
        paint.setColor( Color.BLACK );
        //canvas.drawRect( topBoardX, topBoardY, screenWidth - border, middle - border, paint );

        paint.setColor( Color.BLUE );

        // Calculate cell width based on width
        int cellWidth = ( screenWidth / 11 ) - 1;
        int cellHeight = ( middle - border ) / 12;   // 10 cells + 1 for letters and 1 for Attack/Defend Boards

        // Draw a border
        //canvas.drawRect( topBoardX, topBoardY, screenWidth - border, middle - border, paint );
        paint.setColor( Color.GREEN );
        paint.setStrokeWidth( 2 );

/*        if( Game.gameStarted ) {

            Game.gameGrid[0][0].setCellHeight( cellHeight );
            Game.gameGrid[0][0].setCellWidth( cellWidth );
            Game.gameGrid[0][0].setViewOrigin( origin );

            for( int y = 0; y < 11; y++ ) {
                for( int x = 0; x < 11; x++ ) { // TODO:  add the points to the gameGrid
                    Game.gameGrid[x][y].setTopleft( new Point( x * cellWidth + border, y * cellHeight + topBoardY ) );
                    Game.gameGrid[x][y].setBottomright( new Point( ( x + 1 ) * cellWidth + border, ( y + 1 ) * cellHeight + topBoardX ) );
                }
            }


        } //game started*/

        //MY STUFF BELOW -----------------------------------------------
        // beginning x,y ending x,y paint
        for(int i = 0;  i < 11; i++ ) {
            canvas.drawLine(i * cellWidthM + (borderOffSet), 50, i * cellWidthM + (borderOffSet), screenHeight - borderOffSet - boardSpacing, paintText ); // vertical lines
            //                  X                         ,Y ,   cellWidth,    height of verticle lines
            canvas.drawLine( borderOffSet, i * cellHeightM + 50, screenWidth - borderOffSet, i * cellHeightM + 50, paintText );
        }
        //attacking board border
        canvas.drawRect(borderOffSet, 50, boardWidth, boardHeight, paintLine); // border
        canvas.drawText("Defending Board", 50, 50, paintText); // text, X, Y, paintLine
        //MY STUFF ABOVE -----------------------------------------------


        // Draw the horizontal lines
        //for( int i = 0; i < 11; i++ ) {
        //canvas.drawLine( i * cellWidth + border, topBoardY, i * cellWidth + border, middle - border, paint );    // Vertical Lines
        //canvas.drawLine( border, i * cellHeight + topBoardY, screenWidth - border, i * cellHeight + topBoardY, paint );  // Horizontal Lines
        //}
        paint.setStrokeWidth( 2 );
        paint.setColor( Color.WHITE );
        paint.setStyle( Paint.Style.FILL_AND_STROKE );

        float w = paint.measureText( numbers[0], 0, numbers[0].length() );
        float center = ( cellWidthM / 2 ) - ( w / 2 );

       Game.gameGrid[0][0].setCellHeight( cellHeightM );
        Game.gameGrid[0][0].setCellWidth( cellWidthM );
        Game.gameGrid[0][0].setViewOrigin( origin );

        for( int y = 0; y < 11; y++ ) {
            for( int x = 0; x < 11; x++ ) { // TODO:  add the points to the gameGrid
                Game.gameGrid[x][y].setTopleft( new Point( x * cellWidthM + borderOffSet, y * cellHeightM + 50 ) );
                Game.gameGrid[x][y].setBottomright( new Point( ( x + 1 ) * cellWidthM + borderOffSet, ( y + 1 ) * cellHeightM + 50 ) );
            }
        }


        for( int x = 0; x < 10; x++ ) {
            canvas.drawText( letters[x], Game.gameGrid[x][0].getTopleft().x + center + cellWidthM, Game.gameGrid[x][0].getTopleft().y + fontSize + border, paint );
        }
        for( int y = 0; y < 10; y++ ) {
            canvas.drawText( numbers[y], Game.gameGrid[0][y].getTopleft().x + center, Game.gameGrid[0][y + 1].getTopleft().y + cellHeightM - border, paint );
        }

        paint.setColor( Color.WHITE );
        // Draw the contents of the grid
        //gets drawn all the time
        for( int y = 0; y < 11; y++ ) {
            for( int x = 0; x < 11; x++ ) {
                //has ship
                if( Game.gameGrid[x][y].getHas_ship() == true )
                    fillCell( x, y, center, Color.GREEN, canvas, cellWidthM, cellHeightM);
                //placing ship
                if( Game.gameGrid[x][y].getPlacing_ship() == true )
                    fillCell( x, y, center, Color.BLUE, canvas, cellWidthM, cellHeightM);
                 //waiting                       // canvas.drawRect(left top right bottom paint)          left top right bottom paint)
                if( Game.gameGrid[x][y].getHas_ship() == true && Game.gameGrid[x][y].getPlacing_ship() == true) //if trying to place a ship in cell with ship
                    fillCell( x, y, center, Color.YELLOW, canvas, cellWidthM, cellHeightM);
                if( Game.gameGrid[x][y].getWaiting() == true )
                    //drawCell( "W", x, y, center, canvas );
                    fillCell( x, y, center, Color.YELLOW, canvas, cellWidthM, cellHeightM);
                //miss
                if( Game.gameGrid[x][y].getMiss() == true )
                    //drawCell( "M", x, y, center, canvas );
                    fillCell( x, y, center, Color.LTGRAY, canvas, cellWidthM, cellHeightM);
                //hit
                if( Game.gameGrid[x][y].getHit() == true )
                    //drawCell( "H", x, y, center, canvas );
                    fillCell( x, y, center, Color.RED, canvas, cellWidthM, cellHeightM);
            }

        }
    }

    void drawCell( String contents, int x, int y, float center, Canvas canvas ) {
        canvas.drawText( contents, Game.gameGrid[x][y].getTopleft().x + center, Game.gameGrid[x][y].getTopleft().y + center + 20, paint );


    }
    void drawCellRect(int left,int top,int bottom,int right, Canvas canvas){
        canvas.drawRect( left, top, bottom, right, paint);
    }

    void fillCell(int x, int y, float center, int color, Canvas canvas, int width, int height) {
        Paint myPaint = new Paint();
        myPaint = new Paint();
        myPaint.setStrokeWidth( 1 );
        myPaint.setColor(color);
        myPaint.setStyle( Paint.Style.FILL_AND_STROKE );
        canvas.drawRect( Game.gameGrid[x][y].getTopleft().x, Game.gameGrid[x][y].getTopleft().y + height - 1, Game.gameGrid[x][y].getTopleft().x + width - 1, Game.gameGrid[x][y].getTopleft().y, myPaint);
                                                            //----------------height above-----------------//
    }

}

/*
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.*;

*/
/**
 * Created by Radmation on 11/25/2014.
 *//*



public class GameBoard extends ImageView{

    private static Paint paintLine, paintText;

    public GameBoard (Context context, AttributeSet attrs) {
        super(context, attrs);
        paintLine = new Paint();
        paintLine.setStrokeWidth(1);
        paintLine.setColor(Color.BLUE);
        paintLine.setStyle(Paint.Style.STROKE);

        paintText = new Paint();
        paintText.setTextSize(30);
        paintText.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawing in here
        int screenWidth = this.getMeasuredWidth();
        int screenHeight = this.getMeasuredHeight();
        int borderOffSet = 10;

        int boardSpacing = 50;

        int boardWidth = this.getMeasuredWidth() - (borderOffSet);
        int boardHeight = this.getMeasuredHeight() - (borderOffSet) - boardSpacing;

        int center = this.getMeasuredHeight() / 2;



        int cellWidth = this.getMeasuredWidth()  / 11;
        int cellHeight = (this.getMeasuredHeight() - boardSpacing - borderOffSet - 50) / 11;




        //defending board border
       // canvas.drawRect(borderOffSet + borderOffSet, borderOffSet + borderOffSet + boardHeight, boardWidth, boardHeight + borderOffSet + boardHeight, paintLine); // border

        //rect alt
        canvas.drawRect(borderOffSet + borderOffSet, borderOffSet + borderOffSet + boardHeight + boardSpacing, 100, 100, paintLine);

        // beginning x,y ending x,y paint
        for(int i = 0;  i < 11; i++ ) {
            canvas.drawLine(i * cellWidth + (borderOffSet), 50, i * cellWidth + (borderOffSet), screenHeight - borderOffSet - boardSpacing, paintText ); // vertical lines
            //                  X                         ,Y ,   cellWidth,    height of verticle lines
            canvas.drawLine( borderOffSet, i * cellHeight + 50, screenWidth - borderOffSet, i * cellHeight + 50, paintText );
        }
        //attacking board border
        canvas.drawRect(borderOffSet, 50, boardWidth, boardHeight, paintLine); // border





            //left, top, right, bottom, paintLine
        //     X  Y   WIDTH  HEIGHT
        //canvas.drawRect(0,0, cellWidth, cellWidth, paintLine);

        canvas.drawText("Defending Board", 50, 50, paintText); // text, X, Y, paintLine

    }

}
*/
