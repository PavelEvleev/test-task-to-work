package com.example.demo.entity;


/**
 * Class for change user status
 * */
public class StatusCommand {

    private String uuid;

    private boolean status;

    private boolean lastStatus;

    public StatusCommand(User user, boolean lastStatus) {
        this.lastStatus = lastStatus;
        this.status = user.isOnline();
        this.uuid = user.getUuid();
    }

    public StatusCommand() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public boolean isLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(boolean lastStatus) {
        this.lastStatus = lastStatus;
    }
}
