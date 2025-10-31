package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.Cart;
import com.mycompany.webapp.entity.ProductVariant;
import com.mycompany.webapp.entity.User;
import com.mycompany.webapp.repository.CartRepository;
import com.mycompany.webapp.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private  final ProductVariantRepository productVariantRepository;
    public CartService(CartRepository cartRepository,ProductVariantRepository productVariantRepository) {
        this.cartRepository = cartRepository;
        this.productVariantRepository = productVariantRepository;

    }

    public void addToCart(String guestToken, Long productId, int quantity) {
        Cart cart = new Cart();
        cart.setGuestToken(guestToken);

        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    public Cart save(Cart cart){
       return cartRepository.save(cart);
    }
    public List<Cart> getCartsByGuestToken(String guestToken){
        return cartRepository.findByGuestToken(guestToken);
    }
    public List<Cart> getCart(String guestToken) {
        return cartRepository.findByGuestToken(guestToken);
    }

    public void updateCart(Long idc , Long idv){
         Cart cart = cartRepository.findById(idc) .orElseThrow(() -> new RuntimeException("Cart not found"));
        ProductVariant productVariant = productVariantRepository.findById(idv).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setProductVariant(productVariant);
        cartRepository.save(cart);
    }
    public void updateCartQuantity(Long idc , int q){
        Cart cart = cartRepository.findById(idc) .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setQuantity(q);
        cartRepository.save(cart);
    }
    @Transactional
    public void mergeCart(User user, String sessionId) {
        List<Cart> guestCart = cartRepository.findBytoken(sessionId);
        List<Cart> userCart = cartRepository.findByUserId(user.getId());

        for (Cart guestItem : guestCart) {
            Optional<Cart> existing = userCart.stream()
                    .filter(dbItem -> dbItem.getProductVariant().getId().equals(guestItem.getProductVariant().getId()))
                    .findFirst();

            if (existing.isPresent()) {
                Cart dbItem = existing.get();
                dbItem.setQuantity(dbItem.getQuantity() + guestItem.getQuantity());
                cartRepository.delete(guestItem); // xóa bản guest
                cartRepository.save(dbItem);
            } else {
                guestItem.setUser(user);
                guestItem.setGuestToken(null);
                cartRepository.save(guestItem);
            }
        }
    }

    public List<Cart> getCartByUserId(Long id) {
       return cartRepository.findByUserId(id);
    }

    public List<Cart> getAllCartById(List<Long> ids) {
        return cartRepository.findAllById(ids);
    }

    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    public List<Cart> getCartByGuestToken(String guestToken) {
        return cartRepository.findByGuestToken(guestToken);
    }

    public void deleteCartByGuestToken(String guestToken) {
           cartRepository.deleteAllByGuestToken(guestToken);
    }
}
