package com.notifyguard.notify_service.Audit.Repository;


import com.notifyguard.notify_service.Audit.Entity.AnomalyAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnomalyAlertRepository extends JpaRepository<AnomalyAlert, String> {




}


