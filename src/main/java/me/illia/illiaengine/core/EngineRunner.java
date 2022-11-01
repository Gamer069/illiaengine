package me.illia.illiaengine.core;

public class EngineRunner {
    public static void main(String[] args) {
        run();
    }
    public static void run() {
        IlliaEngine engine = new IlliaEngine(900, 900, "Illia Engine").runEngine();
    }
}
