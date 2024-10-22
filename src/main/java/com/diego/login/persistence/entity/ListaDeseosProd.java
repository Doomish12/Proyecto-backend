package com.diego.login.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_listaDeseosProd")
public class ListaDeseosProd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_Deseo")
    private Long idDeseo;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_Producto")
    private Productos productos;


}
