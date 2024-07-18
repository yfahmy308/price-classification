package com.example.myapp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    private final String pythonApiUrl = "https://f96f-34-31-116-21.ngrok-free.app/predict";

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(device);
    }

    @PostMapping
    public Device createDevice(@RequestBody Device device) {
        return deviceRepository.save(device);
    }

    @PostMapping("/predict/{id}")
    public ResponseEntity<?> predictPrice(@PathVariable Long id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(pythonApiUrl, device, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            int predictedPriceRange = (int) response.getBody().get("price_range");
            device.setPriceRange(predictedPriceRange);
            deviceRepository.save(device);
            return ResponseEntity.ok(device);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Error predicting price");
        }
    }
}
 