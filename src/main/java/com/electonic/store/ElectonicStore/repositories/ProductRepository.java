package com.electonic.store.ElectonicStore.repositories;

import com.electonic.store.ElectonicStore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByTitleContaining (Pageable pageable , String keyword);
    Page<Product> findByLiveTrue(Pageable pageable);

}
