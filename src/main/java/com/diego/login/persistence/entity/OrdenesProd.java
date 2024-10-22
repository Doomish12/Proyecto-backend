package com.diego.login.persistence.entity;

import com.diego.login.persistence.util.EstadoOrdenProd;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_ordenesProd")
public class OrdenesProd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Usuario usuario;

    @OneToMany(mappedBy = "ordenesProd", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"productos"})
    private List<DetalleOrden> productos;

    @Column(name = "total_orden", nullable = false)
    private Double total_orden;

    @Column(name = "fecha_orden")
    private LocalDateTime fecha_orden;

    @Enumerated(EnumType.STRING)
    private EstadoOrdenProd estadoOrdenProd;

    @Column(name = "fecha_expiración")
    private LocalDateTime fechaExpiracion;



}
