package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.BusStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// <객체 type, pk type>
@Repository
public interface BusStationRepository extends JpaRepository<BusStationEntity, Long> {
    @Query("SELECT e FROM BusStationEntity e WHERE e.busName LIKE :bus_name")
    Optional<BusStationEntity> findByBusName(@Param("bus_name")String busName);
}
