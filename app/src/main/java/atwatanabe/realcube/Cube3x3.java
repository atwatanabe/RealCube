package atwatanabe.realcube;

import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

public class Cube3x3
{
    private Map<Side, Color[][]> sides;

    public Cube3x3()
    {
        sides = new TreeMap<Side, Color[][]>();
        initializeCube();
    }

    public enum Color
    {
        White, Blue, Red, Yellow, Green, Orange
    }

    public enum Side
    {
        Up, Down, Left, Right, Front, Back
    }

    public void initializeCube()
    {
        Color[][] up = new Color[][]
        {
            {Color.White, Color.White, Color.White},
            {Color.White, Color.White, Color.White},
            {Color.White, Color.White, Color.White}
        };
        Color[][] down = new Color[][]
        {
            {Color.Yellow, Color.Yellow, Color.Yellow},
            {Color.Yellow, Color.Yellow, Color.Yellow},
            {Color.Yellow, Color.Yellow, Color.Yellow}
        };
        Color[][] left = new Color[][]
        {
            {Color.Red, Color.Red, Color.Red},
            {Color.Red, Color.Red, Color.Red},
            {Color.Red, Color.Red, Color.Red}
        };
        Color[][] right = new Color[][]
        {
            {Color.Orange, Color.Orange, Color.Orange},
            {Color.Orange, Color.Orange, Color.Orange},
            {Color.Orange, Color.Orange, Color.Orange}
        };
        Color[][] front = new Color[][]
        {
            {Color.Blue, Color.Blue, Color.Blue},
            {Color.Blue, Color.Blue, Color.Blue},
            {Color.Blue, Color.Blue, Color.Blue}
        };
        Color[][] back = new Color[][]
        {
            {Color.Green, Color.Green, Color.Green},
            {Color.Green, Color.Green, Color.Green},
            {Color.Green, Color.Green, Color.Green}
        };

        sides.put(Side.Up, up);
        sides.put(Side.Down, down);
        sides.put(Side.Left, left);
        sides.put(Side.Right, right);
        sides.put(Side.Front, front);
        sides.put(Side.Back, back);
    }

    public void up(boolean invert)
    {
        Log.i("Cube move", "up " + invert);
    }

    public void down(boolean invert)
    {
        Log.i("Cube move", "down " + invert);
    }

    public void left(boolean invert)
    {
        Log.i("Cube move", "left " + invert);
    }

    public void right(boolean invert)
    {
        Log.i("Cube move", "right " + invert);
    }

    public void front(boolean invert)
    {
        Log.i("Cube move", "front " + invert);
    }

    public void back(boolean invert)
    {
        Log.i("Cube move", "back " + invert);
    }

    public void middle(boolean invert)
    {
        Log.i("Cube move", "middle " + invert);
    }

    public void equator(boolean invert)
    {
        Log.i("Cube move", "equator " + invert);
    }

    public void standing(boolean invert)
    {
        Log.i("Cube move", "standing " + invert);
    }
}
