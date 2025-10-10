package com.yukidayo.bsaclient.lwjgl3; // ✅ SIEMPRE debe ir primero

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.bsa.game.MainGame; // tu clase principal del juego

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MainGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("battlestaracademia client");

        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        configuration.setWindowedMode(1280, 720);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0);

        return configuration;
    }
}
