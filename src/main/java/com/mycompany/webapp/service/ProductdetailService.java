package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.ProductDetail;
import com.mycompany.webapp.repository.ProductDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductdetailService  {
     ProductDetailRepository productDetailRepository;

    public  ProductdetailService(ProductDetailRepository productDetailRepository){
        this.productDetailRepository= productDetailRepository;
    }
    public List<ProductDetail> getByProductId(Integer productId){
       return productDetailRepository.findByProductId(productId);
    }


    public List<ProductDetail> getAllPdWithPdVr(){
       return productDetailRepository.findAllWithPdvariant();
    }

    public ProductDetail getPdWithPdvariant(Long id){
        return productDetailRepository.findProductDetailWithProductVariant(id);
    }
    public List<ProductDetail> searchByKeyword(String keyword){

        return productDetailRepository.searchByName(keyword);

    }

    public Optional<ProductDetail> getById(long l) {
      return productDetailRepository.findById(l);
    }

    public void save(ProductDetail productDetail) {
        productDetailRepository.save(productDetail);
    }

    public void deletePd(Long id) {
        productDetailRepository.deleteById(id);
    }
    public Map<String, List<ProductDetail>> getGroupedByProductAndSupplier() {
        List<ProductDetail> details = productDetailRepository.findAll();

        Map<String, List<ProductDetail>> grouped = new HashMap<>();

        for (ProductDetail detail : details) {
            String key = "danh sách thuộc về sản phẩm "+detail.getProduct().getName()+" mã : "+detail.getProduct().getP_id()+ " - " +"nhà cung cấp : "+detail.getSupplier().getContactName()+" mã : "+detail.getSupplier().getId();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(detail);
        }
        return grouped;
    }
}

