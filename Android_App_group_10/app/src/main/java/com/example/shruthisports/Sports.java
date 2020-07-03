package com.example.shruthisports;

public class Sports {
    private int sport_id;
    private String sport_name;
    private String sport_category;
    private int mini_team_size;
    private int max_team_size;
    private String gender;
    private String start_date;
    private String end_date;

    public Sports(int sport_id, String sport_name, String sport_category,
                  int mini_team_size, int max_team_size, String gender,
                  String start_date, String end_date) {
        this.sport_id = sport_id;
        this.sport_name = sport_name;
        this.sport_category = sport_category;
        this.mini_team_size = mini_team_size;
        this.max_team_size = max_team_size;
        this.gender = gender;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getSport_id() {
        return sport_id;
    }

    public void setSport_id(int sport_id) {
        this.sport_id = sport_id;
    }

    public String getSport_name() {
        return sport_name;
    }

    public void setSport_name(String sport_name) {
        this.sport_name = sport_name;
    }

    public String getSport_category() {
        return sport_category;
    }

    public void setSport_category(String sport_category) {
        this.sport_category = sport_category;
    }

    public int getMini_team_size() {
        return mini_team_size;
    }

    public void setMini_team_size(int mini_team_size) {
        this.mini_team_size = mini_team_size;
    }

    public int getMax_team_size() {
        return max_team_size;
    }

    public void setMax_team_size(int max_team_size) {
        this.max_team_size = max_team_size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
