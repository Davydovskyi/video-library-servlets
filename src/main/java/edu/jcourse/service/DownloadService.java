package edu.jcourse.service;

import edu.jcourse.dto.ReceiveMovieDto;

import java.io.InputStream;
import java.util.List;

public interface DownloadService {

    InputStream get(List<ReceiveMovieDto> movies);

}