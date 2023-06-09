package edu.jcourse.service;

import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Person create(CreatePersonDto createPersonDTO) throws ServiceException, ValidationException;

    List<ReceivePersonDto> findAll() throws ServiceException;

    Optional<ReceivePersonDto> findById(Long id) throws ServiceException;
}
