package org.service.topsquad.service;

import java.util.List;
import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.Account;
import org.service.topsquad.model.UserEntity;
import org.service.topsquad.model.UserModel;

public interface UserService {

    List<UserEntity> findAllUser();
    UserEntity findByUserName(String userName) throws ResourceNotFoundException ;
    UserEntity create(UserModel model) throws ResourceValidateException;
    boolean changePassword(Account model) throws ResourceNotFoundException, ResourceValidateException;
    boolean updateUserStatus(String userName, String status) throws ResourceNotFoundException;
}
