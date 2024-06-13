package controller;

import java.util.EventObject;


//klasa koja prosiruje EventObject i predstavlja dogadaj koji se prosljeduje listeneru kada se pritisne exit gumb

public class ExitButtonActionEvent extends EventObject {


    //poziva konstruktor superklase EventObject
    public ExitButtonActionEvent(Object source) {
        super(source);
    }
}
