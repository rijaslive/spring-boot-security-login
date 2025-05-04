package dev.rias.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private int status;
    private Date timestamp;
    private String message;
    private String error;
    private String path;
}
