package luis.angel.doctorapplication.repository;

import luis.angel.doctorapplication.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfficeRepository extends JpaRepository<Office, UUID> {
}
