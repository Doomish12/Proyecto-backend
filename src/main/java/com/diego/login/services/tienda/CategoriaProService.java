package com.diego.login.services.tienda;

import com.diego.login.dto.CategoriaProdCount;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.CategoriaProd;
import com.diego.login.persistence.interfaces.ICategoriaProd;
import com.diego.login.persistence.repository.CategoriaRepo;
import com.diego.login.persistence.util.EstadoCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProService implements ICategoriaProd {

    @Autowired
    private CategoriaRepo categoriaRepo;

    //LISTAR CANTIDAD DE PRODUCTOS QUE TIENE CADA CATEGORIA.
    public List<CategoriaProdCount> getCategoriaProductoCounts() {
        return categoriaRepo.findCategoriaProductoCounts();
    }

    @Override
    public Page<CategoriaProd> getCategorias(Pageable pageable) {
        return  categoriaRepo.findAll(pageable);
    }

    @Override
    public Optional<CategoriaProd> obtenerCategoriaId(Long id_Categoria) {
        return categoriaRepo.findById(id_Categoria);
    }

    @Override
    public CategoriaProd guardarCategoria(CategoriaProd categoria) {
        CategoriaProd categoriResponse = new CategoriaProd();
        categoriResponse.setId(categoria.getId());
        categoriResponse.setNombre_categoria_prod(categoria.getNombre_categoria_prod());
        categoriResponse.setEstado_categoria(EstadoCategoria.NO_ACTIVO);
        return categoriaRepo.save(categoriResponse);
    }

    @Override
    public CategoriaProd actualizarCategoria(Long id_Categoria, CategoriaProd categoria) {

        CategoriaProd categoriaActual = categoriaRepo.findById(id_Categoria).
                orElseThrow(() -> new ObjectNotFoundException("Categoria no encontrada"));

        categoriaActual.setNombre_categoria_prod(categoria.getNombre_categoria_prod());

        return categoriaRepo.save(categoriaActual);
    }

    @Override
    public void eliminarCategoria(Long id_Categoria) {
        categoriaRepo.deleteById(id_Categoria);
    }
}
