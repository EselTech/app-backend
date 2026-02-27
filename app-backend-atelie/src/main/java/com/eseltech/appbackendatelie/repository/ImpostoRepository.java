package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Imposto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImpostoRepository extends JpaRepository<Imposto, Integer> {
    public Imposto findByCodigoSgs(Integer codigoSgs);
}
