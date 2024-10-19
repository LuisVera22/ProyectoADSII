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

    @PostMapping("/attendance/entry/{dni}")
    public ResponseEntity<AttendanceEntity> registerEntry(@PathVariable String dni) {
        try {
            AttendanceEntity attendance = attendanceService.registerEntry(dni);
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/attendance_control")
	public String userList(Model model) {
		List<UserEntity> userList = userService.userList();
		model.addAttribute("userList", userList);
		return "attendance_control";
	}

    @GetMapping("/scanQR")
    public String getScanQR(){
        return "scan_qr_code";
    }

}
