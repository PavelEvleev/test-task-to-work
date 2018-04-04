package com.example.demo.entity;


/**
 * Class for change user status
 * */
public class StatusCommand {

    private Long id;

    private boolean status;

    private boolean lastStatus;

    public StatusCommand(User user, boolean lastStatus) {
        this.lastStatus = lastStatus;
        this.status = user.isOnline();
        this.id = user.getId();
    }

    public StatusCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return id != null ? id.equals(command.id) : command.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status ? 1 : 0);
        result = 31 * result + (lastStatus ? 1 : 0);
        return result;
    }
}
