package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
