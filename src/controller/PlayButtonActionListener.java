package controller;

import controller.PlayButtonActionEvent;

import java.util.EventListener;

public interface PlayButtonActionListener extends EventListener {
    void playButtonActionPerformed(PlayButtonActionEvent event);
}
