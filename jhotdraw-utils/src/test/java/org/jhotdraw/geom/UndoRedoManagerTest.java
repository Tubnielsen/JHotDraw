package org.jhotdraw.geom;

import org.jhotdraw.undo.UndoRedoManager;
import org.junit.*;

import javax.swing.*;
import javax.swing.undo.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UndoRedoManagerTest {

    private UndoRedoManager manager;
    private UndoableEdit edit;

    @Before
    public void setUp() {
        manager = new UndoRedoManager();
        edit = mock(UndoableEdit.class);

        when(edit.isSignificant()).thenReturn(true);
        when(edit.canUndo()).thenReturn(true);
        when(edit.canRedo()).thenReturn(true);
        doNothing().when(edit).undo();
        doNothing().when(edit).redo();
    }

    // Best Case
    @Test
    public void testUndo() throws CannotUndoException {
        manager.addEdit(edit);

        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
        assertTrue(manager.getUndoAction().isEnabled());
        assertFalse(manager.getRedoAction().isEnabled());

        manager.undo();

        verify(edit).undo();
        assertFalse(manager.canUndo());
        assertTrue(manager.canRedo());
        assertFalse(manager.getUndoAction().isEnabled());
        assertTrue(manager.getRedoAction().isEnabled());
    }

    @Test
    public void testRedo() throws CannotUndoException {
        manager.addEdit(edit);
        manager.undo();

        assertTrue(manager.canRedo());
        assertFalse(manager.canUndo());
        assertTrue(manager.getRedoAction().isEnabled());
        assertFalse(manager.getUndoAction().isEnabled());

        manager.redo();

        verify(edit).redo();
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
        assertTrue(manager.getUndoAction().isEnabled());
        assertFalse(manager.getRedoAction().isEnabled());
    }

    @Test
    public void testUndoOrRedoAfterUndo() throws CannotUndoException, CannotRedoException {
        manager.addEdit(edit);

        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
        assertTrue(manager.getUndoAction().isEnabled());
        assertFalse(manager.getRedoAction().isEnabled());

        manager.undoOrRedo();

        verify(edit).undo();
        assertFalse(manager.canUndo());
        assertTrue(manager.canRedo());
        assertFalse(manager.getUndoAction().isEnabled());
        assertTrue(manager.getRedoAction().isEnabled());

    }

    @Test
    public void testUndoOrRedoAfterRedo() throws CannotUndoException, CannotRedoException {
        manager.addEdit(edit);
        manager.undo();

        assertTrue(manager.canRedo());
        assertFalse(manager.canUndo());
        assertTrue(manager.getRedoAction().isEnabled());
        assertFalse(manager.getUndoAction().isEnabled());

        manager.undoOrRedo();

        verify(edit).redo();
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
        assertTrue(manager.getUndoAction().isEnabled());
        assertFalse(manager.getRedoAction().isEnabled());
    }

    @Test
    public void testUpdateActionsAfterAddEdit() {
        manager.addEdit(edit);

        Action undo = manager.getUndoAction();
        Action redo = manager.getRedoAction();

        assertTrue(undo.isEnabled());
        assertFalse(redo.isEnabled());

        String undoLabel = (String) undo.getValue(Action.NAME);
        String redoLabel = (String) redo.getValue(Action.NAME);

        assertNotNull(undoLabel);
        assertNotNull(redoLabel);
        assertTrue(undoLabel.toLowerCase().contains("undo"));
        assertTrue(redoLabel.toLowerCase().contains("redo"));
    }

    // Boundary case
    @Test(expected = CannotUndoException.class)
    public void testUndoWithoutEdits() throws CannotUndoException {
        UndoRedoManager emptyManager = new UndoRedoManager();
        emptyManager.undo();
    }

    @Test(expected = CannotRedoException.class)
    public void testRedoWithoutEdits() throws CannotRedoException {
        UndoRedoManager emptyManager = new UndoRedoManager();
        emptyManager.redo();
    }

    @Test(expected = CannotUndoException.class)
    public void testUndoOrRedoOnEmptyManagerThrows() throws CannotUndoException, CannotRedoException {
        UndoRedoManager emptyManager = new UndoRedoManager();
        emptyManager.undoOrRedo(); // should throw because nothing to undo or redo
    }






}
