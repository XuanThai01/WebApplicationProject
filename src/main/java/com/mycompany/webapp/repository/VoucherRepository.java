package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.Voucher;
import com.mycompany.webapp.entity.VoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    List<Voucher> findByStatus(VoucherStatus status);

    List<Voucher> findByStartDateBeforeAndEndDateAfter(LocalDate startDate, LocalDate endDate);

    List<Voucher> findByDescriptionContainingIgnoreCase(String keyword);

    @Query("SELECT DISTINCT v\n" +
            "FROM Voucher v\n" +
            "LEFT JOIN v.khachHangs u\n" +
            "WHERE u.id = :userId OR u IS NULL")
    Set<Voucher> findAllByUserOrUnassigned(@Param("userId") Long userId);

    @Query("""
            SELECT DISTINCT v
            FROM Voucher v
            LEFT JOIN v.khachHangs u
            WHERE u IS NULL
           """)
    Set<Voucher> findAllUnassigned();

    List<Voucher> findByIdIn(List<Long> ids);

}