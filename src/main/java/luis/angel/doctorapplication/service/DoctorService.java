package luis.angel.doctorapplication.service;

import luis.angel.doctorapplication.entity.Doctor;
import luis.angel.doctorapplication.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    public Optional<Doctor> findById(String id) {
        return doctorRepository.findById(UUID.fromString(id));
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

}
