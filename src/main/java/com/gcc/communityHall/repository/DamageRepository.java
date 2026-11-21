package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.Damage;

@Repository
public interface DamageRepository extends JpaRepository<Damage,Integer> {
}
