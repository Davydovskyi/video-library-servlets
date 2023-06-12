package edu.jcourse.service.impl;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.service.DownloadService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadServiceImpl implements DownloadService {
    @Override
    public InputStream get(List<ReceiveMovieDto> movies) {
        if (movies == null) {
            return new ByteArrayInputStream(new byte[0]);
        }

        byte[] bytes = movies.stream()
                .map(ReceiveMovieDto::movieData)
                .collect(Collectors.joining("\n"))
                .getBytes();
        return new ByteArrayInputStream(bytes);
    }
}