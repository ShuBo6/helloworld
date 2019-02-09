package com.example.a81418.myapp1.DataManager;

public class username extends Thread {
    private String Username;

    public username(String username) {
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    @Override
    public void run() {
        super.run();
    }
}
