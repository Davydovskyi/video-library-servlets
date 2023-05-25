package edu.jcourse.service.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.PersonDAO;
import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.dto.ReceivePersonDTO;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.CreatePersonMapper;
import edu.jcourse.mapper.impl.PersonMapper;
import edu.jcourse.service.PersonService;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.ValidatorProvider;
import edu.jcourse.validator.impl.CreatePersonValidator;

import java.util.List;
import java.util.Optional;

public class PersonServiceImpl implements PersonService {

    private final PersonDAO personDAO = DAOProvider.getInstance().getPersonDAO();
    private final CreatePersonValidator createPersonValidator = ValidatorProvider.getInstance().getCreatePersonValidator();
    private final CreatePersonMapper createPersonMapper = MapperProvider.getInstance().getCreatePersonMapper();
    private final PersonMapper personMapper = MapperProvider.getInstance().getPersonMapper();

    @Override
    public Person create(CreatePersonDTO createPersonDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createPersonValidator.isValid(createPersonDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        Person person = createPersonMapper.mapFrom(createPersonDTO);
        try {
            return personDAO.save(person);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ReceivePersonDTO> findAll() throws ServiceException {
        try {
            return personDAO.findAll().stream()
                    .map(personMapper::mapFrom)
                    .toList();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<ReceivePersonDTO> findById(Long id) throws ServiceException {
        try {
            Optional<Person> person = personDAO.findById(id);
            return person.map(personMapper::mapFrom);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}