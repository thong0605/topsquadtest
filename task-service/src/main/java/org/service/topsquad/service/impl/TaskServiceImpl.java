package org.service.topsquad.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.TaskEntity;
import org.service.topsquad.model.UserEntity;
import org.service.topsquad.model.TaskModel;
import org.service.topsquad.repository.TaskRepository;
import org.service.topsquad.repository.UserRepository;
import org.service.topsquad.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TaskEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public TaskEntity findByTicketNumber(final String ticketNumber) throws ResourceNotFoundException {
        return repository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Task: " + ticketNumber + " not found"));
    }

    @Override
    public TaskEntity create(final TaskModel model) throws ResourceValidateException {
        if (repository.isTicketNumberExists(model.getTicketNumber())){
            throw new ResourceValidateException("Task " + model.getTicketNumber() + "already existed.");
        }
        TaskEntity entity = new TaskEntity();
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setTicketNumber(model.getTicketNumber());
        entity.setStatus("TO_DO");
        entity.setCreatedDate(new Date());

        return repository.save(entity);
    }

    @Override
    public TaskEntity update(final TaskModel model) throws ResourceNotFoundException {
        TaskEntity updateTask = repository.findByTicketNumber(model.getTicketNumber())
            .orElseThrow(() -> new ResourceNotFoundException("Task name: " + model.getTicketNumber() + " not found"));
        updateTask.setTicketNumber(model.getTicketNumber());
        if(StringUtils.isNotBlank(model.getTitle())) updateTask.setTitle(model.getTitle());
        if(StringUtils.isNotBlank(model.getDescription())) updateTask.setDescription(model.getDescription());
        if(StringUtils.isNotBlank(model.getStatus())) updateTask.setStatus(model.getStatus());
        updateTask.setLastModified(new Date());

        return repository.save(updateTask);
    }

    @Override
    public boolean deleteByTicketNumber(final String ticketNumber) throws ResourceNotFoundException {
        TaskEntity task = repository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Task: " + ticketNumber + " not found"));
        repository.delete(task);
        return !repository.isTicketNumberExists(ticketNumber);
    }

    @Override
    public boolean deleteAll() {
        repository.deleteAll();
        List<TaskEntity> tasks = repository.findAll();
        return tasks.isEmpty();
    }

    @Override
    public void assignTask(String ticketNumber, String userName) throws ResourceNotFoundException {
        TaskEntity taskEntity = repository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Task " + ticketNumber + " not found"));
        UserEntity userEntity = userRepository.findByUserName(userName)
            .orElseThrow(() -> new ResourceNotFoundException("User name" + userName + " not found"));
        taskEntity.setUser(userEntity);
        repository.save(taskEntity);
    }
}
