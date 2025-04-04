package student;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
    private static final long SEED = 1234L;
    Grid grid = new Grid(testGrid(), SEED, true);

    //Initializes a Grid for testing
    BufferedImage testGrid() {
        BufferedImage testImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        testImage.setRGB(0, 0, 220 - 40 - 200);
        testImage.setRGB(0, 1, 61 - 72 - 250);
        testImage.setRGB(0, 2, 51 - 45 - 245);
        testImage.setRGB(1, 0, 11 - 209 - 97);
        testImage.setRGB(1, 1, 142 - 204 - 189);
        testImage.setRGB(1, 2, 39 - 38 - 35);
        testImage.setRGB(2, 0, Color.BLACK.getRGB());
        testImage.setRGB(2, 1, Color.BLACK.getRGB());
        testImage.setRGB(2, 2, Color.BLACK.getRGB());
        return testImage;
    }

    boolean compareImage(BufferedImage thisImage, BufferedImage otherImage) {
        int height = thisImage.getHeight();
        int width = thisImage.getWidth();
        int otherHeight = otherImage.getHeight();
        int otherWidth = otherImage.getWidth();
        boolean value = true;
        if (height != otherHeight || width != otherWidth) {
            value = false;
        } else {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (thisImage.getRGB(i, j) != (otherImage.getRGB(i, j))) {
                        value = false;
                    }
                }
            }
        }
        return value;
    }


    @Test
    public void removeRandomColumnTest() throws RequestFailedException {
        for (int i = 0; i < 10; i++) {
            Grid testGrid = new Grid(testGrid(), SEED, true);

            //Calls the AbstractGrid method, removeRandomColumn for test
            testGrid.removeRandomColumn();
            //Converts abstractGrid to BufferedImage
            BufferedImage newImage = testGrid.convertToBufferedImage();

            //Assertions
            assertEquals(2, newImage.getWidth());
        }
    }

    @Test
    public void undoTest() throws RequestFailedException {
        BufferedImage originalImage = testGrid();

        Grid gridTest = new Grid(originalImage, SEED, true);
        gridTest.removeBluestColumn();
        gridTest.removeRandomColumn();
        gridTest.undo();
        gridTest.undo();


        BufferedImage resultImage = gridTest.convertToBufferedImage();

        //checks if the width and height of the images are the same
        assertEquals(resultImage.getWidth(), originalImage.getWidth());
        assertEquals(resultImage.getHeight(), originalImage.getHeight());

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                assertEquals(originalImage.getRGB(x, y), resultImage.getRGB(x, y));
                //originalImage.getRGB(x, y), resultImage.getRGB(x, y)
                //compareImage(originalImage, resultImage);
            }
        }
    }
}

