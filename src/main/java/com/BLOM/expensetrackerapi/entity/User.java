package com.BLOM.expensetrackerapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private String name;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private boolean verified = false;

   @JsonIgnore
   private String verificationCode;

   @JsonIgnore
   private Timestamp verificationCodeExpiresAt;

    private Long age;

    @Column(name ="created_at", nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name ="updated_at", nullable = false,updatable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;


}
