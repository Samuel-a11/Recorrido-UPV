package com.upv.pm_2022.iti_27849_u3_equipo_04;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Line {

    private float[] VERTICES = new float[720];


    // Vertex colors.
    private float[] COLORS = new float[960];


    // Order to draw vertices as triangles.
    private byte[] INDICES = new byte[240];

    // Number of coordinates per vertex in {@link VERTICES}.
    private static final int COORDS_PER_VERTEX = 3;

    // Number of values per colors in {@link COLORS}.
    private static final int VALUES_PER_COLOR = 4;

    // Vertex size in bytes.
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    // Color size in bytes.
    private final int COLOR_STRIDE = VALUES_PER_COLOR * 4;

    /** Shader code for the vertex. */
    private static final String VERTEX_SHADER_CODE = "uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;"
            + "attribute vec4 vColor;" + "varying vec4 _vColor;" + "void main() {" + "  _vColor = vColor;"
            + "  gl_Position = uMVPMatrix * vPosition;" + "}";

    /** Shader code for the fragment. */
    private static final String FRAGMENT_SHADER_CODE = "precision mediump float;" + "varying vec4 _vColor;"
            + "void main() {" + "  gl_FragColor = _vColor;" + "}";

    private final FloatBuffer mVertexBuffer;
    private final FloatBuffer mColorBuffer;
    private final ByteBuffer mIndexBuffer;
    private final int mProgram;
    private final int mPositionHandle;
    private final int mColorHandle;
    private final int mMVPMatrixHandle;

    public Line() {

        for(int i = 0; i<120; i++){
            // Front face
            if(i<10) {
                VERTICES[(6 * i)] = -2.5f;
                VERTICES[(6 * i) + 1] = (2.5f / 2) - (2.5f / 10) * i;
                VERTICES[(6 * i) + 2] = 2.5f;
                VERTICES[(6 * i) + 3] = 2.5f;
                VERTICES[(6 * i) + 4] = (2.5f / 2) - (2.5f / 10) * i;
                VERTICES[(6 * i) + 5] = 2.5f;
            }
            if(i>=10 && i<20){
                VERTICES[(6 * i)]     = -2.5f + (5.0f / 10) * (i-10); //x
                VERTICES[(6 * i) + 1] = 2.5f / 2; //y
                VERTICES[(6 * i) + 2] = 2.5f; //z
                VERTICES[(6 * i) + 3] = -2.5f + (5.0f / 10) * (i-10); //x
                VERTICES[(6 * i) + 4] = -2.5f / 2; // y
                VERTICES[(6 * i) + 5] = 2.5f; //z
            }
            // Back Face
            if(i>=20 && i<30) {
                VERTICES[(6 * i)] = -2.5f;
                VERTICES[(6 * i) + 1] = (2.5f / 2) - (2.5f / 10) * (i - 20);
                VERTICES[(6 * i) + 2] = -2.5f;
                VERTICES[(6 * i) + 3] = 2.5f;
                VERTICES[(6 * i) + 4] = (2.5f / 2) - (2.5f / 10) * (i - 20);
                VERTICES[(6 * i) + 5] = -2.5f;
            }
            if(i>=30 && i<40){
                VERTICES[(6 * i)]     = -2.5f + (5.0f / 10) * (i-30); //x
                VERTICES[(6 * i) + 1] = 2.5f / 2; //y
                VERTICES[(6 * i) + 2] = -2.5f; //z
                VERTICES[(6 * i) + 3] = -2.5f + (5.0f / 10) * (i-30); //x
                VERTICES[(6 * i) + 4] = -2.5f / 2; // y
                VERTICES[(6 * i) + 5] = -2.5f; //z
            }
            // Right face
            if(i>=40 && i<50) {
                VERTICES[(6 * i)] = 2.5f;
                VERTICES[(6 * i) + 1] = (2.5f/2) - (2.5f / 10) * (i-40);
                VERTICES[(6 * i) + 2] = 2.5f;
                VERTICES[(6 * i) + 3] = 2.5f;
                VERTICES[(6 * i) + 4] = (2.5f/2) - (2.5f / 10) * (i-40);
                VERTICES[(6 * i) + 5] = -2.5f;
            }
            if(i>=50 && i<60){
                VERTICES[(6 * i)]     = 2.5f ; //x
                VERTICES[(6 * i) + 1] = 2.5f / 2; //y
                VERTICES[(6 * i) + 2] = -2.5f+ (5.0f / 10) * (i-50); //z
                VERTICES[(6 * i) + 3] = 2.5f; //x
                VERTICES[(6 * i) + 4] = -2.5f / 2; // y
                VERTICES[(6 * i) + 5] = -2.5f+ (5.0f / 10) * (i-50); //z
            }

            // Left face
            if(i>=60 && i<70) {
                VERTICES[(6 * i)] = -2.5f;
                VERTICES[(6 * i) + 1] = (2.5f/2) - (2.5f / 10) * (i-60);
                VERTICES[(6 * i) + 2] = 2.5f;
                VERTICES[(6 * i) + 3] = -2.5f;
                VERTICES[(6 * i) + 4] = (2.5f/2) - (2.5f / 10) * (i-60);
                VERTICES[(6 * i) + 5] = -2.5f;
            }
            if(i>=70 && i<80){
                VERTICES[(6 * i)]     = -2.5f ; //x
                VERTICES[(6 * i) + 1] = 2.5f / 2; //y
                VERTICES[(6 * i) + 2] = -2.5f+ (5.0f / 10) * (i-70); //z
                VERTICES[(6 * i) + 3] = -2.5f; //x
                VERTICES[(6 * i) + 4] = -(2.5f / 2); // y
                VERTICES[(6 * i) + 5] = -2.5f+ (5.0f / 10) * (i-70); //z
            }

            // top face
            if(i>=80 && i<90) {
                VERTICES[(6 * i)] = -2.5f;
                VERTICES[(6 * i) + 1] = 2.5f/2;
                VERTICES[(6 * i) + 2] = -2.5f + (5.0f/10) * (i-80);
                VERTICES[(6 * i) + 3] = 2.5f;
                VERTICES[(6 * i) + 4] = 2.5f/2;
                VERTICES[(6 * i) + 5] = -2.5f + (5.0f/10) * (i-80);
            }
            if(i>=90 && i<100) {
                VERTICES[(6 * i)] = -2.5f + (5.0f / 10) * (i - 90);
                VERTICES[(6 * i) + 1] = 2.5f / 2;
                VERTICES[(6 * i) + 2] = -2.5f;
                VERTICES[(6 * i) + 3] = -2.5f + (5.0f / 10) * (i - 90);
                VERTICES[(6 * i) + 4] = 2.5f / 2;
                VERTICES[(6 * i) + 5] = 2.5f;
            }

            // top face
            if(i>=100 && i<110) {
                VERTICES[(6 * i)] = -2.5f;
                VERTICES[(6 * i) + 1] = -2.5f/2;
                VERTICES[(6 * i) + 2] = -2.5f + (5.0f/10) * (i-100);
                VERTICES[(6 * i) + 3] = 2.5f;
                VERTICES[(6 * i) + 4] = -2.5f/2;
                VERTICES[(6 * i) + 5] = -2.5f + (5.0f/10) * (i-100);
            }
            if(i>=110 && i<120) {
                VERTICES[(6 * i)] = -2.5f + (5.0f / 10) * (i - 110);
                VERTICES[(6 * i) + 1] = -2.5f / 2;
                VERTICES[(6 * i) + 2] = -2.5f;
                VERTICES[(6 * i) + 3] = -2.5f + (5.0f / 10) * (i - 110);
                VERTICES[(6 * i) + 4] = -2.5f / 2;
                VERTICES[(6 * i) + 5] = 2.5f;
            }

        }

        for(int i = 0; i<240; i++){
            if(i<80) {
                COLORS[(4 * i)] = 1.0f;
                COLORS[(4 * i) + 1] = 1.0f;
                COLORS[(4 * i) + 2] = 1.0f;
                COLORS[(4 * i) + 3] = 1.0f;
            }
            if(i>=80 && i<160) {
                COLORS[(4 * i)] = 0.0f;
                COLORS[(4 * i) + 1] = 0.0f;
                COLORS[(4 * i) + 2] = 1.0f;
                COLORS[(4 * i) + 3] = 1.0f;
            }
            if(i>=160 && i<240) {
                COLORS[(4 * i)] = 1.0f;
                COLORS[(4 * i) + 1] = 0.0f;
                COLORS[(4 * i) + 2] = 0.0f;
                COLORS[(4 * i) + 3] = 1.0f;
            }
        }

        for(int i = 0; i<240; i++){
            INDICES[i] = (byte)i;
        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(VERTICES.length * 4);

        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(VERTICES);
        mVertexBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(COLORS.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuffer.asFloatBuffer();
        mColorBuffer.put(COLORS);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(INDICES.length);
        mIndexBuffer.put(INDICES);
        mIndexBuffer.position(0);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE));
        GLES20.glAttachShader(mProgram, loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE));
        GLES20.glLinkProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix The Model View Project matrix in which to draw this shape
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment.
        GLES20.glUseProgram(mProgram);

        // Prepare the cube coordinate data.
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);

        // Prepare the cube color data.
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, COLOR_STRIDE, mColorBuffer);

        // Apply the projection and view transformation.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the cube.
        GLES20.glDrawElements(GLES20.GL_LINES, INDICES.length, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);

        // Disable vertex arrays.
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }

    /** Loads the provided shader in the program. */
    private static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

}