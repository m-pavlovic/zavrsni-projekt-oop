package controller;

import controller.ExitButtonActionEvent;

import java.util.EventListener;

public interface ExitButtonActionListener extends EventListener {
    void exitButtonActionPerformed(ExitButtonActionEvent event);
}
