package com.electonic.store.ElectonicStore.repositories;

import com.electonic.store.ElectonicStore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByTitleContaining(String keyword);

}
