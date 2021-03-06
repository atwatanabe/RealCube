package atwatanabe.realcube;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GameActivity extends ActionBarActivity implements SensorEventListener
{
    private Sensor accSensor;
    //private Sensor rotSensor;
    private SensorManager sensorManager;
    private CubeRenderer renderer;
    private GLSurfaceView glView;
    private TextView valXDisplay;
    private TextView valYDisplay;
    private TextView valZDisplay;
    private TextView moveText;
    private Command mostRecentMove;

    private CubeManipulator cubeManipulator;
    private Cube3x3 cube;
    private Button[][] buttons;
    private boolean[][] pressed;

    private float[] rotationValues;
    private float[] startValues;
    private float[] stopValues;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //rotSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, accSensor, sensorManager.SENSOR_DELAY_GAME);
        //sensorManager.registerListener(this, rotSensor, sensorManager.SENSOR_DELAY_GAME);

        //valXDisplay = (TextView)findViewById(R.id.valXDisplay);
        //valYDisplay = (TextView)findViewById(R.id.valYDisplay);
        //valYDisplay = (TextView)findViewById(R.id.valYDisplay);

        /*
        Sensor gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyro != null)
            Log.i("gyroscope: ", "gyroscope exists!");
        else
            Log.i("gyroscope: ", "gyroscope does not exist!");
        */

        //renderer = new CubeRenderer();
        //glView = new GLSurfaceView(this);
        //glView.setRenderer(renderer);

        setContentView(R.layout.activity_game);

        buttons = new Button[3][3];
        pressed = new boolean[3][3];

        buttons[0][0] = (Button)findViewById(R.id.upLeft);
        buttons[0][1] = (Button)findViewById(R.id.upCenter);
        buttons[0][2] = (Button)findViewById(R.id.upRight);
        buttons[1][0] = (Button)findViewById(R.id.middleLeft);
        buttons[1][1] = (Button)findViewById(R.id.middleCenter);
        buttons[1][2] = (Button)findViewById(R.id.middleRight);
        buttons[2][0] = (Button)findViewById(R.id.downLeft);
        buttons[2][1] = (Button)findViewById(R.id.downCenter);
        buttons[2][2] = (Button)findViewById(R.id.downRight);

        cube = new Cube3x3();
        cubeManipulator = new CubeManipulator();

        updateDisplay();

        //rotationValues = new float[3];
        startValues = new float[3];
        stopValues = new float[3];

        moveText = (TextView) findViewById(R.id.moveText);

        buttons[0][0].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        pressed[0][0] = true;
                        System.arraycopy(rotationValues, 0, startValues, 0, 3);
                    }
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                    {
                        pressed[0][0] = false;
                        System.arraycopy(rotationValues, 0, stopValues, 0, 3);
                        rotation largest = getLargest();
                        Command cmd = new LeftCommand(cube, false);
                        switch (largest)
                        {
                            case X:
                            {
                                cmd = new UpCommand(cube, false);
                                break;
                            }
                            case X_:
                            {
                                cmd = new UpCommand(cube, true);
                                break;
                            }
                            case Y_:
                            {
                                cmd = new LeftCommand(cube, true);
                                break;
                            }
                            case Y:
                            {
                                cmd = new LeftCommand(cube, false);
                                break;
                            }
                            case Z:
                            {
                                cmd = new FrontCommand(cube, true);
                                break;
                            }
                            case Z_:
                            {
                                cmd = new FrontCommand(cube, false);
                                break;
                            }
                        }
                        cubeManipulator.manipulateCube(cmd);
                        mostRecentMove = cmd;
                        moveText.setText(cmd.toString());
                    }
                    updateDisplay();
                    return true;
                }
            }
        );

        buttons[0][1].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN) {
                        pressed[0][1] = true;
                        System.arraycopy(rotationValues, 0, startValues, 0, 3);
                    }
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                    {
                        pressed[0][1] = false;
                        System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                        Command cmd = new LeftCommand(cube, false);
                        switch (largest)
                        {
                            case X:
                            {
                                cmd = new UpCommand(cube, false);
                                break;
                            }
                            case X_:
                            {
                                cmd = new UpCommand(cube, true);
                                break;
                            }
                            case Y_:
                            {
                                cmd = new MiddleCommand(cube, true);
                                break;
                            }
                            case Y:
                            {
                                cmd = new MiddleCommand(cube, false);
                                break;
                            }
                            case Z:
                            {
                                cmd = new FrontCommand(cube, true);
                                break;
                            }
                            case Z_:
                            {
                                cmd = new FrontCommand(cube, false);
                                break;
                            }
                        }
                        cubeManipulator.manipulateCube(cmd);
                        mostRecentMove = cmd;
                        moveText.setText(cmd.toString());
                    }
                    updateDisplay();
                    return true;
                }
            }
        );

        buttons[0][2].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        pressed[0][2] = true;
                        System.arraycopy(rotationValues, 0, startValues, 0, 3);
                    }
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                    {
                        pressed[0][2] = false;
                        System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                        Command cmd = new LeftCommand(cube, false);
                        switch (largest)
                        {
                            case X:
                            {
                                cmd = new UpCommand(cube, false);
                                break;
                            }
                            case X_:
                            {
                                cmd = new UpCommand(cube, true);
                                break;
                            }
                            case Y_:
                            {
                                cmd = new RightCommand(cube, false);
                                break;
                            }
                            case Y:
                            {
                                cmd = new RightCommand(cube, true);
                                break;
                            }
                            case Z:
                            {
                                cmd = new FrontCommand(cube, true);
                                break;
                            }
                            case Z_:
                            {
                                cmd = new FrontCommand(cube, false);
                                break;
                            }
                        }
                        cubeManipulator.manipulateCube(cmd);
                        mostRecentMove = cmd;
                        moveText.setText(cmd.toString());
                    }
                    updateDisplay();
                    return true;
                }
            }
        );

        buttons[1][0].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        pressed[1][0] = true;
                        System.arraycopy(rotationValues, 0, startValues, 0, 3);
                    }
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                    {
                        pressed[1][0] = false;
                        System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                        Command cmd = new LeftCommand(cube, false);
                        switch (largest)
                        {
                            case X:
                            {
                                cmd = new EquatorCommand(cube, true);
                                break;
                            }
                            case X_:
                            {
                                cmd = new EquatorCommand(cube, false);
                                break;
                            }
                            case Y_:
                            {
                                cmd = new LeftCommand(cube, true);
                                break;
                            }
                            case Y:
                            {
                                cmd = new LeftCommand(cube, false);
                                break;
                            }
                            case Z:
                            {
                                cmd = new FrontCommand(cube, true);
                                break;
                            }
                            case Z_:
                            {
                                cmd = new FrontCommand(cube, false);
                                break;
                            }
                        }
                        cubeManipulator.manipulateCube(cmd);
                        mostRecentMove = cmd;
                        moveText.setText(cmd.toString());
                    }
                    updateDisplay();
                    return true;
                }
            }
        );

        buttons[1][1].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        pressed[1][1] = true;
                        System.arraycopy(rotationValues, 0, startValues, 0, 3);
                    }
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                    {
                        pressed[1][1] = false;
                        System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                        Command cmd = new LeftCommand(cube, false);
                        switch (largest)
                        {
                            case X:
                            {
                                cmd = new EquatorCommand(cube, true);
                                break;
                            }
                            case X_:
                            {
                                cmd = new EquatorCommand(cube, false);
                                break;
                            }
                            case Y_:
                            {
                                cmd = new MiddleCommand(cube, true);
                                break;
                            }
                            case Y:
                            {
                                cmd = new MiddleCommand(cube, false);
                                break;
                            }
                            case Z:
                            {
                                cmd = new FrontCommand(cube, true);
                                break;
                            }
                            case Z_:
                            {
                                cmd = new FrontCommand(cube, false);
                                break;
                            }
                        }
                        cubeManipulator.manipulateCube(cmd);
                        mostRecentMove = cmd;
                        moveText.setText(cmd.toString());
                    }
                    updateDisplay();
                    return true;
                }
            }
        );

        buttons[1][2].setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent m) {
                        if (m.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            pressed[1][2] = true;
                            System.arraycopy(rotationValues, 0, startValues, 0, 3);
                        } else if (m.getAction() == MotionEvent.ACTION_UP)
                        {
                            pressed[1][2] = false;
                            System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                            Command cmd = new LeftCommand(cube, false);
                            switch (largest)
                            {
                                case X:
                                {
                                    cmd = new EquatorCommand(cube, true);
                                    break;
                                }
                                case X_:
                                {
                                    cmd = new EquatorCommand(cube, false);
                                    break;
                                }
                                case Y_:
                                {
                                    cmd = new RightCommand(cube, false);
                                    break;
                                }
                                case Y:
                                {
                                    cmd = new RightCommand(cube, true);
                                    break;
                                }
                                case Z:
                                {
                                    cmd = new FrontCommand(cube, true);
                                    break;
                                }
                                case Z_:
                                {
                                    cmd = new FrontCommand(cube, false);
                                    break;
                                }
                            }
                            cubeManipulator.manipulateCube(cmd);
                            mostRecentMove = cmd;
                            moveText.setText(cmd.toString());
                        }
                        updateDisplay();
                        return true;
                    }
                }
        );

        buttons[2][0].setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent m) {
                        if (m.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            pressed[2][0] = true;
                            System.arraycopy(rotationValues, 0, startValues, 0, 3);
                        } else if (m.getAction() == MotionEvent.ACTION_UP)
                        {
                            pressed[2][0] = false;
                            System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                            Command cmd = new LeftCommand(cube, false);
                            switch (largest)
                            {
                                case X:
                                {
                                    cmd = new DownCommand(cube, true);
                                    break;
                                }
                                case X_:
                                {
                                    cmd = new DownCommand(cube, false);
                                    break;
                                }
                                case Y_:
                                {
                                    cmd = new LeftCommand(cube, true);
                                    break;
                                }
                                case Y:
                                {
                                    cmd = new LeftCommand(cube, false);
                                    break;
                                }
                                case Z:
                                {
                                    cmd = new FrontCommand(cube, true);
                                    break;
                                }
                                case Z_:
                                {
                                    cmd = new FrontCommand(cube, false);
                                    break;
                                }
                            }
                            cubeManipulator.manipulateCube(cmd);
                            mostRecentMove = cmd;
                            moveText.setText(cmd.toString());
                        }
                        updateDisplay();
                        return true;
                    }
                }
        );

        buttons[2][1].setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent m) {
                        if (m.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            pressed[2][1] = true;
                            System.arraycopy(rotationValues, 0, startValues, 0, 3);
                        } else if (m.getAction() == MotionEvent.ACTION_UP)
                        {
                            pressed[2][1] = false;
                            System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                            Command cmd = new LeftCommand(cube, false);
                            switch (largest)
                            {
                                case X:
                                {
                                    cmd = new DownCommand(cube, true);
                                    break;
                                }
                                case X_:
                                {
                                    cmd = new DownCommand(cube, false);
                                    break;
                                }
                                case Y_:
                                {
                                    cmd = new MiddleCommand(cube, true);
                                    break;
                                }
                                case Y:
                                {
                                    cmd = new MiddleCommand(cube, false);
                                    break;
                                }
                                case Z:
                                {
                                    cmd = new FrontCommand(cube, true);
                                    break;
                                }
                                case Z_:
                                {
                                    cmd = new FrontCommand(cube, false);
                                    break;
                                }
                            }
                            cubeManipulator.manipulateCube(cmd);
                            mostRecentMove = cmd;
                            moveText.setText(cmd.toString());
                        }
                        updateDisplay();
                        return true;
                    }
                }
        );

        buttons[2][2].setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent m) {
                        if (m.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            pressed[2][2] = true;
                            System.arraycopy(rotationValues, 0, startValues, 0, 3);
                        } else if (m.getAction() == MotionEvent.ACTION_UP)
                        {
                            pressed[2][2] = false;
                            System.arraycopy(rotationValues, 0, stopValues, 0, 3);rotation largest = getLargest();
                            Command cmd = new LeftCommand(cube, false);
                            switch (largest)
                            {
                                case X:
                                {
                                    cmd = new DownCommand(cube, true);
                                    break;
                                }
                                case X_:
                                {
                                    cmd = new DownCommand(cube, false);
                                    break;
                                }
                                case Y_:
                                {
                                    cmd = new RightCommand(cube, false);
                                    break;
                                }
                                case Y:
                                {
                                    cmd = new RightCommand(cube, true);
                                    break;
                                }
                                case Z:
                                {
                                    cmd = new FrontCommand(cube, true);
                                    break;
                                }
                                case Z_:
                                {
                                    cmd = new FrontCommand(cube, false);
                                    break;
                                }
                            }
                            cubeManipulator.manipulateCube(cmd);
                            mostRecentMove = cmd;
                            moveText.setText(cmd.toString());
                        }
                        updateDisplay();
                        return true;
                    }
                }
        );

        Button undo = (Button)findViewById(R.id.undoButton);
        Button redo = (Button)findViewById(R.id.redoButton);

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mostRecentMove = cubeManipulator.undo();
                if (mostRecentMove != null)
                    moveText.setText(mostRecentMove.toString());
                updateDisplay();
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mostRecentMove = cubeManipulator.redo();
                if (mostRecentMove != null)
                    moveText.setText(mostRecentMove.toString());
                updateDisplay();
            }
        });

        Button solve = (Button)findViewById(R.id.solveButton);

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cube.initializeCube();
                cubeManipulator.clearHistory();
                updateDisplay();
            }
        });

    }

    public void updateDisplay()
    {
        Cube3x3.Color[][] front = cube.getFront();
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                Cube3x3.Color temp = front[i][j];
                Button b = buttons[i][j];
                switch (temp)
                {
                    case White:
                    {
                        b.setBackgroundColor(android.graphics.Color.WHITE);
                        break;
                    }
                    case Blue:
                    {
                        b.setBackgroundColor(android.graphics.Color.BLUE);
                        break;
                    }
                    case Red:
                    {
                        b.setBackgroundColor(android.graphics.Color.RED);
                        break;
                    }
                    case Green:
                    {
                        b.setBackgroundColor(android.graphics.Color.GREEN);
                        break;
                    }
                    case Orange:
                    {
                        b.setBackgroundColor(android.graphics.Color.BLACK);
                        break;
                    }
                    case Yellow:
                    {
                        b.setBackgroundColor(android.graphics.Color.YELLOW);
                        break;
                    }
                }

            }
        }
    }

    public enum rotation
    {
        X, X_, Y, Y_, Z, Z_
    }

    public rotation getLargest()
    {
        float dx = stopValues[0] - startValues[0];
        float dy = stopValues[1] - startValues[1];
        float dz = stopValues[2] - startValues[2];

        float adx = Math.abs(dx);
        float ady = Math.abs(dy);
        float adz = Math.abs(dz);

        if (adx > ady && adx > adz)
            return (dx >= 0) ? rotation.X : rotation.X_;
        if (ady > adx && ady > adz)
            return (dy >= 0) ? rotation.Y : rotation.Y_;
        return (dz >= 0) ? rotation.Z : rotation.Z_;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accSensor, sensorManager.SENSOR_DELAY_GAME);
        //sensorManager.registerListener(this, rotSensor, sensorManager.SENSOR_DELAY_GAME);
        //renderer.start();
        //glView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        //renderer.stop();
        //glView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            rotationValues = new float[3];
            System.arraycopy(event.values, 0, rotationValues, 0, 3);

            /*
            valXDisplay = (TextView)findViewById(R.id.valXDisplay);
            valYDisplay = (TextView)findViewById(R.id.valYDisplay);
            valZDisplay = (TextView)findViewById(R.id.valZDisplay);
            valXDisplay.setText(Float.toString(event.values[0]));
            valYDisplay.setText(Float.toString(event.values[1]));
            valZDisplay.setText(Float.toString(event.values[2]));
            */
        }
        /*
        else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
        {
            rotationValues[2] = event.values[2];
            if (event != null)
                valZDisplay.setText(new Float(event.values[2]).toString());
        }
        */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    class CubeRenderer implements GLSurfaceView.Renderer, SensorEventListener
    {

        private float[] mRotationMatrix = new float[16];
        private Sensor mSensor;
        private Cube cube;

        public CubeRenderer()
        {
            // find the rotation-vector accSensor
            mSensor = sensorManager.getDefaultSensor(
                    Sensor.TYPE_ROTATION_VECTOR);
            cube = new Cube();
            // initialize the rotation matrix to identity
            mRotationMatrix[ 0] = 1;
            mRotationMatrix[ 4] = 1;
            mRotationMatrix[ 8] = 1;
            mRotationMatrix[12] = 1;
        }

        public void start()
        {
            sensorManager.registerListener(this, mSensor, 10000);
        }

        public void stop()
        {
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            gl.glDisable(GL10.GL_DITHER);
            gl.glClearColor(1, 1, 1, 1);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            gl.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        }

        @Override
        public void onDrawFrame(GL10 gl)
        {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -3.0f);
            gl.glMultMatrixf(mRotationMatrix, 0);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            cube.draw(gl);
        }

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }
    }

    class Cube
    {
        // initialize our cube
        private FloatBuffer mVertexBuffer;
        private FloatBuffer mColorBuffer;
        private ByteBuffer mIndexBuffer;
        public Cube() {
            final float vertices[] = {
                    -1, -1, -1, 1, -1, -1,
                    1, 1, -1, -1, 1, -1,
                    -1, -1, 1, 1, -1, 1,
                    1, 1, 1, -1, 1, 1,
            };
            final float colors[] = {
                    0, 0, 0, 1, 1, 0, 0, 1,
                    1, 1, 0, 1, 0, 1, 0, 1,
                    0, 0, 1, 1, 1, 0, 1, 1,
                    1, 1, 1, 1, 0, 1, 1, 1,
            };
            final byte indices[] = {
                    0, 4, 5, 0, 5, 1,
                    1, 5, 6, 1, 6, 2,
                    2, 6, 7, 2, 7, 3,
                    3, 7, 4, 3, 4, 0,
                    4, 7, 6, 4, 6, 5,
                    3, 0, 1, 3, 1, 2
            };
            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            mVertexBuffer = vbb.asFloatBuffer();
            mVertexBuffer.put(vertices);
            mVertexBuffer.position(0);
            ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
            cbb.order(ByteOrder.nativeOrder());
            mColorBuffer = cbb.asFloatBuffer();
            mColorBuffer.put(colors);
            mColorBuffer.position(0);
            mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
            mIndexBuffer.put(indices);
            mIndexBuffer.position(0);
        }

        public void draw(GL10 gl) {
            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glFrontFace(GL10.GL_CW);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
            gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
