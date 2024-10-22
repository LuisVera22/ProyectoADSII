package pe.com.project.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    @Lazy
    private final AttendanceService attendanceService;
    private final QRCodeService qrCodeService;
    private final UserService userService;

    private void setCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    @GetMapping("/attendance_control")
    public String viewAttendanceControl(HttpSession session, Model model, HttpServletResponse response) {
        if (session.getAttribute("dni") == null) {
            return "redirect:/error_page";
        }

        setCacheHeaders(response);

        String dni = session.getAttribute("dni").toString();
        UserEntity userFound = userService.searchUserById(dni);
        AttendanceEntity lastAttendance = attendanceService.getLastAttendanceByDni(dni);
        model.addAttribute("lastAttendance", lastAttendance);
        model.addAttribute("user", userFound);
        return "attendance_control";
    }

    @GetMapping("/generate-qr/{DNI}")
    public String generateQRCode(@PathVariable String DNI, Model model, HttpSession session,
            HttpServletResponse response) {

        if (session.getAttribute("dni") == null) {
            return "redirect:/error_page";
        }
        setCacheHeaders(response);

        try {
            String qrCode = qrCodeService.generateQRCode(DNI);
            String dni = session.getAttribute("dni").toString();
            UserEntity userFound = userService.searchUserById(dni);

            model.addAttribute("user", userFound);
            model.addAttribute("qrCode", qrCode);
            
            return "qr_code_view";
        } catch (Exception e) {
            return "redirect:/error_page";
        }
    }

    @PostMapping("/attendance/{action}/{dni}")
    public ResponseEntity<?> handleAttendanceAction(@PathVariable String action, @PathVariable String dni, HttpServletResponse response) {
        setCacheHeaders(response);
        try {
            System.out.println("Acción: " + action + ", DNI: " + dni);
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
            System.err.println("Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la acción: " + action + " para el DNI: " + dni + ". Detalle del error: "
                            + e.getMessage());
        }
    }

    @GetMapping("/scanQR")
    public String getScanQR(HttpSession session, Model model, HttpServletResponse response) {
        if (session.getAttribute("dni") == null) {
            return "redirect:/error_page";
        }

        setCacheHeaders(response);

        String dni = session.getAttribute("dni").toString();
        UserEntity userFound = userService.searchUserById(dni);
        model.addAttribute("user", userFound);

        return "scan_qr_code";
    }

}
