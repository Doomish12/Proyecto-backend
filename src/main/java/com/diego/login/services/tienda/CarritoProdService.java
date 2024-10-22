package com.diego.login.services.tienda;
import com.diego.login.dto.productos.SaveCarritoProd;
import com.diego.login.dto.productosReponse.CarritoProdResponse;
import com.diego.login.dto.productosReponse.CarritoProdResponseGet;
import com.diego.login.dto.productosReponse.CarritoResponseMensajes;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.CarritoProd;
import com.diego.login.persistence.entity.Productos;
import com.diego.login.persistence.entity.Usuario;
import com.diego.login.persistence.interfaces.ICarritoProd;
import com.diego.login.persistence.repository.CarritoProdRepo;
import com.diego.login.persistence.repository.ProductosRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoProdService implements ICarritoProd {

    @Autowired
    private CarritoProdRepo carritoProdRepo;

    @Autowired
    private ProductosRepo productosRepo;


    @Override
    public List<CarritoProd> listarCarrito() {
        return carritoProdRepo.findAll();
    }

    @Override
    public Page<CarritoProdResponseGet> getCarritoProds(Long id, Pageable pageable) {
        Page<CarritoProd> carritoProdsPage = carritoProdRepo.findByUsuarioId(id, pageable);
        //OBTENER LISTADO SI EN EL USUARIO COMO RESPUESTA, PARA BRINDAR MAYOR SEGURIDAD A LA API.
        List<CarritoProdResponseGet> carritoProdResponses = carritoProdsPage.getContent().stream()
                .map(carritoProd -> new CarritoProdResponseGet(
                        carritoProd.getId_carrito(),
                        carritoProd.getProductos(),
                        carritoProd.getCantidad(),
                        carritoProd.getTotal_carrito()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(carritoProdResponses, pageable, carritoProdsPage.getTotalElements());
    }

    @Override
    public void eliminarCarritoProd(Long id) {
        carritoProdRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCarritoProdIdUsuario(Long id) {
        carritoProdRepo.deleteByUsuarioId(id);
    }

    @Override
    public CarritoProdResponse guardar(SaveCarritoProd carritoProd) {

        List<CarritoProd> carritoLista = carritoProdRepo.findByUsuarioId(carritoProd.getId_Usuario());
        boolean productoCarrito = false;

        int cantidadMaxima = 0; // Variable para almacenar el stock máximo

        // Obtener el producto del stock
        Productos producto = productosRepo.findById(carritoProd.getId_Producto())
                .orElseThrow(() -> new ObjectNotFoundException("Producto no encontrado con ID: " + carritoProd.getId_Producto()));
        cantidadMaxima = producto.getStock_pro(); // Obtener el stock disponible

            for(CarritoProd item : carritoLista) {
                if(item.getProductos().getId_producto() == carritoProd.getId_Producto()) {
                    productoCarrito = true;

                    // Verificar si la cantidad que se quiere agregar excede el stock disponible
                    if (item.getCantidad() + carritoProd.getCantidad() > cantidadMaxima) {
                        carritoProd.setMensaje("No se puede agregar más productos. Stock limitado: " + cantidadMaxima);
                        return convertirDto(item, carritoProd.getMensaje());
                    }


                    item.setCantidad(item.getCantidad()  + carritoProd.getCantidad());
                    item.setTotal_carrito((item.getCantidad() * item.getProductos().getPrecio_pro()));
                    CarritoProd carritoActualizado = carritoProdRepo.save(item);
                    carritoProd.setMensaje("Cantidad producto aumentado + " + carritoProd.getCantidad() );
                    return convertirDto(carritoActualizado,carritoProd.getMensaje());
                }
            }


            if(!productoCarrito) {
                if (carritoProd.getCantidad() > cantidadMaxima) {
                    carritoProd.setMensaje("No se puede agregar más productos. Stock limitado: " + cantidadMaxima);
                    return null; // o retornar un DTO con el mensaje de error
                }

                CarritoProd carritoProdEntity = new CarritoProd();

                //OBJETO USUARIO
                Usuario usuario = new Usuario();
                usuario.setId(carritoProd.getId_Usuario());

                //OBEJETO PRODUCTO
                Productos productos = new Productos();
                productos.setId_producto(carritoProd.getId_Producto());

                carritoProdEntity.setUsuario(usuario);
                carritoProdEntity.setProductos(productos);
                carritoProdEntity.setCantidad(carritoProd.getCantidad());
                carritoProdEntity.setTotal_carrito(carritoProd.getTotalCarrito());
                // Guardar el nuevo CarritoProd
                carritoProd.setMensaje("Producto agregado correctamente");
                return convertirDto(carritoProdRepo.save(carritoProdEntity), carritoProd.getMensaje());
            }

        return  null;
    }

    private CarritoProdResponse convertirDto(CarritoProd carritoProd, String mensaje) {
        CarritoProdResponse carritoProdResponse = new CarritoProdResponse();
        carritoProdResponse.setId_Carrito(carritoProd.getId_carrito());
        carritoProdResponse.setId_Usuario(carritoProd.getUsuario().getId());
        carritoProdResponse.setId_Producto(carritoProd.getProductos().getId_producto());
        carritoProdResponse.setCantidad(carritoProd.getCantidad());
        carritoProdResponse.setTotalCarrito(carritoProd.getTotal_carrito());
        carritoProdResponse.setMensaje(mensaje);
        return carritoProdResponse;
    }


    @Override
    public CarritoProd aumentarCantidad(Long id) {
        Optional<CarritoProd> carritoOptional = Optional.ofNullable(carritoProdRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id carrito no encontrado")));



        if (carritoOptional.isPresent()) {
            CarritoProd carritoProd = carritoOptional.get();

            // Obtener el producto del stock
            Productos producto = productosRepo.findById(carritoProd.getProductos().getId_producto())
                    .orElseThrow(() -> new ObjectNotFoundException("Producto no encontrado con ID: " + carritoProd.getProductos().getId_producto()));
            int cantidadMaxima = producto.getStock_pro(); // Obtener el stock disponible

            // Verificar si la cantidad que se quiere agregar excede el stock disponible
            if (carritoProd.getCantidad() + 1 > cantidadMaxima) {
                throw new ObjectNotFoundException("Stock máximo alcanzado: " + cantidadMaxima); // Lanzar una excepción con un mensaje específico
            }

            Optional<Productos> productosOptional = productosRepo.findById(carritoProd.getProductos().getId_producto());
            if (productosOptional.isPresent()) {
                Productos productos = productosOptional.get();

                //AUMENTAR LA CANTIDAD Y CALCULAR EL NUEVO PRECIO TOTAL DEL PRODUCTO
                carritoProd.setCantidad(carritoProd.getCantidad() + 1);
                carritoProd.setTotal_carrito(carritoProd.getCantidad() * productos.getPrecio_pro());

                return  carritoProdRepo.save(carritoProd);
            }
        }
        return null;
    }

    @Override
    public CarritoProd diminuirCantidad(Long id) {
        Optional<CarritoProd> carritoOptional = Optional.ofNullable(carritoProdRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Id carrito no encontrado")));
        if (carritoOptional.isPresent()) {
            CarritoProd carritoProd = carritoOptional.get();

            Optional<Productos> productosOptional = productosRepo.findById(carritoProd.getProductos().getId_producto());
            if (productosOptional.isPresent()) {
                Productos productos = productosOptional.get();

                if(carritoProd.getCantidad() > 1){
                    //DISMINUIR LA CANTIDAD Y CALCULAR EL NUEVO PRECIO TOTAL DEL PRODUCTO
                    carritoProd.setCantidad(carritoProd.getCantidad() - 1);
                    carritoProd.setTotal_carrito(carritoProd.getCantidad() * productos.getPrecio_pro());

                  return  carritoProdRepo.save(carritoProd);
                }else{
                   eliminarCarritoProd(id);
                   return null;
                }
            }
        }
        return  null;
    }



}
