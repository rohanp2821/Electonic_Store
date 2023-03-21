package com.electonic.store.ElectonicStore.repositories;

import com.electonic.store.ElectonicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

    List<User> findByNameContaining(String keyword);



}
