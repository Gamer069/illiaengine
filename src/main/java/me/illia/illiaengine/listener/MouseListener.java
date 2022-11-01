package me.illia.illiaengine.listener;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    protected static final MouseListener INSTANCE = new MouseListener();

    private double scrollX = 0.0,
                   scrollY = 0.0,
                   x = 0.0,
                   y = 0.0,
                   lastY = 0.0,
                   lastX = 0.0;

    private boolean[] mouseBtnPressed = new boolean[3];
    private boolean isDragging;
    public static MouseListener getInstance() {
        return INSTANCE;
    }
    public static void posCallback(long window, double x, double y) {
        INSTANCE.lastX = x;
        INSTANCE.lastY = y;
        INSTANCE.x = x;
        INSTANCE.y = y;
        INSTANCE.isDragging = INSTANCE.mouseBtnPressed[0] | INSTANCE.mouseBtnPressed[1] | INSTANCE.mouseBtnPressed[2];
    }
    public static void btnCallback(long window, int btn, int action, int mods) {
        if (action == GLFW_PRESS && check(btn)) INSTANCE.mouseBtnPressed[btn] = true;
        else if (action == GLFW_RELEASE) {
            INSTANCE.mouseBtnPressed[btn] = false;
            INSTANCE.isDragging = false;
        }
    }
    public static void scrollCallback(long window, double xOffset, double yOffset) {
        INSTANCE.scrollX = xOffset;
        INSTANCE.scrollY = yOffset;
    }
    public static void endFrame() {
        INSTANCE.scrollX = 0;
        INSTANCE.scrollY = 0;
        INSTANCE.lastX = INSTANCE.x;
        INSTANCE.lastY = INSTANCE.y;
    }
    public static void setDoublesToNull() {
        INSTANCE.scrollX = 0.0;
        INSTANCE.scrollY = 0.0;
        INSTANCE.y = 0.0;
        INSTANCE.x = 0.0;
        INSTANCE.lastX = 0.0;
        INSTANCE.lastY = 0.0;
    }

    public static float getX() {
        return (float)INSTANCE.x;
    }

    public static float getY() {
        return (float)INSTANCE.y;
    }
    public static float getDx() {
        return (float)(INSTANCE.lastX - INSTANCE.x);
    }
    public static float getDy() {
        return (float)(INSTANCE.lastY - INSTANCE.y);
    }

    public static float getScrollX() {
        return (float)INSTANCE.scrollX;
    }

    public static float getScrollY() {
        return (float)INSTANCE.scrollY;
    }

    public boolean isDragging() {
        return isDragging;
    }
    public static boolean btnDown(int btn) {
        if (check(btn)) {
            return INSTANCE.mouseBtnPressed[btn];
        } else {
            return false;
        }
    }
    public static boolean check(int btn) {
        return btn < INSTANCE.mouseBtnPressed.length;
    }
}
