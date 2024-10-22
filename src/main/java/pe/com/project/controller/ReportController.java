package pe.com.project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import pe.com.project.model.entity.UserEntity;
import pe.com.project.repository.AttendanceRepository;
import pe.com.project.service.UserService;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final AttendanceRepository attendanceRepository;
    private final UserService userService;

    private void setCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    @GetMapping("/administrator_panel")
    public String getAdministratorPanel(Model model, HttpSession session, HttpServletResponse response) {

        if (session.getAttribute("dni") == null) {
            return "redirect:/error_page";
        }

        setCacheHeaders(response);

        String dni = session.getAttribute("dni").toString();
        UserEntity userFound = userService.searchUserById(dni);

        model.addAttribute("user", userFound);

        List<String> months = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
                "Dec");

        List<Object[]> completedStatusData = attendanceRepository.countCompletedStatusByMonth();

        List<Integer> totals = new ArrayList<>();
        for (Object[] row : completedStatusData) {
            totals.add(((Long) row[1]).intValue());
        }

        model.addAttribute("months", months);
        model.addAttribute("totals", totals);

        return "administrator_panel";
    }

    @GetMapping("/delivery_panel")
    public String getDeliveryPanel(Model model, HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("dni") == null) {
            return "redirect:/error_page";
        }

        setCacheHeaders(response);

        String dni = session.getAttribute("dni").toString();
        UserEntity userFound = userService.searchUserById(dni);

        model.addAttribute("user", userFound);

        return "delivery_panel";
    }
}
