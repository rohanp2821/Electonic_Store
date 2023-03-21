package com.electonic.store.ElectonicStore.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
@ToString
public class Category {

    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "category_title", length = 100)
    private String title;
    @Column(name = "category_description", length = 600)
    private String description;
    private String coverImage;

}
