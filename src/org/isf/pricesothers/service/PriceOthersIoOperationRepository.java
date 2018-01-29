package org.isf.pricesothers.service;

import java.util.List;

import org.isf.pricesothers.model.PricesOthers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PriceOthersIoOperationRepository extends JpaRepository<PricesOthers, Integer> {
    public List<PricesOthers> findAllByOrderByDescriptionAsc();
}