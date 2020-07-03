package com.example.shruthisports;

public class TeamMember {

    Long memberId;
    String sportName;
    Long teamId;
    String teamName;
    String teamStatus;
    String memberName;
    String section;
    String branch;
    String gender;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public TeamMember(Long memberId, String sportName, Long teamId, String teamName,
                      String teamStatus, String memberName, String section, String branch, String gender) {
        this.memberId = memberId;
        this.sportName = sportName;
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamStatus = teamStatus;
        this.memberName = memberName;
        this.section = section;
        this.branch = branch;
        this.gender = gender;
    }
}
