package com.diego.login.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_detalle_orden")
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_orden")
    @JsonBackReference
    private OrdenesProd ordenesProd;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Productos producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "total", nullable = false)
    private Double total;
}
