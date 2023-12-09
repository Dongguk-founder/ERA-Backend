package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.BusStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// <객체 type, pk type>
@Repository
public interface BusStationRepository extends JpaRepository<BusStationEntity, Long> {
    Optional<BusStationEntity> findByBusName(String busName);
    Optional<BusStationEntity> findByStName(String stName);

    List<BusStationEntity>findAllByStName(String stName);

    Optional<BusStationEntity> findByBusNameAndStName(String busName, String stName);

}
