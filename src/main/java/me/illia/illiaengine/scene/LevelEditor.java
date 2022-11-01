package me.illia.illiaengine.scene;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditor extends Scene {
    private final String vertShaderSource = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            layout (location = 1) in vec4 aCol;
            out vec4 fCol;
            void main() {
                fCol = aCol;
                gl_Position = vec4(aPos, 1.0);
            }""";
    private final String fragShaderSource = """
            #version 330 core
            in vec4 fCol;
            out vec4 col;
            void main() {
                col = fCol;
            }""";
    private int vertID, fragID, shaderProgram, vaoID, vboID, eboID;
    private final float[] vertArray = {
            // POS               // COL
            0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // BOTTOM RIGHT 0
            -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // TOP LEFT 1
            0.5f,  0.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // TOP RIGHT 2
            -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // BOTTOM LEFT 3
    };
    private final int[] elementArray = {
            2, 1, 0, // TOP RIGHT TRIANGLE
            0, 1, 3 // BOTTOM LEFT TRIANGLE
    };
    public LevelEditor() {
    }

    private void genStuff() {
        genObjectStuff();
        createIndices();
        addVertAttributes();
    }
    private void createIndices() {
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboID = glGenBuffers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
    }
    private void addVertAttributes() {
        int posSize = 3;
        int colSize = 4;
        int floatSizeInBytes = 4;
        int vertSizeInBytes = (posSize + colSize) * floatSizeInBytes;
        glVertexAttribPointer(0, posSize, GL_FLOAT, false, vertSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colSize, GL_FLOAT, false, vertSizeInBytes, posSize * floatSizeInBytes);
        glEnableVertexAttribArray(1);
    }

    private FloatBuffer genVAOs() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(vertArray.length);
        vertBuffer.put(vertArray).flip();
        return vertBuffer;
    }
    private void genObjectStuff() {
        vboID = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, genVAOs(), GL_STATIC_DRAW);
    }

    private void compileVertShader() {
        vertID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertID, vertShaderSource);
        glCompileShader(vertID);

        int shaderI = glGetShaderi(vertID, GL_COMPILE_STATUS);
        if (shaderI == GL_FALSE) {
            int length = glGetShaderi(vertID, GL_INFO_LOG_LENGTH);
            System.out.println("Oops, \n\t Vert, The shader is dumb.");
            System.out.println(glGetShaderInfoLog(vertID, length));
            assert false : "";
        }
    }
    private void compileFragShader() {
        fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragID, fragShaderSource);
        glCompileShader(fragID);
        int shaderI = glGetShaderi(fragID, GL_COMPILE_STATUS);
        if (shaderI == GL_FALSE) {
            int length = glGetShaderi(fragID, GL_INFO_LOG_LENGTH);
            System.out.println("Oops, \n\t Frag, The shader is dumb.");
            System.out.println(glGetShaderInfoLog(fragID, length));
            assert false : "";
        }
    }
    public void compileAndLinkShaders() {
        compileVertShader();
        compileFragShader();
        linkShaders();
    }

    private void linkShaders() {
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertID);
        glAttachShader(shaderProgram, fragID);
        glLinkProgram(shaderProgram);
        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Oops, \n\t Shader program attachment failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, length));
            assert false: "";
        }
    }

    @Override
    public void init() {
        compileAndLinkShaders();
    }

    @Override
    public void update(float deltaTime) {
        enableStuff();

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
        
        disableStuff();
    }
    public void enableStuff() {
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
    public void disableStuff() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);
    }
}
