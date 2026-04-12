package com.notifyguard.notify_service.Notify.repository;

import com.notifyguard.notify_service.Notify.entity.Campaign;
import com.notifyguard.notify_service.Notify.entity.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, String> {

//    List<Campaign> findByStatus(CampaignStatus status);
//    List<Campaign> findByCompanyName(String companyName);
}


