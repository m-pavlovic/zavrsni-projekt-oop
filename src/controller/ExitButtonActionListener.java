package controller;

import java.util.EventListener;

//prosiruje EventListener
public interface ExitButtonActionListener extends EventListener {

    void exitButtonActionPerformed(ExitButtonActionEvent event);
}
