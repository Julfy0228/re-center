package com.recenter.repository;

import com.recenter.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository для Service Entity
 * Spring Data автоматически генерирует реализацию для всех методов
 */
@Repository
public interface ServiceJpaRepository extends JpaRepository<Service, Long> {

    /**
     * Найти все услуги по типу
     * @param serviceType тип услуги (DAILY, HOURLY)
     * @return список услуг заданного типа
     */
    List<Service> findByServiceType(String serviceType);

    /**
     * Найти все услуги с ценой меньше заданной
     * @param maxPrice максимальная цена
     * @return список услуг
     */
    List<Service> findByBasePriceLessThan(Double maxPrice);
}
