package com.example.memor;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

class Card {
    Paint p = new Paint();

    public Card(float x, float y, float width, float height, int color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    int color, backColor = Color.DKGRAY;
    boolean isOpen = false; // цвет карты
    float x, y, width, height;
    public void draw(Canvas c) {
        // нарисовать карту в виде цветного прямоугольника
        if (isOpen) {
            p.setColor(color);
        } else p.setColor(backColor);
        c.drawRect(x,y, x+width, y+height, p);
    }
    public boolean flip (float touch_x, float touch_y) {
        if (touch_x >= x && touch_x <= x + width && touch_y >= y && touch_y <= y + height) {
            isOpen = ! isOpen;
            return true;
        } else return false;
    }

}

public class TilesView extends View {
    // пауза для запоминания карт
    final int PAUSE_LENGTH = 1; // в секундах
    boolean isOnPauseNow = false;
    // число открытых карт
    int openedCard = 0;
    ArrayList<Card> cards = new ArrayList<>();

    int width, height; // ширина и высота канвы

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int n = 4;
        int x1 = 70;
        int y1 = 200;
        int clrYellow = 0;
        int clrRed = 0;
        int clrGreen = 0;
        int clrBlue = 0;
        int clrCyan = 0;
        int clrMagenta = 0;
        int clrBlack = 0;
        int clrLTgray = 0;
        Random rnd = new Random();
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                switch (rnd.nextInt(7))
                {
                    case 0:
                        if (clrYellow < 2)
                        {
                            cards.add(new Card(x1,y1, 200, 150, Color.YELLOW));
                            clrYellow++;
                            break;
                        }
                    case 1:
                        if (clrRed < 2)
                        {
                            cards.add(new Card(x1, y1, 200, 150, Color.RED));
                            clrRed++;
                            break;
                        }
                    case 2:
                        if (clrGreen < 2)
                        {
                            cards.add(new Card(x1, y1, 200, 150, Color.GREEN));
                            clrGreen++;
                            break;
                        }
                    case 3:
                        if (clrBlue < 2)
                        {
                            cards.add(new Card(x1,y1, 200, 150, Color.BLUE));
                            clrBlue++;
                            break;
                        }
                    case 4:
                        if (clrCyan < 2)
                        {
                            cards.add(new Card(x1,y1, 200, 150, Color.CYAN));
                            clrCyan++;
                            break;
                        }
                    case 5:
                        if (clrMagenta < 2)
                        {
                            cards.add(new Card(x1,y1, 200, 150, Color.MAGENTA));
                            clrMagenta++;
                            break;
                        }
                    case 6:
                        if (clrBlack < 2)
                        {
                            cards.add(new Card(x1,y1, 200, 150, Color.BLACK));
                            clrBlack++;
                            break;
                        }
                    case 7:
                        if (clrLTgray < 2)
                        {
                            cards.add(new Card(x1,y1, 200, 150, Color.LTGRAY));
                            clrLTgray++;
                            break;
                        }
                }
                y1 += 200;
            }
            x1 += 250;
            y1 = 200;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        if (!cards.isEmpty())
        {
            for (Card c: cards) {
                c.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 3) получить координаты касания
        int x = (int) event.getX();
        int y = (int) event.getY();
        // 4) определить тип события
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {
            for (Card c: cards) {

                if (openedCard == 0) {
                    if (c.flip(x, y)) {
                        Log.d("mytag", "card flipped: " + openedCard);
                        openedCard ++;
                        invalidate();
                        return true;
                    }
                }

                if (openedCard == 1) {
                    if (c.flip(x, y)) {
                        openedCard ++;
                        checkOpenCardsEqual();
                        invalidate();
                        PauseTask task = new PauseTask();
                        task.execute(PAUSE_LENGTH);
                        isOnPauseNow = true;
                        return true;
                        }
                    }
                }
            }
        invalidate();
        return true;
    }

    public void checkOpenCardsEqual()
    {
        int clrBuf = 0;
        Card buf = null;
        for (Card c: cards)
        {
            if (c.isOpen)
            {
                if (c.color == clrBuf)
                {
                    cards.remove(c);
                    cards.remove(buf);
                    break;
                }
                else
                {
                    clrBuf = c.color;
                    buf = c;
                }
            }
        }
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 1000); // передаём число секунд ожидания
            } catch (InterruptedException e) {}
            Log.d("mytag", "Pause finished");
            return null;
        }

        // после паузы, перевернуть все карты обратно


        @Override
        protected void onPostExecute(Void aVoid) {
                for (Card c: cards) {
                    if (c.isOpen) {
                        c.isOpen = false;
                    }
                }
                openedCard = 0;
                isOnPauseNow = false;
                invalidate();
        }
    }
}
