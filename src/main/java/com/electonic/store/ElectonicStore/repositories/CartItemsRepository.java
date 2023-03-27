package com.electonic.store.ElectonicStore.repositories;

import com.electonic.store.ElectonicStore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItem,Integer> {
}
