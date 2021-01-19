package mimly.guessgame.controller;

import inet.ipaddr.IPAddressString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@Slf4j(topic = "** Cookie Theft Filter **")
public class CookieTheftFilter implements Filter {

    private final Map<String, IPAddressString> sessionIpMap;

    @Autowired
    public CookieTheftFilter(Map<String, IPAddressString> sessionIpMap) {
        this.sessionIpMap = sessionIpMap;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        IPAddressString remoteIp = new IPAddressString(httpServletRequest.getRemoteAddr());
        String sessionId = httpServletRequest.getRequestedSessionId();
        log.info(String.format("%s %s", remoteIp.toNormalizedString(), sessionId));
        if (sessionId != null) {
            this.sessionIpMap.putIfAbsent(sessionId, remoteIp);

            if (!this.sessionIpMap.get(sessionId).equals(remoteIp)) {
                log.info(String.format("%s cookie theft", sessionId));
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(403);
                httpServletResponse.setContentType("text/html; charset=UTF-8");
                httpServletResponse.getWriter().println(
                        String.format("<h4>SESSION COOKIE <span style=\"color: red\">%s</span> HAS BEEN <span style=\"color: red\">STOLEN</span></h4>", sessionId)
                );
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
