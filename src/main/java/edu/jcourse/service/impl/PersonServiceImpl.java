package edu.jcourse.service.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.PersonDao;
import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.dto.ReceivePersonDto;
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

    private final PersonDao personDAO;
    private final CreatePersonValidator createPersonValidator;
    private final CreatePersonMapper createPersonMapper;
    private final PersonMapper personMapper;

    public PersonServiceImpl() {
        this(DaoProvider.getInstance().getPersonDao(),
                ValidatorProvider.getInstance().getCreatePersonValidator(),
                MapperProvider.getInstance().getCreatePersonMapper(),
                MapperProvider.getInstance().getPersonMapper());
    }

    public PersonServiceImpl(PersonDao personDAO, CreatePersonValidator createPersonValidator, CreatePersonMapper createPersonMapper, PersonMapper personMapper) {
        this.personDAO = personDAO;
        this.createPersonValidator = createPersonValidator;
        this.createPersonMapper = createPersonMapper;
        this.personMapper = personMapper;
    }

    @Override
    public Person create(CreatePersonDto createPersonDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createPersonValidator.validate(createPersonDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        Person person = createPersonMapper.mapFrom(createPersonDTO);
        try {
            return personDAO.save(person);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ReceivePersonDto> findAll() throws ServiceException {
        try {
            return personDAO.findAll().stream()
                    .map(personMapper::mapFrom)
                    .toList();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<ReceivePersonDto> findById(Long id) throws ServiceException {
        try {
            return personDAO.findById(id)
                    .map(personMapper::mapFrom);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}