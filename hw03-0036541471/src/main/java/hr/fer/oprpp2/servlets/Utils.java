package hr.fer.oprpp2.servlets;

public class Utils {
    public static boolean isParamInvalid(String param) {
        return param == null || param.trim().isBlank();
    }

    public static Integer parseParamToInteger(String param, int defaultValue) {
        if (isParamInvalid(param)) {
            return Integer.parseInt(param);
        } else {
            return defaultValue;
        }
    }
}
