package main.java.view;

import main.java.utils.callbacks.MessageCallBack;

public abstract class View {

    public abstract void display(String message);

    public MessageCallBack getMessageCallBack() {
        return this::display;
    }
}
