package com.example.administrator.app.bean;

public class Team {
    private String logoUrl;
    private String teamName;
    private String win_num;
    private String lose_num;
    private String rank;
    private String win_percentage;
    private String table_area;

    public Team(String logoUrl, String teamName, String win_num, String lose_num, String rank, String win_percentage,String table_area) {
        this.logoUrl = logoUrl;
        this.teamName = teamName;
        this.win_num = win_num;
        this.lose_num = lose_num;
        this.rank = rank;
        this.win_percentage = win_percentage;
        this.table_area = table_area;
    }

    public String getTable_area() {
        return table_area;
    }

    public void setTable_area(String table_area) {
        this.table_area = table_area;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getWin_num() {
        return win_num;
    }

    public void setWin_num(String win_num) {
        this.win_num = win_num;
    }

    public String getLose_num() {
        return lose_num;
    }

    public void setLose_num(String lose_num) {
        this.lose_num = lose_num;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getWin_percentage() {
        return win_percentage;
    }

    public void setWin_percentage(String win_percentage) {
        this.win_percentage = win_percentage;
    }
}
