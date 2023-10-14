package br.com.ivanildoribeiro.todolist.task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Entity(name = "tb_task")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    private UUID userId;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {
        if(title.length() > 50){
            throw new Exception("O Título deve conter no máximo 50 caracteres");
        }
        this.title = title;
    }
}
