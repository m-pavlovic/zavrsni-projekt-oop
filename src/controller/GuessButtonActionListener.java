package controller;

import controller.GuessButtonActionEvent;

import java.util.EventListener;

public interface GuessButtonActionListener extends EventListener {
    void guessButtonActionPerformed(GuessButtonActionEvent event);
}
