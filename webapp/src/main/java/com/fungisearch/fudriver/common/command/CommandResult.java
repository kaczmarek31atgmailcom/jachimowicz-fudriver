package com.fungisearch.fudriver.common.command;



/**
 * Created by marcin on 04.02.16.
 */
public class CommandResult {
    public String barcode;
    public Integer date;
    public Long entityId;
    public Status status;
    public String body;

    public CommandResult(Long entityId) {
        this.entityId = entityId;
        this.status = Status.OK;
    }

    public CommandResult(Status status, String body){
        this.status = status;
        this.body = body;
    }

    public static final CommandResult OK = new CommandResult(Status.OK);

    public CommandResult(Long entityId, Status status, String body){
        this.entityId = entityId;
        this.status = status;
        this.body = body;
    }

    public CommandResult(String barcode, Long entityId,Status status, String body){
        this.barcode = barcode;
        this.entityId = entityId;
        this.status = status;
        this.body = body;
    }

    public CommandResult(Long entityId,int date,Status status, String body){
        this.date = date;
        this.entityId = entityId;
        this.status = status;
        this.body = body;
    }



    public CommandResult(Status status) {
        this.status = status;
    }

    public CommandResult(ResponseEntity response) {
        if(response.status >= 200 && response.status < 300) {
            status = Status.OK;
        } else {
            status = Status.ERROR;
        }

        body = response.body;
    }

    public enum Status {
        OK, ERROR
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof CommandResult))
            return false;
        CommandResult other = (CommandResult)obj;
        if(status != other.status)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CommandResult [entityId=" + entityId + ", status=" + status + ", body=" + body + "]";
    }

}
