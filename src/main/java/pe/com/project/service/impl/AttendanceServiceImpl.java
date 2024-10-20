package pe.com.project.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.project.exception.AttendanceAlreadyExistsException;
import pe.com.project.exception.UserNotFoundException;
import pe.com.project.model.entity.AttendanceEntity;
import pe.com.project.model.entity.UserEntity;
import pe.com.project.repository.AttendanceRepository;
import pe.com.project.repository.UserRepository;
import pe.com.project.service.AttendanceService;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceEntity registerEntry(String dni) {
        UserEntity user = findUserByDni(dni);

        // Lanza la excepción si el usuario no se encuentra
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado para el DNI: " + dni);
        }

        // Obtener el rango de fechas del día actual
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // Usar la consulta personalizada para verificar si ya existe una entrada
        boolean existsEntryToday = attendanceRepository.existsByUserAndRecordTypeAndDate(user, "ENTRY", startOfDay,
                endOfDay);

        // Registro de depuración
        System.out.println("User: " + user);
        System.out.println("Exists Entry Today: " + existsEntryToday);
        System.out.println("DNI: " + dni);

        if (existsEntryToday) {
            throw new AttendanceAlreadyExistsException(
                    "Ya se ha registrado una entrada para el usuario con DNI: " + dni + " en el día: "
                            + LocalDate.now());
        }

        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setUser(user);
        attendance.setRecordType("ENTRY");
        attendance.setTime(LocalDateTime.now());
        attendance.setStatus("ACTIVE");

        return attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceEntity registerDeparture(String dni) {
        UserEntity user = findUserByDni(dni);

        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado para el DNI: " + dni);
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // Usar la consulta personalizada para verificar si ya existe una entrada
        boolean existsDepartureToday = attendanceRepository.existsByUserAndRecordTypeAndDate(user, "DEPARTURE",
                startOfDay,
                endOfDay);

        if (existsDepartureToday) {
            throw new AttendanceAlreadyExistsException(
                    "Ya se ha registrado una salida para el usuario con DNI: " + dni + " en el día: "
                            + LocalDate.now());
        }

        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setUser(user);
        attendance.setRecordType("DEPARTURE");
        attendance.setTime(LocalDateTime.now());
        attendance.setStatus("COMPLETED");

        return attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceEntity startBreak(String dni) {
        UserEntity user = findUserByDni(dni);

        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado para el DNI: " + dni);
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // Usar la consulta personalizada para verificar si ya existe una entrada
        boolean existsBreakToday = attendanceRepository.existsByUserAndRecordTypeAndDate(user, "START_BREAK",
                startOfDay,
                endOfDay);

        if (existsBreakToday) {
            throw new AttendanceAlreadyExistsException(
                    "Ya se ha registrado un inicio de descanso para el usuario con DNI: " + dni + " en el día: "
                            + LocalDate.now());
        }

        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setUser(user);
        attendance.setRecordType("START_BREAK");
        attendance.setTime(LocalDateTime.now());
        attendance.setStatus("ON_BREAK");

        return attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceEntity endBreak(String dni) {
        UserEntity user = findUserByDni(dni);

        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado para el DNI: " + dni);
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // Usar la consulta personalizada para verificar si ya existe una entrada
        boolean existsEndBreakToday = attendanceRepository.existsByUserAndRecordTypeAndDate(user, "END_BREAK",
                startOfDay,
                endOfDay);

        if (existsEndBreakToday) {
            throw new AttendanceAlreadyExistsException(
                    "Ya se ha registrado un fin de descanso para el usuario con DNI: " + dni + " en el día: "
                            + LocalDate.now());
        }

        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setUser(user);
        attendance.setRecordType("END_BREAK");
        attendance.setTime(LocalDateTime.now());
        attendance.setStatus("ACTIVE");

        return attendanceRepository.save(attendance);
    }

    private UserEntity findUserByDni(String dni) {
        Optional<UserEntity> userOptional = userRepository.findById(dni);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Usuario no encontrado con DNI: " + dni);
        }
        return userOptional.get();
    }

    public AttendanceEntity getLastAttendanceByDni(String dni) {
        return attendanceRepository.findFirstByUserDniOrderByTimeDesc(dni);
    }
}
