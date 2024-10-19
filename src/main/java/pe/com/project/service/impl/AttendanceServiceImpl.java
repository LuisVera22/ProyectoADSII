package pe.com.project.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.project.model.entity.AttendanceEntity;
import pe.com.project.model.entity.UserEntity;
import pe.com.project.repository.AttendanceRepository;
import pe.com.project.repository.UserRepository;
import pe.com.project.service.AttendanceService;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{
    
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Override
    public AttendanceEntity registerEntry(String dni) {
        UserEntity user = userRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (attendanceRepository.existsByUserAndDepartureTimeIsNull(dni)) {
            throw new RuntimeException("Ya hay un registro de entrada activo para el usuario " + dni);
        }

        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setUser(user);
        attendance.setEntryTime(LocalDateTime.now());
        attendance.setStatus("en curso");

        return attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceEntity registerDeparture(String dni) {
        AttendanceEntity attendance = findLastAttendanceByUser(dni)
                .orElseThrow(() -> new RuntimeException("No hay registro de asistencia para el usuario."));

        if (attendance.getEntryTime() == null) {
            throw new RuntimeException("No se puede registrar la salida sin una entrada previa.");
        }

        if (attendance.getDepartureTime() != null) {
            throw new RuntimeException("Ya se ha registrado una salida para el usuario " + dni);
        }

        attendance.setDepartureTime(LocalDateTime.now());
        attendance.setStatus("finalizado");
        return attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceEntity startBreak(String dni) {
        AttendanceEntity attendance = findLastAttendanceByUser(dni)
                .orElseThrow(() -> new RuntimeException("No hay registro de asistencia para el usuario."));

        if (attendance.getEntryTime() == null) {
            throw new RuntimeException("No se puede iniciar el descanso sin una entrada previa.");
        }

        if (attendance.getStartBreak() != null) {
            throw new RuntimeException("Ya se ha registrado un inicio de descanso para el usuario " + dni);
        }

        attendance.setStartBreak(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceEntity endBreak(String dni) {
        AttendanceEntity attendance = findLastAttendanceByUser(dni)
                .orElseThrow(() -> new RuntimeException("No hay registro de asistencia para el usuario."));

        if (attendance.getStartBreak() == null) {
            throw new RuntimeException("No se puede finalizar el descanso sin haberlo iniciado.");
        }

        if (attendance.getEndBreak() != null) {
            throw new RuntimeException("Ya se ha registrado un fin de descanso para el usuario " + dni);
        }

        attendance.setEndBreak(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    @Override
    public Optional<AttendanceEntity> findLastAttendanceByUser(String dni) {
        return attendanceRepository.findLastAttendanceByUser(dni);
    }
}
