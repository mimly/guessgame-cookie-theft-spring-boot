package mimly.guessgame.config;

import inet.ipaddr.IPAddressString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class WebConfig {

    @Bean
    public Map<String, IPAddressString> sessionIpMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
        commonsRequestLoggingFilter.setIncludeClientInfo(true);
        commonsRequestLoggingFilter.setIncludeQueryString(true);
        commonsRequestLoggingFilter.setIncludePayload(true);
        commonsRequestLoggingFilter.setMaxPayloadLength(256);
        commonsRequestLoggingFilter.setIncludeHeaders(false);
        return commonsRequestLoggingFilter;
    }
}
