package com.StreetAlert.Street_Alert.repository;

import com.StreetAlert.Street_Alert.entity.FetchLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FetchLogRepository extends JpaRepository<FetchLogs, Long> {
}
