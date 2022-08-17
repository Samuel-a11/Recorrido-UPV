package com.upv.pm_2022.iti_27849_u3_equipo_04;

/*
Ver enlace para detalles de interacción con el Control de XBox
https://developer.android.com/training/game-controllers/controller-input

 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

//import com.upv.pm_2022.iti_27849_u3_equipo_04.R;

//import com.andretietz.android.controller.ActionView;
//import com.andretietz.android.controller.DirectionView;
//import com.andretietz.android.controller.InputView;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {

    private float previousX;
    private float previousY;

    final static int UP       = 0;
    final static int LEFT     = 1;
    final static int RIGHT    = 2;
    final static int DOWN     = 3;

    private GLSurfaceView mGLView;
    private CubeRenderer mRenderer;

    int Joystick_Izquierdo_Derecho;


    @SuppressLint("ClickableViewAccessibility") // Medicina Necesaria
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Se bloque la orientacion vertical
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Joystick_Izquierdo_Derecho=0;

        mGLView = findViewById(R.id.OpenGL1_surfaceView);
        mGLView.setEGLContextClientVersion(2);

        mRenderer = new CubeRenderer(this);
        mGLView.setRenderer(mRenderer);

        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        mRenderer.ReiniciarPosiciones();

    }

    // ----- Para mando conectado (Botones A y B)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BUTTON_Y:
                        mRenderer.MoveUpward( 5f ); ;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_A:
                        //mRenderer.MoveUpward( -5f ); ;
                        mRenderer.ReiniciarPosiciones();
                        break;

                    case KeyEvent.KEYCODE_BUTTON_X:
                        mRenderer.MoveUpward( -5f );
                        Log.d("control","Abajo");
                        break;
                }
            }
            if (handled) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    // ----- Para mando conectado (Pad direccional o cruceta) ----------------------------
    Dpad dpad = new Dpad();

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        //DPAD
        if (Dpad.isDpadDevice(event)) {
            int press = dpad.getDirectionPressed(event);
            switch (press) {
                case LEFT:
                    mRenderer.StrafeRight( -10f );
                    //mRenderer.StrafeRight( -10f );
                    return true;
                case RIGHT:
                    mRenderer.StrafeRight( 10f );
                    return true;
                case UP:
                    mRenderer.MoveForward( -10f );
                    return true;
                case DOWN:
                    mRenderer.MoveForward( 10f ) ;
                    return true;
            }
        }

        // ----- Para mando conectado (Joystick) ----------------------------

        float x = event.getX();
        float y = event.getY();

        float dx = x - previousX;
        float dy = y - previousY;

        int valor = 30;
        // Joystick Izquierdo
        if (Joystick_Izquierdo_Derecho==0) {
            switch (processJoystickInput(event, -1)) {
                case "(0,-1)": // UP
                    mRenderer.MoveForward( -3f );
                    Log.d("control", "Joystick UP"+dx);
                    break;
                case "(0,1)": // DOWN
                    mRenderer.MoveForward( 3f ) ;
                    Log.d("control", "Joystick DOWN"+dx);
                    break;
                case "(-1,0)": // LEFT
                    mRenderer.StrafeRight( -3f );
                    Log.d("control", "Joystick LEFT"+dx);
                    break;
                case "(1,0)": // RIGHT
                    mRenderer.StrafeRight( 3f );
                    Log.d("control", "Joystick RIGHT"+dx);
                    break;
            }
        } // Joystick Derecho
        else {
            switch (processJoystickInput(event, -1)) {
                case "(0,-1)": // UP

                    Log.d("control", "Joystick UP"+dx);
                    mRenderer.RotateX(10);
                    break;
                case "(0,1)": // DOWN

                    Log.d("control", "Joystick DOWN"+dx);
                    mRenderer.RotateX(-10);
                    break;
                case "(-1,0)": // LEFT
                    mRenderer.RotateY(10);
                    //mRenderer.RotateY(dy * valor);
                    Log.d("control", "Joystick LEFT"+dx);
                    break;
                case "(1,0)": // RIGHT
                    //mRenderer.RotateY(-dy * valor);
                    mRenderer.RotateY(-10);
                    Log.d("control", "Joystick RIGHT"+dx);
                    break;
            }

        }

        previousX = x;
        previousY = y;

        // --------------------------------------------
        // --------------------------------------------
        // --------------------------------------------

        if(event.getAction()==MotionEvent.AXIS_RTRIGGER){
            Log.d("control","Arriba");
        }
        return true;

    }

    // Version 2.0. Detecta cual de los dos Joysticks esta siendo utilizado, para
    // realizar movimientos diferenciados entre ambos

    private String processJoystickInput(MotionEvent event, int historyPos) {
        InputDevice inputDevice = event.getDevice();
        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float x = getCenteredAxis(event, inputDevice,MotionEvent.AXIS_X, historyPos);
        if (x == 0) {
            x = getCenteredAxis(event, inputDevice,MotionEvent.AXIS_Z, historyPos);
            if (x != 0) {
                Log.d("control", "Derecho");
                Joystick_Izquierdo_Derecho=1;
            }
        } else {
            Log.d("control","Izquierdo");
            Joystick_Izquierdo_Derecho=0;
        }
        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        float y = getCenteredAxis(event, inputDevice,MotionEvent.AXIS_Y, historyPos);
        if (y == 0) {
            y = getCenteredAxis(event, inputDevice,MotionEvent.AXIS_RZ, historyPos);
            if (y != 0) {
                Log.d("control", "Derecho");
                Joystick_Izquierdo_Derecho=1;
            }
        }
        else {
            Log.d("control","Izquierdo");
            Joystick_Izquierdo_Derecho=0;

        }
        String coordenada = "("+(int)x+","+(int)y+")";
        return coordenada;
        // Update the ship object based on the new x and y values
    }


    private static float getCenteredAxis(MotionEvent event, InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());
        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value = historyPos < 0 ? event.getAxisValue(axis):event.getHistoricalAxisValue(axis, historyPos);
            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }


    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        //((TextView)findViewById(R.id.TV1)).setText("UPV Recorrido Virtual");
    }
}