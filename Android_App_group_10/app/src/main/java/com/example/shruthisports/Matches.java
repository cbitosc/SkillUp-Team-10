package com.example.shruthisports;

public class Matches {
    private String sportName;
    private Long teamId1;
    private Long teamId2;
    private String matchDate;
    private String matchTitle;
    private String startTime;
    private String reportingTime;

    public Matches(String sportName, String matchDate, String matchTitle, String startTime, String reportingTime){
        this.sportName = sportName;
        this.matchDate = matchDate;
        this.matchTitle = matchTitle;
        this.startTime = startTime;
        this.reportingTime = reportingTime;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public Long getTeamId1() {
        return teamId1;
    }

    public void setTeamId1(Long teamId1) {
        this.teamId1 = teamId1;
    }

    public Long getTeamId2() {
        return teamId2;
    }

    public void setTeamId2(Long teamId2) {
        this.teamId2 = teamId2;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getReportingTime() {
        return reportingTime;
    }

    public void setReportingTime(String reportingTime) {
        this.reportingTime = reportingTime;
    }
}
