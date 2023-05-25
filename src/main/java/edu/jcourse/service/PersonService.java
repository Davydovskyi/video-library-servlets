package edu.jcourse.service;

import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.dto.ReceivePersonDTO;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Person create(CreatePersonDTO createPersonDTO) throws ServiceException, ValidationException;

    List<ReceivePersonDTO> findAll() throws ServiceException;

    Optional<ReceivePersonDTO> findById(Long id) throws ServiceException;
}
