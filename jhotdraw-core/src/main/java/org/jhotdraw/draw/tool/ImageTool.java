package org.jhotdraw.draw.tool;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.ImageHolderFigure;

public class ImageTool extends CreationTool {

    private static final long serialVersionUID = 1L;
    protected FileDialog fileDialog;
    protected JFileChooser fileChooser;
    protected boolean useFileDialog;

    public ImageTool(ImageHolderFigure prototype) {
        super(prototype);
    }

    public ImageTool(ImageHolderFigure prototype, Map<AttributeKey<?>, Object> attributes) {
        super(prototype, attributes);
    }

    public void setUseFileDialog(boolean newValue) {
        useFileDialog = newValue;
        if (useFileDialog) {
            fileChooser = null;
        } else {
            fileDialog = null;
        }
    }

    public boolean isUseFileDialog() {
        return useFileDialog;
    }

    @Override
    public void activate(DrawingEditor editor) {
        super.activate(editor);
        final DrawingView v = getView();
        if (v == null) {
            return;
        }

        // 1. Select the image file
        File file = chooseImageFile(v);

        // Assertion checks that file is not null when selected
        assert file != null : "File selection should not be null!"; // Ensure file is selected

        if (file != null) {
            // 2. Create a new ImageHolderFigure
            ImageHolderFigure imageHolderFigure = createdImageFigure();

            // 3. Load the image asynchronously
            loadImageAsync(imageHolderFigure, file, v);
        } else {
            // 4. Handle the case when no file is selected
            handleNotFileChoosen();
        }
    }

    // Method to create a new ImageHolderFigure based on the prototype
    private ImageHolderFigure createdImageFigure() {
        ImageHolderFigure figure = (ImageHolderFigure) prototype.clone();

        // Assertion checks that figure is not null after creation
        assert figure != null : "Created ImageHolderFigure should not be null!"; // Ensures figure creation

        return figure;
    }

    // Method to handle the case where no file was chosen
    private void handleNotFileChoosen() {
        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    // Asynchronously load the image
    private void loadImageAsync(ImageHolderFigure loaderFigure, File file, DrawingView v) {
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                loaderFigure.loadImage(file); // Load the image asynchronously
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();  // Will throw an ExecutionException if something went wrong in doInBackground

                    // Assertion checks that image data is loaded successfully
                    assert loaderFigure.getImageData() != null : "Image data should not be null after loading!"; // Ensure image data is loaded

                    // Assertion checks that BufferedImage is loaded successfully
                    assert loaderFigure.getBufferedImage() != null : "BufferedImage should not be null after loading!"; // Ensure BufferedImage is loaded

                    if (createdFigure != null) {
                        ((ImageHolderFigure) createdFigure).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                    } else {
                        ((ImageHolderFigure) prototype).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                    }
                } catch (IOException ex) {
                    handleErrors(ex, v);  // Handle the exception
                } catch (InterruptedException | ExecutionException ex) {
                    handleErrors(ex, v);  // Handle the exception
                    getDrawing().remove(createdFigure); // Remove the figure in case of error
                    fireToolDone(); // Finish the tool's operation
                }
            }
        }.execute();
    }

    // Handling errors for the ImageTool
    private void handleErrors(Exception ex, DrawingView v) {
        JOptionPane.showMessageDialog(v.getComponent(),
                ex.getMessage(),
                null,
                JOptionPane.ERROR_MESSAGE);
    }

    // Method for selecting the image file
    private File chooseImageFile(DrawingView v) {
        final File file;
        if (useFileDialog) {
            getFileDialog().setVisible(true);
            if (getFileDialog().getFile() != null) {
                file = new File(getFileDialog().getDirectory(), getFileDialog().getFile());
            } else {
                file = null;
            }
        } else {
            if (getFileChooser().showOpenDialog(v.getComponent()) == JFileChooser.APPROVE_OPTION) {
                file = getFileChooser().getSelectedFile();
            } else {
                file = null;
            }
        }
        return file;
    }

    // Returns JFileChooser instance if not already initialized
    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
        }
        return fileChooser;
    }

    // Returns FileDialog instance if not already initialized
    private FileDialog getFileDialog() {
        if (fileDialog == null) {
            fileDialog = new FileDialog(new Frame());
        }
        return fileDialog;
    }
}


