package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.Voucher;
import com.mycompany.webapp.entity.VoucherStatus;
import com.mycompany.webapp.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherById(Long id) {
        return voucherRepository.findById(id);
    }

    public Voucher saveVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }

    public List<Voucher> getActiveVouchers() {
        LocalDate now = LocalDate.now();
        return voucherRepository.findByStartDateBeforeAndEndDateAfter(now, now);
    }

    public List<Voucher> getVouchersByStatus(VoucherStatus status) {
        return voucherRepository.findByStatus(status);
    }
    public Set<Voucher> getAllByUserOrUnassigned(Long userId){
        return voucherRepository.findAllByUserOrUnassigned(userId);
    }
    public Set<Voucher> getAllByUnassigned(){
        return voucherRepository.findAllUnassigned();
    }

    public List<Voucher> getAllById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of(); // trả về list rỗng nếu danh sách null hoặc rỗng
        }
        return voucherRepository.findByIdIn(ids);
    }
}
