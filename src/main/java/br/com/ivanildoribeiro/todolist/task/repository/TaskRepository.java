package br.com.ivanildoribeiro.todolist.task.repository;

import br.com.ivanildoribeiro.todolist.task.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

    List<TaskModel> findAllByUserId(UUID idUser);
}
