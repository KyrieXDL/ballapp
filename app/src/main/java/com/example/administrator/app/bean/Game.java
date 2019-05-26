package com.example.administrator.app.bean;

public class Game {
    String player1;
    String player1logo;
    String player2;
    String player2logo;
    String time;
    int status;
    String score;
    String link1url;
    String link2url;

    boolean isShowMenu = false;

    public String getLink1url() {
        return link1url;
    }

    public void setLink1url(String link1url) {
        this.link1url = link1url;
    }

    public String getLink2url() {
        return link2url;
    }

    public void setLink2url(String link2url) {
        this.link2url = link2url;
    }

    public boolean isShowMenu() {
        return isShowMenu;
    }

    public void setShowMenu(boolean showMenu) {
        isShowMenu = showMenu;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer1LogoUrl() {
        return player1logo;
    }

    public void setPlayer1LogoUrl(String player1LogoUrl) {
        this.player1logo = player1LogoUrl;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer2LogoUrl() {
        return player2logo;
    }

    public void setPlayer2LogoUrl(String player2LogoUrl) {
        this.player2logo = player2LogoUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}