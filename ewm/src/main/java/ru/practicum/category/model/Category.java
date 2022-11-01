package ru.practicum.category.model;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@Audited
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
