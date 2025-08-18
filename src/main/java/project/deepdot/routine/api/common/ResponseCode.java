package project.deepdot.routine.api.common;

public final class ResponseCode {
    private ResponseCode() {}

    public static final String SUCCESS = "SU"; // 성공
    public static final String BAD_REQUEST = "BR"; // 잘못된 요청
    public static final String VALIDATION_ERROR = "VE"; // 유효성 실패
    public static final String UNAUTHORIZED = "UN"; // 인증 실패
    public static final String FORBIDDEN = "FO"; // 권한 없음
    public static final String NOT_FOUND = "NF"; // 리소스 없음
    public static final String CONFLICT = "CF"; // 충돌(중복 등)
    public static final String SERVER_ERROR = "SE"; // 서버 오류
}
