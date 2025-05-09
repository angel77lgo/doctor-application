package luis.angel.doctorapplication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentCreateDto {
    private String doctorId;
    private String officeId;
    private String patientName;
    private LocalDateTime schedule;
}
