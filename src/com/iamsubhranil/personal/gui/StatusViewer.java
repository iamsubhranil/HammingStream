package com.iamsubhranil.personal.gui;/**
 * Author : Nil
 * Date : 11/3/2016 at 9:07 PM.
 * Project : HammingStream
 */

import com.iamsubhranil.personal.HammingWriter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StatusViewer extends Application {

    private static boolean alreadyCalled = false;

    private static Label readLabel;
    private static Label wroteLabel;

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));
        Scene primaryScene = new Scene(vBox);
        primaryStage.setScene(primaryScene);
        primaryScene.getStylesheets().add("styles/MyMetro.css");
        readLabel = new Label("Starting read..");
        wroteLabel = new Label("Starting write..");
        vBox.setSpacing(20);
        vBox.getChildren().addAll(readLabel, wroteLabel);
        primaryStage.show();
        Platform.runLater(() -> {
            readLabel.textProperty().bind(HammingWriter.readProperty);
            wroteLabel.textProperty().bind(HammingWriter.writeProperty);
            HammingWriter.main(new String[]{});
        });
    }
}
