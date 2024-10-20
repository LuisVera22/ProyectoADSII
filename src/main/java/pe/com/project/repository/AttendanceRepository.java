package pe.com.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.com.project.model.entity.AttendanceEntity;
import pe.com.project.model.entity.UserEntity;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Integer> {

    boolean existsByUserAndStatus(UserEntity user, String status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
            "FROM AttendanceEntity a " +
            "WHERE a.user = :user " +
            "AND a.recordType = :recordType " +
            "AND a.time BETWEEN :startOfDay AND :endOfDay")
    boolean existsByUserAndRecordTypeAndDate(@Param("user") UserEntity user, @Param("recordType") String recordType,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    List<AttendanceEntity> findAllByUser(UserEntity user);
}
