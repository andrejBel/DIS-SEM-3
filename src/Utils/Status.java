package Utils;

public class Status {

    private boolean status_;
    private String message_;

    public Status(boolean status, String message) {
        this.status_ = status;
        this.message_ = message;
    }

    public Status() {
        status_ = true;
        message_ = "";
    }

    public boolean isStatusOk() {
        return status_;
    }

    public String getMessage() {
        return message_;
    }
    public void setMessage(String message) {
        this.message_ = message;
    }

    public void setStatus_(boolean status) {
        this.status_ = status;
    }
}
