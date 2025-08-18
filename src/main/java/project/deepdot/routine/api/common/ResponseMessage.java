package project.deepdot.routine.api.common;

public final class ResponseMessage {
    private ResponseMessage() {}

    public static final String SUCCESS = "success";
    public static final String BAD_REQUEST = "bad request";
    public static final String VALIDATION_ERROR = "validation error";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String FORBIDDEN = "forbidden";
    public static final String NOT_FOUND = "not found";
    public static final String CONFLICT = "conflict";
    public static final String SERVER_ERROR = "server error";
}
