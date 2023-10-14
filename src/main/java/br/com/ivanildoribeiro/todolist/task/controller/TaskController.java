package br.com.ivanildoribeiro.todolist.task.controller;

import br.com.ivanildoribeiro.todolist.task.model.TaskModel;
import br.com.ivanildoribeiro.todolist.task.repository.TaskRepository;
import br.com.ivanildoribeiro.todolist.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel model, HttpServletRequest request) {
        model.setUserId((UUID) request.getAttribute("idUser"));

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(model.getStartAt()) || currentDate.isAfter(model.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de criação ou Data de término não deve anteceder a data atual");
        }
        if (model.getEndAt().isBefore(model.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de inicio deve ser menor que a de término");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(model));
    }

    @GetMapping
    public List<TaskModel> getTasksByIdUser(HttpServletRequest request) {
        var id = (UUID) request.getAttribute("idUser");
        return repository.findAllByUserId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") UUID id, @RequestBody TaskModel model, HttpServletRequest request) {
        var task = this.repository.findById(id).orElse(null);

        if(task==null){
            return ResponseEntity.status(404).body("Tarefa não encontrada");
        }

        if (!model.getUserId().equals(request.getAttribute("idUser"))) {
            return ResponseEntity.status(401).body("Usuário sem permissão para alterar a tarefa");
        }

        Utils.copyNonNullProperties(model, task);

        return ResponseEntity.status(204).body(this.repository.save(task));
    }
}
