package com.diego.login.services.tienda;

import com.diego.login.dto.productosReponse.ListaDeseoReponse;
import com.diego.login.dto.productos.SaveListaDeseos;
import com.diego.login.dto.productosReponse.ListaDeseoResponseGet;
import com.diego.login.persistence.entity.ListaDeseosProd;
import com.diego.login.persistence.entity.Productos;
import com.diego.login.persistence.entity.Usuario;
import com.diego.login.persistence.interfaces.IListaDeseosProd;
import com.diego.login.persistence.repository.ListaDeseosProdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListaDeseosProdService implements IListaDeseosProd {

    @Autowired
    private ListaDeseosProdRepo listaDeseosProdRepo;

    @Override
    public Page<ListaDeseoResponseGet> getListaDeseosProd(Pageable pageable, Long userId) {
        Page<ListaDeseosProd> listaDeseosProd = listaDeseosProdRepo.findByUsuarioId(userId,pageable);
        //OBTENER LISTADO SI EN EL USUARIO COMO RESPUESTA, PARA BRINDAR MAYOR SEGURIDAD A LA API.
        List<ListaDeseoResponseGet> listaDeseoReponse = listaDeseosProd.getContent().stream()
                .map(listaDeseos -> new ListaDeseoResponseGet(
                        listaDeseos.getIdDeseo(),
                        listaDeseos.getProductos()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(listaDeseoReponse,pageable,listaDeseosProd.getTotalElements());
    }

    @Override
    public ListaDeseoReponse guardarListaDeseosProd(SaveListaDeseos listaDeseos) {

        ListaDeseosProd listaDeseosProd = new ListaDeseosProd();

        //OBJETO USUARIO
        Usuario usuario = new Usuario();
        usuario.setId(listaDeseos.getId_User());

        //OBJETO PRODUCTO
        Productos productos = new Productos();
        productos.setId_producto(listaDeseos.getId_Producto());

        listaDeseosProd.setUsuario(usuario);
        listaDeseosProd.setProductos(productos);

        ListaDeseosProd saveEntity = listaDeseosProdRepo.save(listaDeseosProd);

        return convertirDTO(saveEntity);
    }

    private ListaDeseoReponse convertirDTO(ListaDeseosProd listaDeseosProd) {
        ListaDeseoReponse response = new ListaDeseoReponse();
        response.setId_Deseo(listaDeseosProd.getIdDeseo());
        response.setId_Usuario(listaDeseosProd.getUsuario().getId());
        response.setId_Producto(listaDeseosProd.getProductos().getId_producto());

        return response;
    }

    @Override
    public void eliminarListaDeseoProd(Long id) {
        listaDeseosProdRepo.deleteById(id);
    }
}
