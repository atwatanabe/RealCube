package atwatanabe.realcube;

import android.content.Context;
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
import java.util.EventListener;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GameActivity extends ActionBarActivity implements SensorEventListener
{
    private Sensor sensor;
    private SensorManager sensorManager;
    private CubeRenderer renderer;
    private GLSurfaceView glView;
    private TextView valuesDisplay;

    private CubeManipulator cubeManipulator;
    private Cube3x3 cube;
    private Button[][] buttons;
    private boolean[][] pressed;

    private float[] rotationValues;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);

        valuesDisplay = (TextView)findViewById(R.id.valsDisplay);

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
        buttons[1][1] = (Button)findViewById(R.id.middle);
        buttons[1][2] = (Button)findViewById(R.id.middleRight);
        buttons[2][0] = (Button)findViewById(R.id.downLeft);
        buttons[2][1] = (Button)findViewById(R.id.downCenter);
        buttons[2][2] = (Button)findViewById(R.id.downRight);

        buttons[0][0].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                        pressed[0][0] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[0][0] = false;
                    return true;
                }
            }
        );

        buttons[0][1].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                        pressed[0][1] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[0][1] = false;
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
                        pressed[0][2] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[0][2] = false;
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
                        pressed[1][0] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[1][0] = false;
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
                        pressed[1][1] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[1][1] = false;
                    return true;
                }
            }
        );

        buttons[1][2].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                        pressed[1][2] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[1][2] = false;
                    return true;
                }
            }
        );

        buttons[2][0].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                        pressed[2][0] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[2][0] = false;
                    return true;
                }
            }
        );

        buttons[2][1].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                        pressed[2][1] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[2][1] = false;
                    return true;
                }
            }
        );

        buttons[2][2].setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_DOWN)
                        pressed[2][2] = true;
                    else if (m.getAction() == MotionEvent.ACTION_UP)
                        pressed[2][2] = false;
                    return true;
                }
            }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_FASTEST);
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
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
        {
            //SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            //Log.i("rotationSensor", new Float(mRotationMatrix[0]).toString());
            //Log.i("rotation", new Float(event.values[0]).toString());

            //if (pressed[0][0])
                //Log.i("test", "It worked!");


            rotationValues = new float[] {event.values[0], event.values[1], event.values[2]};

            valuesDisplay = (TextView)findViewById(R.id.valsDisplay);
            if (event != null)
                valuesDisplay.setText(event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
        }
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
            // find the rotation-vector sensor
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
            //Log.i("onSensorChanged", event.toString());
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
            {
                /*
                float[] temp = new float[event.values.length];
                for (int i = 0; i < temp.length; ++i)
                {
                    temp[i] = 2 * event.values[i];
                }
                */

                SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
                //Log.i("rotationSensor", new Float(mRotationMatrix[0]).toString());
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
