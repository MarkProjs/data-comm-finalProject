/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class JavaFX extends HBox {

    private MyMqtt mqtt;
    private final Threads threads;
    private PiKeyStore keystore;

    //setting the tiles
    //mark's tiles
    private Tile markHumidTile;
    private Tile markTempTile;
    private final TextArea markDoorBellTxtA = new TextArea();
    private final TextArea markSensorTxtA = new TextArea();
    private Tile markImageTile;

    //Anotnio's tiles
    private Tile antHumidTile;
    private Tile antTempTile;
    private final TextArea antDoorBellTxtA = new TextArea();
    private final TextArea antSensorTxtA= new TextArea();
    private Tile antImageTile;

    //Jeremy's tiles
    private Tile jerHumidTile;
    private Tile jerTempTile;
    private final TextArea jerDoorBellTxtA = new TextArea();
    private final TextArea jerSensorTxtA = new TextArea();
    private Tile jerImageTile;

    public JavaFX() throws IOException {
        threads = new Threads();

        //Build the screen
        this.buildMqttPrompt();
    }

    private void buildMqttPrompt() throws IOException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Log into MQTT");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction((e) -> {
            actiontarget.setFill(Color.BLACK);
            actiontarget.setText("Signing in...");
            try {
                this.mqtt = new MyMqtt(userTextField.getText(), pwBox.getText());
                this.mqtt.getData("project/jeremy", jerDoorBellTxtA, jerSensorTxtA, jerHumidTile, jerTempTile, jerImageTile);
                this.mqtt.getData("project/antonio", antDoorBellTxtA, antSensorTxtA, antHumidTile, antTempTile, antImageTile);
                this.mqtt.connectClient();
                this.mqtt.subscribe("project/jeremy");
                this.mqtt.subscribe("project/antonio");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully signed in with user: " + userTextField.getText());
                alert.showAndWait();
                buildKeystorePrompt();
            } catch (Exception exc) {
                Logger.getLogger(JavaFX.class.getName()).log(Level.SEVERE, null, exc);
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Sign in failed");
                System.out.println(exc);
                userTextField.clear();
                pwBox.clear();
            }
        });

        this.getChildren().clear();
        this.getChildren().add(grid);
    }

    private void buildKeystorePrompt() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Credentials for Keystore");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userPath = new Label("File Path:");
        grid.add(userPath, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Load");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        btn.setOnAction((e) -> {
            try {
                this.keystore = new PiKeyStore(pwBox.getText().toCharArray(), userTextField.getText());
                this.mqtt.setKeyStore(this.keystore);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully loaded keystore");
                alert.showAndWait();
                this.buildScreen();
                this.threads.startDoorBellThread(markDoorBellTxtA);
                this.threads.startDHTThread(markHumidTile, markTempTile);
                this.threads.startSenseLEDThread(markSensorTxtA, markImageTile);
                this.threads.startPublishThread(mqtt, "project/jeremy", markDoorBellTxtA, markSensorTxtA, markHumidTile, markTempTile, markImageTile, this.keystore);
            } catch (Exception exc) {
                Logger.getLogger(JavaFX.class.getName()).log(Level.SEVERE, null, exc);
                System.out.println(exc);
                userTextField.clear();
                pwBox.clear();
            }
        });

        this.getChildren().clear();
        this.getChildren().add(grid);
    }

    private void buildScreen() throws IOException {
        //create the doorBell Tile
        markDoorBellTxtA.setEditable(false);
        markDoorBellTxtA.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: #1E90FF;"
                + "-fx-text-box-border: transparent;");
        VBox markTxtAVboxDb = new VBox(markDoorBellTxtA);
        var markDoorBellTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Mark's doorbell tile")
                .graphic(markTxtAVboxDb)
                .build();
        
        antDoorBellTxtA.setEditable(false);
        antDoorBellTxtA.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: #1E90FF;"
                + "-fx-text-box-border: transparent;");
        VBox antTxtAVboxDb = new VBox(antDoorBellTxtA);
        var antDoorBellTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Antonio's doorbell tile")
                .graphic(antTxtAVboxDb)
                .build();
        
        jerDoorBellTxtA.setEditable(false);
        jerDoorBellTxtA.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: #1E90FF;"
                + "-fx-text-box-border: transparent;");
        VBox jerTxtAVboxDb = new VBox(jerDoorBellTxtA);
        var jerDoorBellTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Jeremy's doorbell tile")
                .graphic(jerTxtAVboxDb)
                .build();

        //create sensor tile
        markSensorTxtA.setEditable(false);
        markSensorTxtA.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: #1E90FF;"
                + "-fx-text-box-border: transparent;");
        VBox markTxtAVboxSen = new VBox(markSensorTxtA);
        var markSensorTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Mark's Sensor tile")
                .graphic(markTxtAVboxSen)
                .build();
        
        antSensorTxtA.setEditable(false);
        antSensorTxtA.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: #1E90FF;"
                + "-fx-text-box-border: transparent;");
        VBox antTxtAVboxSen = new VBox(antSensorTxtA);
        var antSensorTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Antonio's Sensor tile")
                .graphic(antTxtAVboxSen)
                .build();
        
        jerSensorTxtA.setEditable(false);
        jerSensorTxtA.setStyle("-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-inner-color: #1E90FF;"
                + "-fx-text-box-border: transparent;");
        VBox jerTxtAVboxSen = new VBox(jerSensorTxtA);
        var jerSensorTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Jeremy's Sensor tile")
                .graphic(jerTxtAVboxSen)
                .build();

        //create tempTile
        markTempTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(350, 300)
                .title("Mark's Temp Tile")
                .unit("C")
                .threshold(75)
                .build();
        markTempTile.setValue(0.0);

        antTempTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(350, 300)
                .title("Antonio's Temp Tile")
                .unit("C")
                .threshold(75)
                .build();
        antTempTile.setValue(0.0);

        jerTempTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(350, 300)
                .title("Jeremy's Temp Tile")
                .unit("C")
                .threshold(75)
                .build();
        jerTempTile.setValue(0.0);
        //Create humidTile
        markHumidTile = TileBuilder.create()
                .skinType(Tile.SkinType.PERCENTAGE)
                .prefSize(350, 300)
                .title("Mark's Humid Tile")
                .unit("g.m-3")
                .maxValue(60)
                .build();
        markHumidTile.setValue(0.0);

        antHumidTile = TileBuilder.create()
                .skinType(Tile.SkinType.PERCENTAGE)
                .prefSize(350, 300)
                .title("Antonio's Humid Tile")
                .unit("g.m-3")
                .maxValue(60)
                .build();
        antHumidTile.setValue(0.0);

        jerHumidTile = TileBuilder.create()
                .skinType(Tile.SkinType.PERCENTAGE)
                .prefSize(350, 300)
                .title("Jeremy's Humid Tile")
                .unit("g.m-3")
                .maxValue(60)
                .build();

        jerHumidTile.setValue(0.0);
        //setup the image tile
        markImageTile = TileBuilder.create()
                .skinType(Tile.SkinType.IMAGE)
                .prefSize(350, 300)
                .title("Mark's Image Tile")
                .imageMask(Tile.ImageMask.RECTANGULAR)
                .build();

        antImageTile = TileBuilder.create()
                .skinType(Tile.SkinType.IMAGE)
                .prefSize(350, 300)
                .title("Antonio's Image Tile")
                .image(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/defaultImage/sunny-clip-art.png"))))
                .imageMask(Tile.ImageMask.RECTANGULAR)
                .build();

        jerImageTile = TileBuilder.create()
                .skinType(Tile.SkinType.IMAGE)
                .prefSize(350, 300)
                .title("Jeremy's Image Tile")
                .imageMask(Tile.ImageMask.RECTANGULAR)
                .build();
        
        //Setup a tile with an exit button to end the application
        var exitButton = new Button("Exit");
        //Setup event handler for the exit button
        exitButton.setOnAction(e -> endApplication());
        //Setup the tile
        var exitTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Quit the application")
                .graphic(exitButton)
                .roundedCorners(false)
                .build();

        var column1 = new HBox(markHumidTile, markTempTile, markSensorTile, markDoorBellTile, markImageTile);
        var column2 = new HBox(antHumidTile, antTempTile, antSensorTile, antDoorBellTile, antImageTile);
        var column3 = new HBox(jerHumidTile, jerTempTile, jerSensorTile, jerDoorBellTile, jerImageTile);
        var elems = new VBox(column1, column2, column3, exitTile);

        this.getChildren().clear();
        //adding to the main screen
        this.getChildren().add(elems);
        this.setSpacing(5);

    }

    private void endApplication() {
        this.threads.endThreads();
        this.mqtt.disconnect();
        Platform.exit();

    }
}
