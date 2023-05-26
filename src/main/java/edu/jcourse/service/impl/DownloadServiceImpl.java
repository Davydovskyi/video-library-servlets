package edu.jcourse.service.impl;

import edu.jcourse.dto.ReceiveMovieDTO;
import edu.jcourse.service.DownloadService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadServiceImpl implements DownloadService {
    @Override
    public InputStream get(List<ReceiveMovieDTO> movies) {
        byte[] bytes = movies.stream()
                .map(ReceiveMovieDTO::movieData)
                .collect(Collectors.joining("\n"))
                .getBytes();
        return new ByteArrayInputStream(bytes);
    }
}