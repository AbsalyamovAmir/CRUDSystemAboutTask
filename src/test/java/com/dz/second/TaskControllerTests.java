package com.dz.second;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.dz.second.controllers.TaskController;
import com.dz.second.model.Task;
import com.dz.second.services.TaskService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX");

    @Test
    public void testGetAllTasks() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        Task task1 = new Task(1L, "Task 1", "Description 1", now, false);
        Task task2 = new Task(2L, "Task 2", "Description 2", now, true);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);
        
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Task 1"))
            .andExpect(jsonPath("$[0].description").value("Description 1"))
            .andExpect(jsonPath("$[0].dueDate").value(task1.getDueDate().format(formatter)))
            .andExpect(jsonPath("$[0].completed").value(false))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].title").value("Task 2"))
            .andExpect(jsonPath("$[1].description").value("Description 2"))
            .andExpect(jsonPath("$[1].dueDate").value(task2.getDueDate().format(formatter)))
            .andExpect(jsonPath("$[1].completed").value(true));

        verify(taskService, times(1)).getAllTasks();
        }

    @Test
    public void testGetTaskById() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        Task task = new Task(1L, "Task 1", "Description 1", now, false);

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.title").value("Task 1"))
               .andExpect(jsonPath("$.description").value("Description 1"))
               .andExpect(jsonPath("$.dueDate").value(task.getDueDate().format(formatter)))
               .andExpect(jsonPath("$.completed").value(false));

        verify(taskService, times(1)).getTaskById(1L);
    }
    @Test
    public void testCreateTask() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        Task task = new Task(1L, "Task 1", "Description 1", now, false);

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        String requestJson = "{\"title\":\"Task 1\",\"description\":\"Description 1\",\"dueDate\":\"" + now.format(formatter) + "\",\"completed\":false}";

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.dueDate").value(now.format(formatter)))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    public void testUpdateTask() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description", now, true);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        String requestJson = "{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"dueDate\":\"" + now.format(formatter) + "\",\"completed\":true}";

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.dueDate").value(now.format(formatter)))
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(eq(1L));
    }
    
}

