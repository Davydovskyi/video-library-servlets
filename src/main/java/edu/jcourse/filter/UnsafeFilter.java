package edu.jcourse.filter;

import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Role;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/admin")
public class UnsafeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ReceiveUserDto user = (ReceiveUserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        if (user == null) {
            ((HttpServletResponse) servletResponse).sendRedirect(UrlPath.LOGIN);
        } else if (user.role() == Role.USER) {
            String previousPage = ((HttpServletResponse) servletResponse).getHeader("referer");
            ((HttpServletResponse) servletResponse).sendRedirect(previousPage != null ? previousPage : UrlPath.MOVIES);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
