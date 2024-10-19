package pe.com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.com.project.model.entity.AttendanceEntity;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity,Integer>{
    
    @Query("SELECT COUNT(a) > 0 FROM AttendanceEntity a WHERE a.user.dni = :dni AND a.departureTime IS NULL")
    boolean existsByUserAndDepartureTimeIsNull(@Param("dni") String dni);

    @Query("SELECT a FROM AttendanceEntity a WHERE a.user.dni = :dni ORDER BY a.id DESC")
    Optional<AttendanceEntity> findLastAttendanceByUser(@Param("dni") String dni);
}
