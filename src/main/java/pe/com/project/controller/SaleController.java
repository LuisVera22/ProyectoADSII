package pe.com.project.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.com.project.model.entity.ClientEntity;
import pe.com.project.model.entity.SaleEntity;
import pe.com.project.model.entity.UserEntity;
import pe.com.project.repository.ClientRepository;
import pe.com.project.repository.UserRepository;
import pe.com.project.service.SaleService;

@RestController
@RequiredArgsConstructor
public class SaleController {

    private SaleService saleService;
    private ClientRepository clientRepository;
    private UserRepository userRepository;


    
    @GetMapping("/register_sales")
    public String showRegisterSalesPage(Model model, HttpServletResponse response, HttpSession session) {
        // Verificar si la sesión es válida
        if (session.getAttribute("name") == null) {  
            return "redirect:/error_page"; 
        }
        setCacheHeaders(response);

        String dni = session.getAttribute("dni").toString();
        UserEntity userFound = userRepository.findById(dni).get();
		model.addAttribute("user", userFound);

        List<ClientEntity> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("sale", new SaleEntity());
        return "register_sales";
    }

    @PostMapping("/register_sales")
    public String registerSale(@ModelAttribute("sale") SaleEntity saleEntity, Model model) {
        try {
            saleService.save(saleEntity);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error de integridad de datos: " + e.getMessage());
            model.addAttribute("registrationError", "Error de integridad de datos: " + e.getMessage());
            return "register_sales";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("registrationError", "Ocurrió un error al registrar la venta: " + e.getMessage());
            return "register_sales";
        }
        return "sales_list";
    }
    

    /*@PostMapping("/api/sales")
    public String registerSale(@ModelAttribute("sale") SaleEntity saleEntity, Model model) {
        try {
            saleService.save(saleEntity);  // Aquí llamas al servicio para registrar la venta
        } catch (Exception e) {
            model.addAttribute("registrationError", "Error al registrar la venta");  // Manejar errores
            return "register_sales";  // Volver a mostrar el formulario con un mensaje de error
        }
        return "redirect:/sales/list";  // Redirige a la lista de ventas u otra página tras el éxito
    }*/
    // Mostrar la lista de ventas después de registrar una nueva venta
@GetMapping("/sales/list")
public String listSales(Model model, HttpSession session, HttpServletResponse response) {
    // Verificar si la sesión es válida
    if (session.getAttribute("name") == null) {
        return "redirect:/error_page";  // Redirigir si no hay sesión activa
    }

    setCacheHeaders(response);  // Configurar las cabeceras de caché si es necesario
    
    List<SaleEntity> sales = saleService.findAll();  // Obtener todas las ventas
    model.addAttribute("sales", sales);  // Añadir la lista de ventas al modelo
    return "sales_list";  // El nombre de tu archivo HTML o JSP para mostrar la lista de ventas
}


    private void setCacheHeaders(HttpServletResponse response) {
        // Aquí puedes configurar las cabeceras de caché según tus necesidades
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies
    }



    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<SaleEntity>> getAllSales() {
        List<SaleEntity> sales = saleService.findAll();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<SaleEntity> getSaleById(@PathVariable("id") int id) {
        Optional<SaleEntity> sale = saleService.findById(id);
        if (sale.isPresent()) {
            return new ResponseEntity<>(sale.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Registrar una nueva venta
    //@PostMapping
    //public ResponseEntity<SaleEntity> createSale(@RequestBody SaleEntity saleEntity) {
    //    SaleEntity newSale = saleService.save(saleEntity);
     //   return new ResponseEntity<>(newSale, HttpStatus.CREATED);
    //}

    // Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable("id") int id) {
        Optional<SaleEntity> sale = saleService.findById(id);
        if (sale.isPresent()) {
            saleService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


   

}







