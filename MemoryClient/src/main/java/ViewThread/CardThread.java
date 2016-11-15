package ViewThread;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.util.Map;

/**
 * Created by Pieter-Jan on 14/11/2016.
 */
public class CardThread implements Runnable {
    private ImageView imageView;
    private Map<Integer, Image> images;
    private int afbeeldingId;
    private Image backImage = null;

    public CardThread(ImageView imageView, Map<Integer, Image> images, int afbeeldingId) {
        this.imageView = imageView;
        this.images = images;
        this.afbeeldingId = afbeeldingId;
    }

    public CardThread(ImageView imageView, Image backImage) {
        this.imageView = imageView;
        this.backImage = backImage;
    }

    @Override
    public void run() {
        if (backImage == null) {
            imageView.setImage(images.get(afbeeldingId));
        }else{
            imageView.setImage(backImage);
        }

        imageView.snapshot(new SnapshotParameters(), new WritableImage(1, 1));
    }
}
