package org.service.topsquad.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.UserEntity;
import org.service.topsquad.model.UserModel;
import org.service.topsquad.repository.UserRepository;
import org.service.topsquad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;


    @Override
    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public UserEntity findByUserName(final String userName) throws ResourceNotFoundException {
        return repository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User: " + userName + " not found"));
    }

    @Override
    public UserEntity create(final UserModel model) throws ResourceValidateException {
        if (repository.isUserExistsByUserName(model.getUserName())){
            throw new ResourceValidateException("User " + model.getUserName() + "already existed.");
        }
        UserEntity entity = new UserEntity();
        entity.setUserName(model.getUserName());
        entity.setPassword(model.getPassword());
        entity.setRole("USER");
        entity.setMailAddress(model.getMailAddress());
        entity.setStatus("ACTIVE");
        entity.setCreatedDate(new Date());

        return repository.save(entity);
    }

    @Override
    public UserEntity update(final UserModel model) throws ResourceNotFoundException {
        UserEntity updateUser = repository.findByUserName(model.getUserName())
            .orElseThrow(() -> new ResourceNotFoundException("User name: " + model.getUserName() + " not found"));

        if(StringUtils.isNotBlank(model.getPassword())) updateUser.setPassword(model.getPassword());
        updateUser.setLastModified(new Date());

        return repository.save(updateUser);
    }

    @Override
    public boolean deactivateByUserName(final String userName) throws ResourceNotFoundException {
        UserEntity user = repository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User: " + userName + " not found"));
        user.setStatus("DEACTIVATE");
        repository.save(user);
        return repository.isUserExistsByStatus(userName, "DEACTIVATE");
    }
}
