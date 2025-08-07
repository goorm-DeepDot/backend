package project.deepdot.aa.signup.api.common;

public interface ResponseMessage {
    String SUCCESS="success";

    String VALIDATION_FAIL="Validation Failed";
    String DUPLICATE_ID="Duplicate id";

    String SIGN_IN_FAIL="Login information mismatch";
    String CERTIFICATION_FAIL="Certification failed";

    String MAIL_FAIL = "Mail send failed";
    String DATABASE_ERROR="Database error";

    String MAIL_NOT_VERIFIED="Mail not verified";
    String MAIL_NOT_FOUND="Mail not found";

    String MAIL_USER_NOT_EQUAL="Mail and username are not equal";
    String NEW_OLD_PASSWORD_SAME="New password and old passwrd are same";

    String ROUTINE_DELETE_FAIL="Routine delete failed";

}
