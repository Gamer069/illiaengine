package me.illia.illiaengine.listener;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static final KeyListener INSTANCE = new KeyListener();
    private final boolean[] keyPressed = new boolean[350];
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) INSTANCE.keyPressed[key] = true;
        else if (action == GLFW_RELEASE) INSTANCE.keyPressed[key] = false;
    }
    public static boolean isKeyPressed(int keyCode) {
        return INSTANCE.keyPressed[keyCode];
    }
    public static KeyListener getInstance() {
        return INSTANCE;
    }
}
