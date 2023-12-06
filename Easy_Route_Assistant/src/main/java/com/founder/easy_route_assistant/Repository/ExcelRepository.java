package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.ExcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcelRepository extends JpaRepository<ExcelEntity, Long> {
    public List<ExcelEntity> findAllByStationName(String stationName);
    public ExcelEntity findByStationCode(String stationCode);
}
