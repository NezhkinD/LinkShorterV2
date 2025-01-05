package app.Utils;

import app.Property.AppProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class LinkUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public String createShortLink(int len, String chars) {
        StringBuilder result = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int randomIndex = RANDOM.nextInt(chars.length());
            result.append(chars.charAt(randomIndex));
        }
        return result.toString();
    }
}
