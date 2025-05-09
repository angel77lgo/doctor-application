package luis.angel.doctorapplication.repository;

import luis.angel.doctorapplication.entity.Appointment;
import luis.angel.doctorapplication.entity.Doctor;
import luis.angel.doctorapplication.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByOfficeAndSchedule(Office officeId, LocalDateTime schedule);
    boolean existsByDoctorAndSchedule(Doctor doctorId, LocalDateTime schedule);
    List<Appointment> findAllByPatientNameAndScheduleBetween(String patientName, LocalDateTime from, LocalDateTime until);
    List<Appointment> findAllByDoctorAndScheduleBetween(Doctor doctorId, LocalDateTime from, LocalDateTime until);
    List<Appointment> findAllByScheduleBetween(LocalDateTime from, LocalDateTime until);
    List<Appointment> findAllByOfficeAndScheduleBetween(Office officeId, LocalDateTime from, LocalDateTime until);
}
