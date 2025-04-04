package student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

// You must only call the constructors that are declared in AbstractGrid.
// You must not call methods that are declared in your Grid implementation.
public class AbstractGridTest {
    private static final long SEED = 1234L;

    // You must not change this constructor.
    // Use only this to create test fixtures.
    public AbstractGrid makeGrid(BufferedImage bufferedImage) {
        return new Grid(bufferedImage, SEED, true);
    }

    //Create a test BufferedImage and AbstractGrid Instance
    BufferedImage initializeGrid() {
        BufferedImage testImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        testImage.setRGB(0, 0, Color.blue.getRGB());
        testImage.setRGB(0, 1, Color.blue.getRGB());
        testImage.setRGB(0, 2, Color.blue.getRGB());
        testImage.setRGB(1, 0, 11 - 209 - 97);
        testImage.setRGB(1, 1, 142 - 204 - 189);
        testImage.setRGB(1, 2, 39 - 38 - 35);
        testImage.setRGB(2, 0, 51 - 204 - 233);
        testImage.setRGB(2, 1, 45 - 211 - 16);
        testImage.setRGB(2, 2, 201 - 11 - 1);
        return testImage;
    }

    @Test
    public void convertToBufferedImageTest() {
        //Create a test BufferedImage and AbstractGrid Instance
        BufferedImage testImage = initializeGrid();
        AbstractGrid testGrid = makeGrid(testImage);
        //Calls the method covertToBufferedImage for testing
        BufferedImage testResultImage = testGrid.convertToBufferedImage();

        //Assertions
        assertEquals(testImage.getWidth(), testResultImage.getWidth());
        assertEquals(testImage.getHeight(), testResultImage.getHeight());

        for (int i = 0; i < testImage.getWidth(); i++) {
            for (int j = 0; j < testImage.getHeight(); j++) {
                //Get the int pixel value and Color of the point on grid for original image
                int pixel = testImage.getRGB(i, j);
                Color originalColor = new Color(pixel, true);

                //Get the int pixel value and Color of the point on grid for result image
                int resultPixel = testResultImage.getRGB(i, j);
                Color resultColor = new Color(resultPixel, true);

                //Extract the RGB components for original
                int red = originalColor.getRed();
                int green = originalColor.getGreen();
                int blue = originalColor.getBlue();

                //Extract the RGB components for result image
                int resultRed = resultColor.getRed();
                int resultGreen = resultColor.getGreen();
                int resultBlue = resultColor.getBlue();


                assertEquals(red, resultRed);
                assertEquals(green, resultGreen);
                assertEquals(blue, resultBlue);
            }
        }
    }

    @Test
    public void getBluestColumnIndexTest() throws RequestFailedException {
        AbstractGrid testGrid = makeGrid(initializeGrid());
        //Calls the AbstractGrid method, getBluestColumn for test
        int bluestColumnIndex = testGrid.getBluestColumnIndex();

        //Assertions
        assertEquals(0, bluestColumnIndex);


    }

    @Test
    public void removeBluestColumnTest() throws RequestFailedException {
        for(int i = 0; i < 10; i++){
            //Create a test BufferedImage and AbstractGrid Instance
            BufferedImage testImage = initializeGrid();
            AbstractGrid testGrid = makeGrid(testImage);
            //Calls the AbstractGrid method, removeBluestColumn for test
            testGrid.removeBluestColumn();
            //Converts abstractGrid to BufferedImage
            BufferedImage newImage = testGrid.convertToBufferedImage();

            //Assertions
            assertEquals(2, newImage.getWidth());
        }


    }

}