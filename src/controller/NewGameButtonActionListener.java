package controller;

import controller.NewGameButtonActionEvent;

import java.util.EventListener;

public interface NewGameButtonActionListener extends EventListener {
    void newGameButtonActionPerformed(NewGameButtonActionEvent event);
}
