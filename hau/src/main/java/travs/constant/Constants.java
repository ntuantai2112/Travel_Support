package travs.constant;

public class Constants {

    private Constants() {
    }

    public static final String ENCODING_UTF8 = "UTF-8";

    public class UrlPath {

        private UrlPath() {
        }
        public static final String URL_API_LOGIN = "/login";
        public static final String URL_API_PROFILE = "/member/account/profile";
        public static final String URL_API_FORWARD_PASSWORD= "/forward-password";
        public static final String URL_API_Reset_PassWord= "/reset-password";
        public static final String URL_API_Change_PassWord= "/member/change-password";
    }

    public class FileProperties {
        private FileProperties() {
        }

        public static final String PROPERTIES_APPLICATION = "application.properties";
        public static final String PROPERTIES_VALIDATION = "validation.properties";
    }

    public class AccountGender {
        private AccountGender() {
        }

        public static final String GENDER_MALE = "nam";
        public static final String GENDER_FEMALE = "nữ";
    }

}
