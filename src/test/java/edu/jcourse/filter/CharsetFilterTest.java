package edu.jcourse.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CharsetFilterTest {

    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;
    @Spy
    private CharsetFilter charsetFilter;

    @SneakyThrows
    @Test
    void doFilter() {

        charsetFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).setCharacterEncoding(StandardCharsets.UTF_8.name());
        verify(servletResponse).setCharacterEncoding(StandardCharsets.UTF_8.name());
        verify(filterChain).doFilter(servletRequest, servletResponse);
    }
}