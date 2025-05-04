package dev.rias.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

}