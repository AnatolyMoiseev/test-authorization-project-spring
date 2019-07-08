package com.test.authorization.model;

import com.test.authorization.model.enums.Status;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDate created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDate updated;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

}
