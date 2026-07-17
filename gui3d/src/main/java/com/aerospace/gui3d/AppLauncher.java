package com.aerospace.gui3d;

/**
 * Entry point kept separate from JavaFX Application so packaged classpath
 * launches work consistently on every supported platform.
 */
public final class AppLauncher {

    private AppLauncher() {
    }

    public static void main(String[] args) {
        App.main(args);
    }
}
