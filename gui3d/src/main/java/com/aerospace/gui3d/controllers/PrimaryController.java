package com.aerospace.gui3d.controllers;

import com.aerospace.gui3d.App;
import com.aerospace.gui3d.AppMode;
import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void startDemo() throws IOException {
        App.openViewer(AppMode.DEMO);
    }

    @FXML
    private void startDatabase() throws IOException {
        App.openViewer(AppMode.DATABASE);
    }
}
