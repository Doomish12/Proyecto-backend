package com.diego.login.persistence.entity;


import com.diego.login.persistence.util.EstadoCategoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_categoriaProd")
public class CategoriaProd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre de la categoría no puede ser nulo")
    @NotEmpty(message = "El nombre de la categoría no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String nombre_categoria_prod;

    @Enumerated(EnumType.STRING)
    private EstadoCategoria estado_categoria;
}
