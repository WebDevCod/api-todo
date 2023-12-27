package com.restapp.rest.controller;

import com.restapp.rest.dto.TaskDTO;
import com.restapp.rest.entity.Task;
import com.restapp.rest.myexception.MyException;
import com.restapp.rest.repository.TaskRepository;
import com.restapp.rest.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasks() {
        List<TaskDTO> tasksDTO = taskService.getTasksDTO();
        return new ResponseEntity<>(tasksDTO, HttpStatus.OK);
    }

    @PostMapping("/savetask")
    public ResponseEntity<?> saveTask(@Valid @RequestBody Task task) {

        validateTask(task);

        try {
            Task taskSaved = taskService.saveTask(task);
            return new ResponseEntity<>(taskSaved, HttpStatus.CREATED);
        } catch (MyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {

        validateTask(task);

        try {
            Task updatedTask = taskService.updateTask(id, task);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (MyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            Task deletedTask = taskService.deleteTask(id);
            return new ResponseEntity<>(deletedTask, HttpStatus.OK);
        } catch (MyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateTask(Task task) {
        if (task == null || task.getTitle().isEmpty() || task.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Task cannot be null and must have non-empty title and description");
        }
    }
}
