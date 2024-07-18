package com.example.myapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Device predictDevicePrice(Long deviceId) {
        // Implement logic to predict device price using ML model
        // For now, returning the device itself with predicted price
        Device device = deviceRepository.findById(deviceId).orElse(null);
        // Call Python API or ML model to predict price
        // device.setPrice_range(predictedPrice);
        return deviceRepository.save(device);
    }
}
