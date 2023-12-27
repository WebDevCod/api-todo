package com.restapp.rest.service;

import com.restapp.rest.dto.TaskDTO;
import com.restapp.rest.entity.Task;
import com.restapp.rest.myexception.MyException;
import com.restapp.rest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskDTO> getTasksDTO() {
        List<Task> tasks = (List<Task>) taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToTasksDTO)
                .collect(Collectors.toList());
    }

    private TaskDTO convertToTasksDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        return taskDTO;
    }

    @Transactional
    public Task saveTask(Task task) {
        try {
            return taskRepository.save(task);
        } catch (DataIntegrityViolationException e) {
            throw new MyException("Data integrity error while creating a new task", e);
        } catch (Exception e) {
            throw new MyException("Error while creating a new task", e);
        }
    }

    @Transactional
    public Task updateTask(Long id, Task task) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task updatedTask = optionalTask.get();
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            saveTask(updatedTask);
            return updatedTask;
        } else
            throw new MyException("Task not found with id: " + id, new NoSuchElementException());
    }

    @Transactional
    public Task deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task deletedTask = optionalTask.get();
            taskRepository.delete(deletedTask);
            return deletedTask;
        } else {
            throw new MyException("Task not found with id: " + id, new NoSuchElementException());
        }
    }
}
