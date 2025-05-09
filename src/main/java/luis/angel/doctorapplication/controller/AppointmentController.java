package luis.angel.doctorapplication.controller;

import luis.angel.doctorapplication.dto.AppointmentCreateDto;
import luis.angel.doctorapplication.entity.Appointment;
import luis.angel.doctorapplication.service.AppointmentService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin()
@RestController()
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/test")
    public String test() {
        return "Endpoint de prueba funcionando!";
    }

    @PostMapping()
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentCreateDto appointment) throws BadRequestException {
        return appointmentService.create(appointment);
    }

    @GetMapping()
    public List<Appointment> findAllAppointments(@RequestParam(name = "date") LocalDate date,
                                                 @RequestParam(required = false) String doctorId,
                                                 @RequestParam(required = false) String officeId) {
        return appointmentService.findAllAppointments(date, doctorId, officeId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable("id") String id,
                                                         @RequestBody AppointmentCreateDto appointment) {
        return appointmentService.update(id, appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") String id) {
        return appointmentService.cancelAppointment(id);
    }

}
