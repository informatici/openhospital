package org.isf.pricesothers.service;

import java.util.List;

import org.isf.pricesothers.model.PricesOthers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceOthersIoOperationRepository extends JpaRepository<PricesOthers, Integer> {
    List<PricesOthers> findAllByOrderByDescriptionAsc();
}