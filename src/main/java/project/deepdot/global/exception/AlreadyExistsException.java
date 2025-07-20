package project.deepdot.global.exception;

public class AlreadyExistsException extends CustomException{
    public AlreadyExistsException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}