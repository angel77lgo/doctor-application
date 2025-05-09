package luis.angel.doctorapplication.service;

import luis.angel.doctorapplication.entity.Office;
import luis.angel.doctorapplication.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OfficeService {
    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    public Optional<Office> getOfficeById(String id) {
        return officeRepository.findById(UUID.fromString(id));
    }
}
