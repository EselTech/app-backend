package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.ShopeeTokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopeeTokenInfoRepository extends JpaRepository<ShopeeTokenInfo, Long> {

    Optional<ShopeeTokenInfo> findByShopId(Long shopId);

    boolean existsByShopId(Long shopId);
}

