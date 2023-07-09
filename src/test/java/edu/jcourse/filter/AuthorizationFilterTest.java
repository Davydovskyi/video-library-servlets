package edu.jcourse.filter;

import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationFilterTest {

    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpSession session;
    @Spy
    private AuthorizationFilter authorizationFilter;

    static Stream<Arguments> getPublicPathArguments() {
        return Stream.of(
                Arguments.of(UrlPath.REGISTRATION),
                Arguments.of(UrlPath.LOGIN),
                Arguments.of(UrlPath.LOCALE),
                Arguments.of(UrlPath.IMAGES)
        );
    }

    @SneakyThrows
    @Test
    void doFilterWhenUserIsLoggedIn() {
        doReturn("/some-page").when(servletRequest).getRequestURI();
        doReturn(session).when(servletRequest).getSession();
        doReturn(ReceiveUserDto.builder().build()).when(session).getAttribute("user");

        authorizationFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getRequestURI();
        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(filterChain).doFilter(servletRequest, servletResponse);
        verifyNoInteractions(servletResponse);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("getPublicPathArguments")
    void doFilterWithPublicPath(String requestURI) {
        doReturn(requestURI).when(servletRequest).getRequestURI();
        doReturn(session).when(servletRequest).getSession();
        doReturn(null).when(session).getAttribute("user");

        authorizationFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getRequestURI();
        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(filterChain).doFilter(servletRequest, servletResponse);
        verifyNoInteractions(servletResponse);
    }

    @SneakyThrows
    @Test
    void doFilterWhenUserIsNotLoggedInAndPathIsNotPublicAndThereIsNotReferer() {
        doReturn("/some-page").when(servletRequest).getRequestURI();
        doReturn(session).when(servletRequest).getSession();
        doReturn(null).when(session).getAttribute("user");
        doReturn(null).when(servletRequest).getHeader("referer");

        authorizationFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getRequestURI();
        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(servletRequest).getHeader("referer");
        verify(servletResponse).sendRedirect(UrlPath.LOGIN);
        verifyNoInteractions(filterChain);
    }

    @SneakyThrows
    @Test
    void doFilterWhenUserIsNotLoggedInAndPathIsNotPublicAndThereIsReferer() {
        doReturn("/some-page").when(servletRequest).getRequestURI();
        doReturn(session).when(servletRequest).getSession();
        doReturn(null).when(session).getAttribute("user");
        doReturn(UrlPath.MOVIE).when(servletRequest).getHeader("referer");

        authorizationFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).getRequestURI();
        verify(servletRequest).getSession();
        verify(session).getAttribute("user");
        verify(servletRequest).getHeader("referer");
        verify(servletResponse).sendRedirect(UrlPath.MOVIE);
        verifyNoInteractions(filterChain);
    }
}