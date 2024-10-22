package com.diego.login.services.tienda;

import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.*;
import com.diego.login.persistence.interfaces.IOrdenesProd;
import com.diego.login.persistence.repository.*;
import com.diego.login.persistence.util.EstadoOrdenProd;
import com.diego.login.persistence.util.EstadoProducto;
import com.diego.login.persistence.util.EstadoUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class OrdenesProdService implements IOrdenesProd {

    @Autowired
    private OrdenesProdRepo ordenesProdRepo;
    @Autowired
    private CarritoProdRepo carritoProdRepo;
    @Autowired
    private ProductosRepo productosRepo;
    @Autowired
    private VentasRepo historialVentasRepo;
    @Autowired
    private DetalleOrdenRepo detalleOrdenRepo;
    @Autowired
    private DetalleVentaRepo detalleVentaRepo;
    @Autowired
    private VentasRepo ventasRepo;
    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private EmailService emailService;


    @Override
    public Page<OrdenesProd> listarOrdenes(Pageable pageable) {
        return ordenesProdRepo.findAll(pageable);
    }

    @Override
    public Page<OrdenesProd> getOrdenesProd(Long id, Pageable pageable) {
        return ordenesProdRepo.findByUsuarioId(id, pageable);
    }

    @Override
    public Optional<OrdenesProd> getOrdenesProdById(Long id) {
        return ordenesProdRepo.findById(id);
    }

    @Override
    @Transactional
    public OrdenesProd crearOrdenDesdeCarrito(Long userId) {

        List<CarritoProd> carritoProds = carritoProdRepo.findByUsuarioId(userId);

        double totalOrden = 0.0;

        //OBJETO Orden
        OrdenesProd orden = new OrdenesProd();
        orden.setUsuario(carritoProds.get(0).getUsuario());
        orden.setFecha_orden(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        orden.setEstadoOrdenProd(EstadoOrdenProd.EN_PROCESO);


        for (CarritoProd carritoProd : carritoProds) {
            DetalleOrden detalleOrden = new DetalleOrden();
            detalleOrden.setOrdenesProd(orden);
            detalleOrden.setProducto(carritoProd.getProductos());
            detalleOrden.setCantidad(carritoProd.getCantidad());
            detalleOrden.setTotal(carritoProd.getTotal_carrito());

            totalOrden += carritoProd.getTotal_carrito();
            detalleOrdenRepo.save(detalleOrden);
            // Iterar sobre cada detalle de orden encontrado y actualizar el stock de productos
            Long idProducto = detalleOrden.getProducto().getId_producto();
            Productos productos = productosRepo.findById(idProducto)
                    .orElseThrow(() -> new ObjectNotFoundException("Producto no encontrado con ID: " + idProducto));

            // Verificar stock antes de actualizar
            int nuevoStock = productos.getStock_pro() - detalleOrden.getCantidad();
            productos.setStock_pro(nuevoStock);

            // Actualizar el estado si el stock llega a 0
            if(nuevoStock >= 10){
                productos.setEstado_pro(EstadoProducto.EN_STOCK);
            }
            else if(nuevoStock >=1){
                productos.setEstado_pro(EstadoProducto.POCO_STOCK);
            }
            else{
                productos.setEstado_pro(EstadoProducto.AGOTADO);
            }
            productosRepo.save(productos);
        }

        //UNA VEZ CALCULADO EN TOTAL DE LA ORDEN, GUARDALO EN LA TABLA ORDEN
        orden.setTotal_orden(totalOrden);
        orden = ordenesProdRepo.save(orden);

        //CAMBIAR ESTADO DE USUARIO UNA VEZ QUE TENGA UNA COMPRA EN LA TIENDA.
        Usuario usuario = usuarioRepo.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado"));
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuarioRepo.save(usuario);

        //ELIMINA TODOS LOS PRODUCTOS DEL CARRITO
        carritoProdRepo.deleteAll(carritoProds);

        // Enviar correo de confirmación
        String email = usuario.getEmail(); // Asegúrate de que el usuario tenga un campo de correo
        String subject = "Confirmación de Compra";
       // Agrega detalles de la orden aquí
        String body = "Gracias por tu compra. Tu orden ha sido creada con éxito.\n" +
                "Detalles de la orden:\n" +
                "Total: S/" + orden.getTotal_orden() + "\n\n" +
                "¡Gracias por elegirnos!";
        emailService.sendEmail(email, subject, body);


        //GUARDAR
        return orden;

    }


    @Override
    @Transactional
    public OrdenesProd actualizarPorEstadoOrden(Long id, OrdenesProd ordenesProd) {
        try {
            // Buscar la orden por su ID
            OrdenesProd ordenes = ordenesProdRepo.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada con ID: " + id));

            // Encontrar todos los detalles de orden asociados a la orden encontrada
            List<DetalleOrden> detallesOrden = detalleOrdenRepo.findByOrdenesProdId(id);

            // SI EL ORDEN ES ENTREGADO
            if (ordenesProd.getEstadoOrdenProd().equals(EstadoOrdenProd.ENTREGADO)) {
                // Si el estado es ENTREGADO, actualizar la fecha de expiración
                LocalDateTime fechaExpiracion = LocalDateTime.now().plusHours(10); // Cambiado a 10 horas
                ordenes.setFechaExpiracion(fechaExpiracion);

                // Actualizar el estado de la orden
                ordenes.setEstadoOrdenProd(ordenesProd.getEstadoOrdenProd());
                return ordenesProdRepo.save(ordenes);
            }
            //SI EL ORDEN ES CANCELADO
            else if (ordenesProd.getEstadoOrdenProd().equals(EstadoOrdenProd.CANCELADO)) {
                // Iterar sobre cada detalle de orden encontrado y regresar  el stock de productos
                for (DetalleOrden detalleOrden : detallesOrden) {
                    Long idProducto = detalleOrden.getProducto().getId_producto();
                    Productos productos = productosRepo.findById(idProducto)
                            .orElseThrow(() -> new ObjectNotFoundException("Producto no encontrado con ID: " + idProducto));

                    // Actualizar el stock del producto
                    productos.setStock_pro(productos.getStock_pro() + detalleOrden.getCantidad());
                    // Actualizar el estado si el stock llega a 0
                    if(productos.getStock_pro() >= 10){
                        productos.setEstado_pro(EstadoProducto.EN_STOCK);
                    }
                    else if(productos.getStock_pro() >=1){
                        productos.setEstado_pro(EstadoProducto.POCO_STOCK);
                    }
                    else{
                        productos.setEstado_pro(EstadoProducto.AGOTADO);
                    }

                    productosRepo.save(productos);

                    // Actualizar el estado de la orden
                    ordenes.setEstadoOrdenProd(ordenesProd.getEstadoOrdenProd());
                    return ordenesProdRepo.save(ordenes);
                }

            } else {
                // Si el estado no es ENTREGADO, solo actualizar el estado de la orden
                ordenes.setEstadoOrdenProd(ordenesProd.getEstadoOrdenProd());
                return ordenesProdRepo.save(ordenes);
            }
        } catch (ObjectNotFoundException e) {
            // Capturar y manejar la excepción específica de objeto no encontrado
            throw new RuntimeException("Error al actualizar la orden: " + e.getMessage(), e);
        } catch (Exception e) {
            // Capturar y manejar cualquier otra excepción no esperada
            throw new RuntimeException("Error desconocido al actualizar la orden", e);
        }
        return null;
    }



    @Override
    @Transactional
    public void eliminarOrden(Long id) {
        OrdenesProd ordenesProd = ordenesProdRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Orden no encontrado con ID: " + id));
        // Encontrar todos los detalles de orden asociados a la orden encontrada
        List<DetalleOrden> detallesOrden = detalleOrdenRepo.findByOrdenesProdId(id);

        // Iterar sobre cada detalle de orden encontrado y regresar  el stock de productos
        for (DetalleOrden detalleOrden : detallesOrden) {
            Long idProducto = detalleOrden.getProducto().getId_producto();
            Productos productos = productosRepo.findById(idProducto)
                    .orElseThrow(() -> new ObjectNotFoundException("Producto no encontrado con ID: " + idProducto));

            // Actualizar el stock del producto
            if(!detalleOrden.getOrdenesProd().getEstadoOrdenProd().equals(EstadoOrdenProd.CANCELADO)) {
                productos.setStock_pro(productos.getStock_pro() + detalleOrden.getCantidad());
            }

                //ACTUALIZAR ESTADO DE PRODUCTO
                if(productos.getStock_pro() >= 10){
                    productos.setEstado_pro(EstadoProducto.EN_STOCK);
                }else{
                    productos.setEstado_pro(EstadoProducto.POCO_STOCK);
                }

            productosRepo.save(productos);
        }

        //ELIMINAR ORDEN
        ordenesProdRepo.deleteById(id);

        // VERIFICAR CUANTOS PEDIDOS QUEDAN PARA EL USUARIO
        Long usuarioId = ordenesProd.getUsuario().getId();
        List<OrdenesProd> pedidosUsuario = ordenesProdRepo.findByUsuarioId(usuarioId);
        List<Venta> ventasUsuario = ventasRepo.findByUsuarioId(usuarioId);

        //PROCESAR METODO SI EL USUARIO NO TIENE HISTORIAL DE COMPRAS , DE UN PRODUCTO COMPRADO
        if(ventasUsuario.isEmpty()) {
            if (pedidosUsuario.isEmpty()) {
                // CAMBIAR ESTADO DE USUARIO SI NO HAY PEDIDOS EXISTENTES
                Usuario usuario = usuarioRepo.findById(usuarioId)
                        .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado"));
                usuario.setEstado(EstadoUsuario.INACTIVO);
                usuarioRepo.save(usuario);
            }
        }

    }

    @Transactional
    @Scheduled(fixedDelay = 36000000) // Cada media hora para (ajustar según necesidades)
    public void eliminarOrdenesProd() {
        LocalDateTime now = LocalDateTime.now();
        List<OrdenesProd> ordenesExpiradas = ordenesProdRepo.findByFechaExpiracionBefore(now);
        System.out.println("Buscando órdenes expiradas antes de: " + now);
        System.out.println("Órdenes encontradas: " + ordenesExpiradas.size());

        for (OrdenesProd orden : ordenesExpiradas) {
            System.out.println("Eliminando orden con ID: " + orden.getId());

            // Antes de eliminar, inserta en el historial de ventas
            Venta venta = new Venta();
            venta.setUsuario(orden.getUsuario());
            venta.setFechaVenta(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            // Inicializa el total de la venta
            double totalVenta = 0.0;

            // Encuentra los detalles de la orden
            List<DetalleOrden> listaOrdenes = detalleOrdenRepo.findByOrdenesProdId(orden.getId());
            for (DetalleOrden detalleOrden : listaOrdenes) {
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setVenta(venta);
                detalleVenta.setProducto(detalleOrden.getProducto());
                detalleVenta.setCantidad(detalleOrden.getCantidad());
                detalleVenta.setTotal(detalleOrden.getTotal());
                totalVenta += detalleVenta.getTotal();

                // GUARDAR LA VENTA , ANTES DE GUARDAR EL DETALLE PARA QUE SE PUEDE COLOCAR EL SETVENTA DE DETALLEVENTA
                // Establece el total de la venta y guarda la venta
                venta.setTotalVenta(totalVenta);
                ventasRepo.save(venta);

                //GUARDAR DETALLE VENTA
                detalleVentaRepo.save(detalleVenta);

                // Elimina el detalle de la orden
                detalleOrdenRepo.delete(detalleOrden);
            }


            // Elimina la orden principal
            ordenesProdRepo.delete(orden);
        }
    }

}
