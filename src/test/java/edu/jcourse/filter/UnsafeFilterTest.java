package edu.jcourse.filter;

import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Role;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnsafeFilterTest {

    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpSession session;
    @Spy
    private UnsafeFilter unsafeFilter;

    @BeforeEach
    void setUp() {
        doReturn(session).when(servletRequest).getSession();
    }

    @SneakyThrows
    @Test
    void doFilterWhenUserIsNull() {
        doReturn(null).when(session).getAttribute("user");

        unsafeFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(servletResponse).sendRedirect(UrlPath.LOGIN);
        verify(servletRequest, never()).getHeader("referer");
        verifyNoInteractions(filterChain);
    }

    @SneakyThrows
    @Test
    void doFilterWhenUserRoleIsUserAndRefererIsNull() {
        doReturn(buildReceiveUserDto(Role.USER)).when(session).getAttribute("user");
        doReturn(null).when(servletRequest).getHeader("referer");

        unsafeFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(servletResponse).sendRedirect(UrlPath.MOVIES);
        verify(servletRequest).getHeader("referer");
        verifyNoInteractions(filterChain);
    }

    @SneakyThrows
    @Test
    void doFilterWhenUserRoleIsUserAndRefererIsPresent() {
        doReturn(buildReceiveUserDto(Role.USER)).when(session).getAttribute("user");
        doReturn(UrlPath.MOVIE).when(servletRequest).getHeader("referer");

        unsafeFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(servletResponse).sendRedirect(UrlPath.MOVIE);
        verify(servletRequest).getHeader("referer");
        verifyNoInteractions(filterChain);
    }

    @SneakyThrows
    @Test
    void doFilterWhenUserRoleIsAdmin() {
        doReturn(buildReceiveUserDto(Role.ADMIN)).when(session).getAttribute("user");

        unsafeFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(servletResponse, never()).sendRedirect(any());
        verify(servletRequest, never()).getHeader("referer");
        verify(filterChain).doFilter(servletRequest, servletResponse);
    }

    private ReceiveUserDto buildReceiveUserDto(Role role) {
        return ReceiveUserDto.builder()
                .role(role)
                .build();
    }
}