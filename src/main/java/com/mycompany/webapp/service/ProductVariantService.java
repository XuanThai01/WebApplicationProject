package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.ProductVariant;
import com.mycompany.webapp.repository.ProductDetailRepository;
import com.mycompany.webapp.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantService {
    private ProductVariantRepository productVariantRepository;

    public ProductVariantService(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }
   public ProductVariant getpvWithPd(Long id){
      return  productVariantRepository.findPvWithPdById(id);
    }
    public Optional<ProductVariant> getPvbyid(Long id){
        return productVariantRepository.findById(id);
    }

    public List<ProductVariant> getAllPvbyPvId(List<Long> ids) {
        return  productVariantRepository.findAllById(ids);
    }

    public void save(ProductVariant pv) {
        productVariantRepository.save(pv);
    }
}
