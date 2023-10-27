package org.service.topsquad.service;

import java.util.List;
import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.UserEntity;
import org.service.topsquad.model.UserModel;

public interface UserService {

    List<UserEntity> findAll();
    UserEntity findByUserName(String userName) throws ResourceNotFoundException ;
    UserEntity create(UserModel model) throws ResourceValidateException;
    UserEntity update(UserModel model) throws ResourceNotFoundException;
    boolean deactivateByUserName(String userName) throws ResourceNotFoundException;
}
