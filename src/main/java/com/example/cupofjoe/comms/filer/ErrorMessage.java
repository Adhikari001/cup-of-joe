package com.example.cupofjoe.comms.filer;
import com.example.cupofjoe.comms.helper.HelperUtil;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ErrorMessage {
    private String timestamp;
    private String message;
    private String requestUrl;
    private int status;
    private String error;

    public ErrorMessage(String message,String requestUrl, int status, String error) {
        this.timestamp = HelperUtil.getLocalDateTimeOfUTC().toString();
        this.message = message;
        this.requestUrl = requestUrl;
        this.status = status;
        this.error = error;
    }
}