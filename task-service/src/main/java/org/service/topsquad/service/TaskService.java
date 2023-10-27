package org.service.topsquad.service;

import java.util.List;
import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.TaskEntity;
import org.service.topsquad.model.TaskModel;

public interface TaskService {

    List<TaskEntity> findAll();
    TaskEntity findByTicketNumber(String ticketNumber) throws ResourceNotFoundException ;
    TaskEntity create(TaskModel model) throws ResourceValidateException;
    TaskEntity update(TaskModel model) throws ResourceNotFoundException;
    boolean deleteByTicketNumber(String ticketNumber) throws ResourceNotFoundException;
    boolean deleteAll();
    void assignTask(String ticketNumber, String userName) throws ResourceNotFoundException;
}
