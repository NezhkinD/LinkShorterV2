package app.Property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperty {

    @Value("${app.properties.shortLinkLen}")
    public int len;

    @Value("${app.properties.shortLinkAllowedChars}")
    public String chars;

    @Value("${app.properties.shortLinkExpiredInHours}")
    public int expired;

    @Value("${app.properties.shortLinkTransitionLimit}")
    public int limit;
}
