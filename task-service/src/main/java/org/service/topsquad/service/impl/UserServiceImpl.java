package org.service.topsquad.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.Account;
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
    public List<UserEntity> findAllUser() {
        return repository.findAllByRole("USER");
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
    public boolean changePassword(final Account model) throws ResourceNotFoundException, ResourceValidateException {
        if(StringUtils.isBlank(model.getPassword()) || StringUtils.isBlank(model.getNewPassword()))
            throw new ResourceValidateException("Input validation: Passwords cannot be blanked.");
        if(StringUtils.equals(model.getPassword(), model.getNewPassword()))
            throw new ResourceValidateException("Input validation: Passwords should be different to the one other.");

        UserEntity updateUser = repository.findByUserName(model.getUserName())
            .orElseThrow(() -> new ResourceNotFoundException("Resource validation: User name: " + model.getUserName() + " not found"));

        String currentPw = updateUser.getPassword();
        String newEncodedPw = new Base64().encodeAsString(model.getNewPassword().getBytes());

        if(StringUtils.equals(currentPw, newEncodedPw))
            throw new ResourceValidateException("Resource validation: Passwords should be different to the one other.");

        updateUser.setPassword(newEncodedPw);
        updateUser.setLastModified(new Date());

        repository.save(updateUser);
        return repository.isPasswordMatch(newEncodedPw);
    }

    @Override
    public boolean updateUserStatus(final String userName, final String status) throws ResourceNotFoundException {
        UserEntity user = repository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User: " + userName + " not found"));
        user.setStatus(status);
        repository.save(user);
        return repository.isUserExistsByStatus(userName, "DEACTIVATE");
    }
}
