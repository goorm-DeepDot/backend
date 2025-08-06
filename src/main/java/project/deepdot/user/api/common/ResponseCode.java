package project.deepdot.user.api.common;

public interface ResponseCode {

    String SUCCESS="SU";

    String VALIDATION_FAIL="VF";
    String DUPLICATE_ID="DI";

    String SIGN_IN_FAIL="SF";
    String CERTIFICATION_FAIL="CF";

    String MAIL_FAIL="MF";
    String DATABASE_ERROR="DBE";

    String MAIL_NOT_VERIFIED="EN";
    String MAIL_NOT_FOUND="ENF";

    String MAIL_USER_NOT_EQUAL="MUNE";
    String NEW_OLD_PASSWORD_SAME="NOPS";

    String ROUTINE_DELETE_FAIL="RDF";


}
