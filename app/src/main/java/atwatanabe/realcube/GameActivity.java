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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GameActivity extends ActionBarActivity
{

    private SensorManager mSensorManager;
    private CubeRenderer renderer;
    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        //mSensorManager.registerListener(this, mSensor, 10000);
        renderer = new CubeRenderer();
        glView = new GLSurfaceView(this);
        glView.setRenderer(renderer);

        //setContentView(R.layout.activity_game);
        setContentView(glView);

        /*
        Button upLeft = (Button)findViewById(R.id.upLeft);
        upLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m) {

                Log.i("move", "upLeft");
                return true;
            }
        });

        Button upCenter = (Button)findViewById(R.id.upCenter);
        upCenter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m)
            {
                Log.i("move", "upCenter");
                return true;
            }
        });
        */
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        renderer.start();
        glView.onResume();
    }
    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        renderer.stop();
        glView.onPause();
    }

    /*
    public void onSensorChanged(SensorEvent event)
    {
        //Log.i("onSensorChanged", event.toString());
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
        {
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            Log.i("rotationSensor", new Float(mRotationMatrix[0]).toString());
        }
    }
    */


    class CubeRenderer implements GLSurfaceView.Renderer, SensorEventListener
    {

        private float[] mRotationMatrix = new float[16];
        private Sensor mSensor;
        private Cube cube;

        public CubeRenderer()
        {
            // find the rotation-vector sensor
            mSensor = mSensorManager.getDefaultSensor(
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
            mSensorManager.registerListener(this, mSensor, 10000);
        }

        public void stop()
        {
            mSensorManager.unregisterListener(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
