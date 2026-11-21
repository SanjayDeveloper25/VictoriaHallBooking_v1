package com.gcc.communityHall.repository;

import com.gcc.communityHall.entity.erpPostData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ErpPostDataRepository extends JpaRepository<erpPostData, Long> {

    @Query(value = "SELECT * FROM erp_post_data WHERE status = false", nativeQuery = true)
    List<erpPostData> getNotPostedDetails();

    @Modifying
    @Transactional
    @Query(value = "update erp_post_data set status = ? where id =?", nativeQuery = true)
    void updateERPStatus(boolean status, Long id);
}
