package org.jhotdraw.undo;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

public class RedoAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(RedoAction.class.getName());
    private final UndoRedoManager manager;

    public RedoAction(UndoRedoManager manager) {
        this.manager = manager;
        UndoRedoManager.getLabels().configureAction(this, "edit.redo");
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            manager.redo();
        } catch (Exception e) {
            LOGGER.info("Cannot redo: " + e);
            e.printStackTrace();
        }
    }
}
