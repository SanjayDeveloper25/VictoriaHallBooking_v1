package com.gcc.communityHall.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcc.communityHall.entity.Damage;
import com.gcc.communityHall.entity.EbDetails;
import com.gcc.communityHall.repository.DamageRepository;
import com.gcc.communityHall.repository.EbDetailsRepository;

@Service
public class EbDamageService {
    @Autowired
    private EbDetailsRepository ebDetailsRepository;

    @Autowired
    private DamageRepository damageRepository;

    public void saveEbDetailsAndDamage(
            String ebnumber,
            float startingreading, float endingreading,
            int usedunit, float rate,

            String photo1, String photo2, String photo3,String materials, String comment) {

        // Create EbDetails object
        EbDetails ebDetails = new EbDetails();
        //ebDetails.setEbnumber(ebnumber);
        ebDetails.setStartingReading(startingreading);
        ebDetails.setEndingReading(endingreading);
        ebDetails.setUsedUnit(usedunit);
        ebDetails.setEbAmount(rate);
        // Set other fields of EbDetails similarly

        // Create Damage object
        Damage damage = new Damage();
        //damage.setPhoto1(photo1);
        //   damage.setPhoto2(photo2);
        // damage.setPhoto3(photo3);
        damage.setAssetName(materials);
       // damage.setAssetAmount(damageamount);
        damage.setComments(comment);
        // Set other fields of Damage similarly

        // Call service method to save EbDetails and Damage
        ebDetailsRepository.save(ebDetails);
        damageRepository.save(damage);
    }

    public void saveEbDetailsAndDamage(EbDetails ebDetails, Damage damage) {
        // Save EbDetails and Damage entities
        ebDetailsRepository.save(ebDetails);
        damageRepository.save(damage);
    }
}