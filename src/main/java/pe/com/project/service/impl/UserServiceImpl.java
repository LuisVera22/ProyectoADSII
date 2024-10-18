package pe.com.project.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pe.com.project.model.entity.UserEntity;
import pe.com.project.repository.UserRepository;
import pe.com.project.service.UserService;
import pe.com.project.utils.Utilitarian;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	
	@Override
	public List<UserEntity> userList() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public void userCreate(UserEntity newUser, MultipartFile photo) {
		// TODO Auto-generated method stub
		String photoName = Utilitarian.saveImage(photo);
		newUser.setUrlPhoto(photoName);

		String passwordHash = Utilitarian.extractHash(newUser.getPassword());
		newUser.setPassword(passwordHash);

		userRepository.save(newUser);
	}

	@Override
	public UserEntity searchUserById(String dni) {
		// TODO Auto-generated method stub
		return userRepository.findById(dni).get();
	}

	@Override
	public void updateUser(String dni, UserEntity updatedUser) {
		// TODO Auto-generated method stub
		UserEntity userFound = searchUserById(dni);
		
		if(userFound == null) {
			throw new RuntimeException("Usuario no encontrado");
		}
		try {
			userFound.setEmail(updatedUser.getEmail());
			userFound.setName(updatedUser.getName());
			userFound.setLastname(updatedUser.getLastname());
			userFound.setNumberPhone(updatedUser.getNumberPhone());
			userFound.setBirthdate(updatedUser.getBirthdate());
			userFound.setRole(updatedUser.getRole());
			userFound.setSchedule(updatedUser.getSchedule());
			userRepository.save(userFound);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Error al actualizar");
		}
	}

	@Override
	public void deleteUser(String dni) {
		// TODO Auto-generated method stub
		UserEntity userFound = searchUserById(dni);
		
		if(userFound == null) {
			throw new RuntimeException("Empleado no encontrado");
		}
		userRepository.delete(userFound);
	}

	@Override
	public boolean validateUser(UserEntity userEntity) {
		// TODO Auto-generated method stub
		UserEntity userFound = userRepository.findByEmail(userEntity.getEmail());

		if(userFound == null){
			return false;
		}
		if(!Utilitarian.checkPassword(userEntity.getPassword(), userFound.getPassword())){
			return false;
		}
		
		return true;
	}
}