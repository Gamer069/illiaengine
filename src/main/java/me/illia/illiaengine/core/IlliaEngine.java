package me.illia.illiaengine.core;

import me.illia.illiaengine.listener.KeyListener;
import me.illia.illiaengine.listener.MouseListener;
import me.illia.illiaengine.scene.Level;
import me.illia.illiaengine.scene.LevelEditor;
import me.illia.illiaengine.scene.Scene;
import me.illia.illiaengine.utils.time.TimeUtil;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.logging.Logger;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static java.util.logging.Level.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class IlliaEngine {
    int windowWidth, windowHeight;
    protected String title;
    protected long window;
    protected static Scene scene;
    public static final Logger logger = Logger.getLogger("illia_engine");
    public IlliaEngine(int windowWidth, int windowHeight, String title) {
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.title = title;
    }
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0: scene =
                    new LevelEditor();
                    scene.init();
                    break;
            case 1: scene =
                    new Level();
                    scene.init();
                    break;
            default:
                assert false : "A strange looking scene going in here, " + newScene + ", huh?";
                break;
        }
    }
    public IlliaEngine runEngine() {
        System.out.println(Version.getVersion());

        initEngine();
        loopProcess();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
        return this;
    }
    private void loopProcess() {
        float beginTime = TimeUtil.getTime();
        float endTime = TimeUtil.getTime();
        float deltaTime = -1.0f;
        float fps = 1.0f / deltaTime;
        createCapabilities();
        changeScene(0);
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        soutInfo();
        while (!glfwWindowShouldClose(window)) {
            CharSequence fpsCharSequence = ", Your STUPID FPS: " + fps;
            fpsCharSequence.length();
            glfwSetWindowTitle(window, title + fpsCharSequence);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            if (deltaTime >= 0) scene.update(deltaTime);
            glfwSwapBuffers(window);
            glfwPollEvents();
            if (KeyListener.isKeyPressed(GLFW_KEY_EQUAL)) {
                System.out.println("Exiting");
                glfwSetWindowShouldClose(window, true);
            }
            endTime = TimeUtil.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
            fps = 1.0f / deltaTime;
        }
    }

    private void initEngine() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("For some reason GLFW failed to init");

        // WINDOW HINTS
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        
        window = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
        if (window == NULL) throw new Error("Failed to create the window");
        System.out.println(window);
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (vidMode == null) throw new Error("vidMode is null");
            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
        }

        // CALLBACKS
        glfwSetCursorPosCallback(window, MouseListener::posCallback);
        glfwSetMouseButtonCallback(window, MouseListener::btnCallback);
        glfwSetScrollCallback(window, MouseListener::scrollCallback);
        glfwSetKeyCallback(window, KeyListener::keyCallback);
        
        // CONTEXT STUFF
        glfwMakeContextCurrent(window);
        // V-SYNC
        glfwSwapInterval(1);

        // SHOWING THE WINDOW
        glfwShowWindow(window);
        soutInfo();
    }
    public void soutInfo() {
        if (!glfwWindowShouldClose(window)) {
            logger.log(INFO, "Press = to quit");
        } else if (glfwWindowShouldClose(window)) logger.log(INFO, "Quitting...");
    }
}
