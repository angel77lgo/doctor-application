package luis.angel.doctorapplication.mapper;

import lombok.extern.slf4j.Slf4j;
import luis.angel.doctorapplication.dto.AppointmentCreateDto;
import luis.angel.doctorapplication.entity.Appointment;
import luis.angel.doctorapplication.entity.Doctor;
import luis.angel.doctorapplication.entity.Office;
import luis.angel.doctorapplication.service.DoctorService;
import luis.angel.doctorapplication.service.OfficeService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class AppointmentMapper {

    private final DoctorService doctorService;
    private final OfficeService officeService;

    public AppointmentMapper(DoctorService doctorService,
                             OfficeService officeService) {
        this.doctorService = doctorService;
        this.officeService = officeService;
    }

    public Appointment fromDtoToModel(AppointmentCreateDto appointmentCreateDto) {
        Optional<Doctor> doctor = doctorService.findById(appointmentCreateDto.getDoctorId());
        Optional<Office> office = officeService.getOfficeById(appointmentCreateDto.getOfficeId());

        Appointment appointment = new Appointment();

        appointment.setDoctor(doctor.orElse(null));
        appointment.setOffice(office.orElse(null));
        appointment.setPatientName(appointmentCreateDto.getPatientName());
        appointment.setSchedule(appointmentCreateDto.getSchedule());

        return appointment;
    }


}
