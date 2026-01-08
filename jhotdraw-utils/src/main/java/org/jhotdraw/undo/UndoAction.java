package org.jhotdraw.undo;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

public class UndoAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(UndoAction.class.getName());
    private final UndoRedoManager manager;

    public UndoAction(UndoRedoManager manager) {
        this.manager = manager;
        UndoRedoManager.getLabels().configureAction(this, "edit.undo");
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            manager.undo();
        } catch (Exception e) {
            LOGGER.info("Cannot undo: " + e);
            e.printStackTrace();
        }
    }
}
