package com.electonic.store.ElectonicStore.repositories;

import com.electonic.store.ElectonicStore.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems, Integer> {


}
