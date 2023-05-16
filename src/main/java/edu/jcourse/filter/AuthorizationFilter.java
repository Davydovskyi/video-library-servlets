package edu.jcourse.filter;

import edu.jcourse.dto.ReceiveUserDTO;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter(urlPatterns = "/*", dispatcherTypes = DispatcherType.INCLUDE)
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(UrlPath.REGISTRATION, UrlPath.LOGIN, UrlPath.LOCALE, UrlPath.IMAGES);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();

        if (isUserLoggedIn(servletRequest) || isPublicPath(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String previousPage = ((HttpServletRequest) servletRequest).getHeader("referer");
            ((HttpServletResponse) servletResponse).sendRedirect(previousPage != null ? previousPage : UrlPath.LOGIN);
        }
    }

    private boolean isUserLoggedIn(ServletRequest servletRequest) {
        ReceiveUserDTO user = (ReceiveUserDTO) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null;
    }

    private boolean isPublicPath(String requestURI) {
        return PUBLIC_PATHS.stream()
                .anyMatch(requestURI::startsWith);
    }
}