package com.notifyguard.notify_service.Notify.repository;
import com.notifyguard.notify_service.Notify.entity.CampaignUser;
import com.notifyguard.notify_service.Notify.entity.CampaignUserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CampaignUserRepository extends JpaRepository<CampaignUser, String> {
//    List<CampaignUser> findByCampaignId(String campaignId);
//    List<CampaignUser> findByUserId(String userId);
//    Optional<CampaignUser> findByCampaignIdAndUserId(String campaignId, String userId);
//    List<CampaignUser> findByCampaignIdAndIndividualStatus(
//            String campaignId,
//            CampaignUserStatus individualStatus
//    );
//    String countByCampaignId(String campaignId);
}