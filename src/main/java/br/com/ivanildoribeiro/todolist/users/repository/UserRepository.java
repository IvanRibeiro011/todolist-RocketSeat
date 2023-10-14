package br.com.ivanildoribeiro.todolist.users.repository;

import br.com.ivanildoribeiro.todolist.users.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByUsername(String username);
    UserModel findByUsername(String username);
}
