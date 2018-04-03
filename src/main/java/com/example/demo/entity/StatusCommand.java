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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusCommand command = (StatusCommand) o;

        if (status != command.status) return false;
        if (lastStatus != command.lastStatus) return false;
        return uuid != null ? uuid.equals(command.uuid) : command.uuid == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (status ? 1 : 0);
        result = 31 * result + (lastStatus ? 1 : 0);
        return result;
    }
}
