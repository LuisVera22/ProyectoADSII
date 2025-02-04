package pe.com.project.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pe.com.project.model.entity.UserEntity;
import pe.com.project.repository.UserRepository;
import pe.com.project.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;

	private void setCacheHeaders(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
	}

	@GetMapping("/")
	public String getLogin(Model model, HttpServletResponse response) {
		setCacheHeaders(response);
		model.addAttribute("user", new UserEntity());
		return "login";
	}

	@PostMapping("/login")
	public String viewLogin(@ModelAttribute("user") UserEntity userForm, Model model, HttpSession session,
			HttpServletResponse response) {
		setCacheHeaders(response);
		boolean validatedUser = userService.validateUser(userForm);
		if (validatedUser) {
			UserEntity user = userRepository.findByEmail(userForm.getEmail());
			session.setAttribute("dni", user.getDni());
			
			if (user.getRole().equals("Administrador")) {
				return "redirect:/administrator_panel";
			} else {
				return "redirect:/delivery_panel";
			}

		}

		model.addAttribute("invalidLogin", "El usuario no existe");
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/error_page")
	public String getErrorPage() {
		return "error_page";
	}
	

	@GetMapping("/user_list")
	public String userList(HttpSession session, Model model, HttpServletResponse response) {
		if (session.getAttribute("dni") == null) {
			return "redirect:/error_page";
		}
		setCacheHeaders(response);

		String dni = session.getAttribute("dni").toString();
        UserEntity userFound = userService.searchUserById(dni);
		model.addAttribute("user", userFound);
		List<UserEntity> userList = userService.userList();
		model.addAttribute("userList", userList);
		return "user_list";
	}

	@GetMapping("register_user")
	public String getRegisterUser(Model model, HttpServletResponse response, HttpSession session) {
		if (session.getAttribute("dni") == null) {
			return "redirect:/error_page";
		}
		setCacheHeaders(response);
		String dni = session.getAttribute("dni").toString();
        UserEntity userFound = userService.searchUserById(dni);
		model.addAttribute("userFound", userFound);
		model.addAttribute("user", new UserEntity());
		return "register_user";
	}

	@PostMapping("register_user")
	public String viewRegisterUser(@ModelAttribute("user") UserEntity userEntity,
			Model model, @RequestParam("image") MultipartFile image, HttpServletResponse response) {
		// TODO: process POST request
		setCacheHeaders(response);
		try {
			userService.userCreate(userEntity, image);
		} catch (Exception e) {
			model.addAttribute("registrationError", "Error al registrar el usuario");
			return "register_user";
		}
		return "redirect:/user_list";
	}

}
