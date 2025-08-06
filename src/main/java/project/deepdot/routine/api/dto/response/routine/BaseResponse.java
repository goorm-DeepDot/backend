package project.deepdot.routine.api.dto.response.routine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>("SU", "success", data);
    }

    public static <T> BaseResponse<T> fail(String code, String message) {
        return new BaseResponse<>(code, message, null);
    }
}
