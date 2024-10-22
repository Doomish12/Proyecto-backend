package com.diego.login.persistence.entity;

import com.diego.login.persistence.util.EstadoOrdenProd;
import com.diego.login.persistence.util.EstadoProducto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_productos")
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @Column(nullable = false)
    private Double precio_pro;

    @Column(nullable = false)
    private String nombre_pro;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion_pro;

  //  @Column(nullable = false, length = 300)
  //  private String imagen_pro;

    // Lista de imágenes
    @ElementCollection
    @CollectionTable(name = "imagenes_producto", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "imagen_url", length = 300) // Restricción de longitud
    private List<String> imagen_pro; // Almacena las URLs de las imágenes

    @Column(nullable = false)
    private int stock_pro;

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado_pro;

    @ManyToOne
    @JoinColumn(name = "id_categoria_prod")
    private CategoriaProd categoriaProd;


}
