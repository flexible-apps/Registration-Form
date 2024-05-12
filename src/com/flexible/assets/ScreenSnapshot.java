package com.flexible.assets;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenSnapshot {
    public static void snap(AnchorPane root) {
        root.getScene().setOnKeyPressed(keyEvent -> {
            System.out.println("start write image");
            if (keyEvent.getCode().equals(KeyCode.P)) {
                WritableImage snapshot = root.snapshot(new SnapshotParameters(), null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot,
                        new BufferedImage((int) snapshot.getWidth(), (int) snapshot.getHeight(), BufferedImage.TYPE_INT_RGB));
                try {
                    ImageIO.write(bufferedImage, "png", new FileOutputStream("C:\\Users\\sms-info\\Desktop\\Wallpapers Ai\\ai\\Registration.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
