package com.recenter.service;

import com.recenter.model.entity.Category;
import com.recenter.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public com.recenter.model.entity.Service create(com.recenter.model.entity.Service service) {
        return serviceRepository.save(service);
    }

    public Optional<com.recenter.model.entity.Service> getById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<com.recenter.model.entity.Service> getAll() {
        return serviceRepository.findAll();
    }

    public List<com.recenter.model.entity.Service> getByCategory(Category category) {
        return serviceRepository.findByCategory(category);
    }

    public Optional<com.recenter.model.entity.Service> getByTitle(String title) {
        return serviceRepository.findByTitle(title);
    }

    public com.recenter.model.entity.Service update(Long id, com.recenter.model.entity.Service serviceDetails) {
        return serviceRepository.findById(id).map(service -> {
            if (serviceDetails.getTitle() != null) {
                service.setTitle(serviceDetails.getTitle());
            }
            if (serviceDetails.getDescription() != null) {
                service.setDescription(serviceDetails.getDescription());
            }
            if (serviceDetails.getDuration() != null) {
                service.setDuration(serviceDetails.getDuration());
            }
            if (serviceDetails.getPrice() != null) {
                service.setPrice(serviceDetails.getPrice());
            }
            if (serviceDetails.getMaxPeople() != null) {
                service.setMaxPeople(serviceDetails.getMaxPeople());
            }
            if (serviceDetails.getCategory() != null) {
                service.setCategory(serviceDetails.getCategory());
            }
            return serviceRepository.save(service);
        }).orElse(null);
    }

    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }

    public long count() {
        return serviceRepository.count();
    }
}
