package student;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class Grid extends AbstractGrid {
    private int[][] grid; // 3D array to represent RGB values of pixels
    private Stack<BufferedImage> undoStack = new Stack<>(); // Stack to store undo information
    private int versionNumber = 0;
    private final int fullColor = 255;


    //Constructors for Grid
    Grid(BufferedImage image, long seed) {
        super(image, seed);
        initializeGrid(image);
    }

    Grid(BufferedImage image, long seed, boolean testMode) {
        super(image, seed, testMode);
        initializeGrid(image);
    }

    //initializes grid from buffered image, storing the RGB values of each pixel
    private void initializeGrid(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        grid = new int[height][width]; // Initialize grid with RGB values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = image.getRGB(x, y); // Store RGB value of each pixel
            }
        }
    }

    //converts the grid to a buffered image, and returns the buffered image
    @Override
    protected BufferedImage convertToBufferedImage() {
        int width = grid[0].length;
        int height = grid.length;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                bufferedImage.setRGB(j, i, grid[i][j]); // Set RGB values of each pixel
            }
        }
        return bufferedImage;
    }

    //Gets the bluest column of the grid and returns the index of it
    @Override
    protected int getBluestColumnIndex() throws RequestFailedException {
        int width = grid.length;
        int height = grid[0].length;

        if (width == 0) {
            throw new RequestFailedException("The Grid has no columns.");
        }

        int bluestColumn = 0;
        int maxBlueSum = Integer.MIN_VALUE;

        for (int i = 0; i < height; i++) {
            int blueSum = 0;

            for (int j = 0; j < width; j++) {
                int pixel = grid[j][i];
                Color color = new Color(pixel, true);

                //Extract the RGB components for original
                blueSum += color.getBlue();
            }

            if (blueSum > maxBlueSum) {
                maxBlueSum = blueSum;
                bluestColumn = i;
            }

        }

        //pushes the current image to the undoStack
        undoStack.push(convertToBufferedImage());
        //changes the column to blue
        changeColor(0, 0, fullColor, bluestColumn);
        createFile();
        return bluestColumn;
    }

    //removes the bluest column in the grid
    @Override
    protected void removeBluestColumn() throws RequestFailedException {
        int bluestColumn = getBluestColumnIndex();
        removeColumn(bluestColumn);
    }

    //removes a random column in the grid
    void removeRandomColumn() throws RequestFailedException {
        int height = grid[0].length;
        int randomIndex = random.nextInt(height);
        //pushes the current image to the undoStack
        undoStack.push(convertToBufferedImage());
        //changes the column to red
        changeColor(fullColor, 0, 0, randomIndex);
        createFile();
        removeColumn(randomIndex);
    }

    //helper function takes in an integer and removes that column index in the grid. Then shifts and resizes the grid
    void removeColumn(int column) throws RequestFailedException {
        int width = grid[0].length;
        int height = grid.length;
        if (width <= 1) {
            throw new RequestFailedException("Grid has one or fewer columns.");
        }
        // Shift pixels in columns after the bluest column to the left
        for (int i = 0; i < height; i++) {
            for (int j = column; j < width - 1; j++) {
                grid[i][j] = grid[i][j + 1];
            }
        }

        // Resize the grid to remove the last column
        int[][] newGrid = new int[height][width - 1];
        for (int i = 0; i < height; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, column);
            System.arraycopy(grid[i], column + 1, newGrid[i], column, width - column - 1);
        }
        grid = newGrid;

//        undoStack.push(grid);
        createFile();
    }

    //takes in int values of RGB values and columns, and changes the color of the column to that color
    private void changeColor(int red, int green, int blue, int column) {
        int height = grid[0].length;
        for (int i = 0; i < height; i++) {
            grid[i][column] = new Color(red, green, blue).getRGB();
        }
    }

    //creates a .png file of the changes being made to the image
    private void createFile() {
        try {
            // Create a BufferedImage

            BufferedImage storeImage = convertToBufferedImage();

            // Define a prefix and suffix for the temporary file name
            String prefix = "temp-file" + versionNumber;
            String suffix = ".png";
            versionNumber++;

            // Create a temporary file
            File tempFile = new File("/Users/oliviapivovar/Desktop/CS2510/project-1-oliviaproject/tmp/"
                    + prefix + suffix);

            // Write the BufferedImage to the temporary file
            ImageIO.write(storeImage, "png", tempFile);

            tempFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method that undos the image to the previous version
    void undo() throws RequestFailedException {
        if (undoStack.isEmpty()) {
            throw new RequestFailedException("There is nothing to undo.");
        }
        BufferedImage undoImage = undoStack.pop();
        initializeGrid(undoImage);
    }
}
