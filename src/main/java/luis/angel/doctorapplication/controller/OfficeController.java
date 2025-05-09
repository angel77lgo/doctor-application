package luis.angel.doctorapplication.controller;

import luis.angel.doctorapplication.entity.Office;
import luis.angel.doctorapplication.service.OfficeService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/api/office")
public class OfficeController {
    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping()
    public List<Office> getAllOffices() {
        return officeService.findAll();
    }
}
