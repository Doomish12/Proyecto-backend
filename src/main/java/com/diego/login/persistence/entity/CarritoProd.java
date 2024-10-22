package com.diego.login.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_carritoProd")
public class CarritoProd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Productos productos;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "total_carrito")
    private Double total_carrito;

}
