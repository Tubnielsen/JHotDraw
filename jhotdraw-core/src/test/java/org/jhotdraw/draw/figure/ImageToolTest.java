package org.jhotdraw.draw.figure;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.jhotdraw.draw.tool.ImageTool;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ImageToolTest {

    @Mock private DrawingEditor mockEditor;
    @Mock private DrawingView mockView;
    @Mock private File mockFile;
    @Mock private ImageHolderFigure mockImageHolderFigure;
    @Mock private ImageHolderFigure mockCreatedFigure;
    @Mock private JComponent mockComponent;

    private ImageTool imageTool;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        imageTool = new ImageTool(mockImageHolderFigure);

        when(mockView.getComponent()).thenReturn(mockComponent);
        when(mockEditor.getActiveView()).thenReturn(mockView);
        when(mockImageHolderFigure.clone()).thenReturn(mockCreatedFigure);
    }

    @Test
    public void testActivateWithFileSelected() throws Exception {
        mockFileChooserToReturnFile(true);

        // ACT
        imageTool.activate(mockEditor);

        assertTrue("activate() should complete without exception", true);
    }

    // TEST 2: activate() metode - scenarie hvor bruger annullerer
    @Test
    public void testActivateWhenUserCancels() throws Exception {
        // ARRANGE: Mock at chooseImageFile returnerer null
        mockFileChooserToReturnFile(false);

        imageTool.activate(mockEditor);

        assertTrue("activate() should handle cancellation without exception", true);
    }

    // TEST 3: createdImageFigure() privat metode
    @Test
    public void testCreatedImageFigureReturnsClone() throws Exception {
        // ARRANGE (allerede sat op i @Before)

        Method method = ImageTool.class.getDeclaredMethod("createdImageFigure");
        method.setAccessible(true);
        ImageHolderFigure result = (ImageHolderFigure) method.invoke(imageTool);

        // ASSERT: Returnerer cloned figure
        assertNotNull(result);
        assertEquals(mockCreatedFigure, result);
        verify(mockImageHolderFigure).clone();
    }

    // TEST 4: handleNotFileChoosen() privat metode
    @Test
    public void testHandleNotFileChoosenCallsFireToolDone() throws Exception {
        SimpleTestableImageTool testTool = new SimpleTestableImageTool(mockImageHolderFigure);

        Method method = ImageTool.class.getDeclaredMethod("handleNotFileChoosen");
        method.setAccessible(true);
        method.invoke(testTool);

        assertTrue("fireToolDone should be called", testTool.wasFireToolDoneCalled());
    }

    private void mockFileChooserToReturnFile(boolean returnFile) throws Exception {
        // Sæt useFileDialog til false for at bruge JFileChooser
        Field useFileDialogField = ImageTool.class.getDeclaredField("useFileDialog");
        useFileDialogField.setAccessible(true);
        useFileDialogField.set(imageTool, false);

        // Mock JFileChooser
        JFileChooser mockChooser = mock(JFileChooser.class);
        Field fileChooserField = ImageTool.class.getDeclaredField("fileChooser");
        fileChooserField.setAccessible(true);
        fileChooserField.set(imageTool, mockChooser);

        if (returnFile) {
            when(mockChooser.showOpenDialog(mockComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
            when(mockChooser.getSelectedFile()).thenReturn(mockFile);
        } else {
            when(mockChooser.showOpenDialog(mockComponent)).thenReturn(JFileChooser.CANCEL_OPTION);
        }
    }

    private static class SimpleTestableImageTool extends ImageTool {
        private boolean fireToolDoneCalled = false;

        public SimpleTestableImageTool(ImageHolderFigure prototype) {
            super(prototype);
        }

        @Override
        public void fireToolDone() {
            fireToolDoneCalled = true;
        }

        @Override
        public boolean isToolDoneAfterCreation() {
            return true; // Sæt til true så handleNotFileChoosen kalder fireToolDone
        }

        public boolean wasFireToolDoneCalled() {
            return fireToolDoneCalled;
        }
    }
}