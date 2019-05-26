package com.example.administrator.app.bean;

import java.util.ArrayList;

public class GameDay {

    private String title; //比赛日期
    private ArrayList<Game> gameArrayList = new ArrayList<>();

    public GameDay(String title, ArrayList<Game> gameArrayList) {
        this.title = title;
        this.gameArrayList = gameArrayList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Game> getGameArrayList() {
        return gameArrayList;
    }

    public void setGameArrayList(ArrayList<Game> gameArrayList) {
        this.gameArrayList = gameArrayList;
    }
}
