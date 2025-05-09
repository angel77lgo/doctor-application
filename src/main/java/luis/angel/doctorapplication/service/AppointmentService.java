package luis.angel.doctorapplication.service;

import lombok.extern.slf4j.Slf4j;
import luis.angel.doctorapplication.dto.AppointmentCreateDto;
import luis.angel.doctorapplication.entity.Appointment;
import luis.angel.doctorapplication.entity.Doctor;
import luis.angel.doctorapplication.entity.Office;
import luis.angel.doctorapplication.mapper.AppointmentMapper;
import luis.angel.doctorapplication.repository.AppointmentRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final OfficeService officeService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorService doctorService,
                              OfficeService officeService,
                              AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.officeService = officeService;
        this.appointmentMapper = appointmentMapper;
    }

    public ResponseEntity<Appointment> create(AppointmentCreateDto appointment) throws BadRequestException {
        LocalDateTime start = appointment.getSchedule();
        LocalDateTime end = start.plusHours(1);

        validations(appointment);

        Appointment appointmentToCreate = appointmentMapper.fromDtoToModel(appointment);
        return ResponseEntity.ok(appointmentRepository.save(appointmentToCreate));
    }

    public List<Appointment> findAllAppointments(LocalDate date, String doctorId, String officeId) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime until = date.plusDays(1).atStartOfDay();

        if (doctorId != null) {
            Doctor doctor = doctorService.findById(doctorId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado"));

            return appointmentRepository.findAllByDoctorAndScheduleBetween(doctor, from, until);
        }
        if (officeId != null) {
            Office office = officeService.getOfficeById(officeId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Oficina no encontrada"));

            return appointmentRepository.findAllByOfficeAndScheduleBetween(office, from, until);
        }

        return appointmentRepository.findAllByScheduleBetween(from, until);
    }

    public ResponseEntity<String> cancelAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(UUID.fromString(appointmentId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        if (appointment.getSchedule().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes cancelar una cita pasada");
        }

        appointmentRepository.delete(appointment);

        return ResponseEntity.ok("Cita cancelada");
    }

    public ResponseEntity<Appointment> update(String id, AppointmentCreateDto appointmentCreateDto) {

        Appointment existAppointment = appointmentRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        if (existAppointment.getSchedule().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes editar una cita pasada");
        }

        validations(appointmentCreateDto);

        existAppointment.setSchedule(appointmentCreateDto.getSchedule());
        existAppointment.setPatientName(appointmentCreateDto.getPatientName());

        appointmentRepository.save(existAppointment);

        return ResponseEntity.ok(existAppointment);
    }

    private void validations(AppointmentCreateDto appointmentCreateDto) {
        log.info(appointmentCreateDto.getOfficeId());
        LocalDateTime start = appointmentCreateDto.getSchedule();
        LocalDate date = start.toLocalDate();
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime until = date.plusDays(1).atStartOfDay();

        // Validar oficina
        Optional<Office> optionalOffice = officeService.getOfficeById(appointmentCreateDto.getOfficeId());
        if (optionalOffice.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la oficina");
        }

        // Validar disponibilidad de oficina
        boolean isOfficeBusy = appointmentRepository.existsByOfficeAndSchedule(optionalOffice.get(), start);
        log.debug("isBusy " + isOfficeBusy);

        // Validar doctor
        Optional<Doctor> optionalDoctor = doctorService.findById(appointmentCreateDto.getDoctorId());
        if (optionalDoctor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el medico");
        }

        // Validar disponibilidad de doctor
        boolean isDoctorBusy = appointmentRepository.existsByDoctorAndSchedule(optionalDoctor.get(), start);
        if (isDoctorBusy || isOfficeBusy) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El doctor o el consultorio están ocupados"
            );
        }

        // Validar diferencia de tiempo entre citas del mismo paciente
        List<Appointment> patientAppointments = appointmentRepository.findAllByPatientNameAndScheduleBetween(
                appointmentCreateDto.getPatientName(),
                from,
                until
        );

        for (Appointment appoint : patientAppointments) {
            long diff = Math.abs(Duration.between(appoint.getSchedule(), start).toMinutes());
            if (diff < 120) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La diferencia entre otras es menor a 2 HRS");
            }
        }

        // Validar límite de citas por doctor
        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctorAndScheduleBetween(
                optionalDoctor.get(),
                from,
                until
        );

        if (doctorAppointments.size() >= 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El doctor ya tiene mas de 8 citas");
        }
    }

}
