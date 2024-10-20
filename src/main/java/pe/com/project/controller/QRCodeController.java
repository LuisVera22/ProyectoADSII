package pe.com.project.controller;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import pe.com.project.exception.UserNotFoundException;
import pe.com.project.model.entity.AttendanceEntity;
import pe.com.project.model.entity.UserEntity;
import pe.com.project.service.AttendanceService;
import pe.com.project.service.QRCodeService;
import pe.com.project.service.UserService;


@Controller
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;
    private final UserService userService;
    @Lazy
    private final AttendanceService attendanceService;

    @GetMapping("/generate-qr/{dni}")
    public String generateQRCode(@PathVariable String dni, Model model) {
        try {
            String qrCode = qrCodeService.generateQRCode(dni);
            model.addAttribute("qrCode", qrCode);
            return "qr_code_view";
        } catch (Exception e) {
            return "redirect:/error_page";
        }
    }

    @GetMapping("/attendance_control")
    public String viewAttendanceControl(Model model) {
		List<UserEntity> userList = userService.userList();
		model.addAttribute("userList", userList);
		return "attendance_control";
    }
    

    @PostMapping("/attendance/{action}/{dni}")
    public ResponseEntity<?> handleAttendanceAction(@PathVariable String action, @PathVariable String dni) {
        try {
            System.out.println("Acción: " + action + ", DNI: " + dni); // Agregado para depuración
            AttendanceEntity attendance = null;
            switch (action) {
                case "entry":
                    attendance = attendanceService.registerEntry(dni);
                    break;
                case "departure":
                    attendance = attendanceService.registerDeparture(dni);
                    break;
                case "startBreak":
                    attendance = attendanceService.startBreak(dni);
                    break;
                case "endBreak":
                    attendance = attendanceService.endBreak(dni);
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Acción no válida: " + action);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage()); // Mensaje de error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la acción: " + action + " para el DNI: " + dni + ". Detalle del error: "
                            + e.getMessage());
        }
    }

    @GetMapping("/scanQR")
    public String getScanQR() {
        return "scan_qr_code";
    }

}
