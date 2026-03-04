import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GeneratePassword {
    public static void main(String[] args) {
        String password = "12345678";
        System.out.println("原始密码: " + password);
        System.out.println("使用MD5加密: " + md5(password));
        System.out.println("使用SHA-256加密: " + sha256(password));
    }
    
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}