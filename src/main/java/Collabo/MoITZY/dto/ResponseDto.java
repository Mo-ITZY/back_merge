package Collabo.MoITZY.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor(staticName = "of")
public class ResponseDto<D> {

    private HttpStatus status;
    private String message;
    private D data;

    public static <D> ResponseDto<D> ok(HttpStatus status, String message) {
        return ResponseDto.of(status, message, null);
    }

    public static <D> ResponseDto<D> error(HttpStatus status, String message) {
        return ResponseDto.of(status, message, null);
    }

    public static <D> ResponseDto<D> ok(HttpStatus status,String message, D data) {
        return ResponseDto.of(status, message, data);
    }

    public static <D> ResponseDto<D> error(HttpStatus status,String message, D data) {
        return ResponseDto.of(status, message, data);
    }
}