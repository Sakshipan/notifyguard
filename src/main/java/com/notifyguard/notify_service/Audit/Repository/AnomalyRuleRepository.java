package com.notifyguard.notify_service.Audit.Repository;

import com.notifyguard.notify_service.Audit.Entity.AnomalyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnomalyRuleRepository extends JpaRepository<AnomalyRule, String> {

  //  List<AnomalyRule> findByEnabledTrue();
}