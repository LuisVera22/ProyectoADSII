package pe.com.project.service;

import pe.com.project.model.entity.AttendanceEntity;

public interface AttendanceService {
    AttendanceEntity registerEntry(String dni);
    AttendanceEntity registerDeparture(String dni);
    AttendanceEntity startBreak(String dni);
    AttendanceEntity endBreak(String dni);
}
