package com.mycompany.webapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.webapp.entity.*;
import com.mycompany.webapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//@RequestMapping("/listpd")
@Controller
public class HomePageController {
    private ProductdetailService productdetailService;
    private ProductVariantService productVariantService;
    private CartService cartService;
    private UserService userService;
    private UserInfoService userInfoService;
    private VoucherService voucherService;
    private ShippingMethodService shippingMethodService;
    private UsedVoucherService usedVoucherService;
    private OrderService orderService;
    private SupplierService supplierService;
    private ProductService productService;
    @Autowired
    AuthenticationManager authManager;

    public HomePageController(ProductService productService,SupplierService supplierService,UserService userService,OrderService orderService,ProductdetailService productdetailService, ProductVariantService productVariantService, CartService cartService,UserInfoService userInfoService,VoucherService voucherService,ShippingMethodService shippingMethodService,UsedVoucherService usedVoucherService) {
        this.productdetailService = productdetailService;
        this.productVariantService = productVariantService;
        this.cartService = cartService;
        this.userService = userService;
        this.userInfoService=userInfoService;
        this.voucherService = voucherService;
        this.shippingMethodService = shippingMethodService;
        this.usedVoucherService =usedVoucherService;
        this.orderService = orderService;
        this.productService=productService;
        this.supplierService=supplierService;
    }

    @GetMapping("/listpd")
    public String getListPdwithPdvr(HttpServletRequest servletRequest,Model model,HttpServletRequest request){


      /*  User userAuthen = userService.findUserByUsername("xuanthai");
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("xuanthai", "123", List.of());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        servletRequest.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

       */

        model.addAttribute("registerRequest", new RegisterRequest());
        String token = servletRequest.getAttribute("guestToken").toString();
         List<ProductDetail> listPd = productdetailService.getAllPdWithPdVr();
         User user= null;
         if(getCurrentUsername(request)!=null){
             user = userService.findUserByUsername(getCurrentUsername(request));
         }
         model.addAttribute("user", user);
         model.addAttribute("guestToken",token);
         model.addAttribute("listpd",listPd);
         return "home-page";
     }
    @GetMapping("/login-page")
    public String loginPage() {
        return "from-signin-signup"; // Thymeleaf render templates/from-signin-signup.html
    }
    public static String getCurrentUsername(HttpServletRequest request) {
        // 1️⃣ Lấy từ Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }

        // 2️⃣ Nếu không có, lấy từ session attribute bạn tự lưu
        HttpSession session = request.getSession(false); // false: không tạo mới nếu chưa có
        if (session != null) {
            Object usernameAttr = session.getAttribute("username");
            if (usernameAttr != null) {
                return usernameAttr.toString();
            }
        }

        // 3️⃣ Nếu không có cả 2
        return null;
    }

    @GetMapping("/creat_cart")
      public String creatCart(@RequestParam("id")Long id,Model model){
        ProductDetail pd = productdetailService.getPdWithPdvariant(id);
        model.addAttribute("productDetail",pd);
        return "creat-cart";
     }
     @PostMapping("/cart")
     public String cart(HttpServletRequest servletRequest,
                        @RequestParam("idpv")Long id,
                        @RequestParam("number")Integer number
                        ){
        String token = servletRequest.getAttribute("guestToken").toString();
         Cart cart = new Cart();
         cart.setGuestToken(token);
         ProductVariant productVariant = productVariantService.getPvbyid(id).get();
         cart.setProductVariant(productVariant);
         cart.setQuantity(number);
         cartService.save(cart);
         return "redirect:/page-cart";
     }
     @GetMapping("/page-cart")
    public String pagecart(HttpServletRequest servletRequest, Model model, Authentication auth){
         List<Cart> carts = new ArrayList<>();
         List<Order> orders = new ArrayList<>();
         // Lấy token an toàn
         Object guestTokenAttr = servletRequest.getAttribute("guestToken");
         String token = guestTokenAttr != null ? guestTokenAttr.toString() : null;
             if(getCurrentUsername(servletRequest)!=null){
                 User user = userService.findUserByUsername(getCurrentUsername(servletRequest));
                 carts = cartService.getCartByUserId(user.getId()); // nên đặt tên rõ ràng
                 orders = orderService.getByUserId(user.getId());
             }

        // Nếu chưa sign up thì fallback về guest cart
         if ((carts == null || carts.isEmpty()) && token != null) {
             carts = cartService.getCartsByGuestToken(token);
             orders = orderService.getByGuestToken(token);
         }

         List<Long> ids_pd = carts.stream()
                 .map(c -> c.getProductVariant().getProductDetail().getPd_id())
                 .distinct()
                 .collect(Collectors.toList());

         List<ProductDetail> productDetails = ids_pd.stream()
                 .map(id_pd -> productdetailService.getPdWithPdvariant(id_pd))
                 .distinct()
                 .collect(Collectors.toList());



           model.addAttribute("carts",carts);
           model.addAttribute("ids_pd",ids_pd);
           model.addAttribute("productdetails",productDetails);
           model.addAttribute("orders",orders);
        return "cart";
     }
     @PostMapping("/updateCart")
     @ResponseBody
    public String updateCart(@RequestParam("idv")Long idv,@RequestParam("idc")Long idc){
        cartService.updateCart(idc,idv);
         System.out.println("thành công ");
        return "update thành công ";
     }
    @PostMapping("/updateCartQty")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCartQty(@RequestBody Map<String, Object> data) {
        Long idc = Long.valueOf(data.get("idc").toString());
        int quantity = Integer.parseInt(data.get("quantity").toString());

        cartService.updateCartQuantity(idc,quantity);

        return ResponseEntity.noContent().build(); // HTTP 204, không có body
    }

    public record ProductDTO(String name) {}
    @GetMapping("/api/products/search")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> search(@RequestParam String keyword) {
        List<ProductDetail> result = productdetailService.searchByKeyword(keyword);
        System.out.println("Keyword: " + keyword + ", found: " + result.size());

        List<ProductDTO> list = result.stream()
                .map(p -> new ProductDTO(p.getName()) )
                .toList();

        return ResponseEntity.ok(list);
    }

    @PostMapping("/creatOder")
    public String creatOrder(@RequestParam("ids") String idsJson, Model model , HttpServletRequest request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Long> ids = mapper.readValue(idsJson, new TypeReference<List<Long>>(){});
        System.out.println("thông tin danh sách id cart : "+ids);
        // xử lý đơn hàng
        List<Cart> carts = cartService.getAllCartById(ids);
        System.out.println("thông tin danh sách cart : "+carts.size());

        List<ProductVariant> productVariants = carts.stream()
                .map(Cart::getProductVariant)
                .collect(Collectors.toList());
        List<ProductDetail> productDetails = productVariants.stream()
                .map(ProductVariant::getProductDetail)
                .collect(Collectors.toList());

        long totalLong = carts.stream()
                .mapToLong(c -> c.getProductVariant().getPrice() * c.getQuantity())
                .sum();
        BigDecimal total = BigDecimal.valueOf(totalLong);

        // xử lý vocher theo user hoặc guest
        if (getCurrentUsername(request)!=null) {
             UserInfo userInfo = userService.findUserByUsername(getCurrentUsername(request)).getUserInfo();
             Long idUser_info = userInfo.getId();

             Set<Voucher> vouchers = voucherService.getAllByUserOrUnassigned(idUser_info);
             System.out.println("số lượng vocher : "+vouchers.size());

             List<Voucher> listVochers =new ArrayList<>(vouchers);
             listVochers.sort(VoucherUtil.getVoucherComparator(total));

             model.addAttribute("vouchers",listVochers);
             model.addAttribute("userInfo",userInfo);
        }else {
            // đối với trường hợp chưa đk hay đn
            Set<Voucher> vouchers = voucherService.getAllByUnassigned();
            System.out.println("số lượng vocher : "+vouchers.size());

            List<Voucher> listVochers =new ArrayList<>(vouchers);
            listVochers.sort(VoucherUtil.getVoucherComparator(total));

            model.addAttribute("vouchers",listVochers);
        }
        List<ShippingMethod> shippingMethods = shippingMethodService.findAll();
        model.addAttribute("shippingMethods",shippingMethods);
        model.addAttribute("productPrice",total);
        model.addAttribute("carts",carts);
        model.addAttribute("productVariants",productVariants);
        model.addAttribute("productDetails",productDetails);

        return "page-creat-order"; // view JSP/Thymeleaf
    }

    public static Order createOder(Map<String, Object> data,ShippingMethodService shippingMethodService,HttpServletRequest servletRequest,UserService userService,CartService cartService){
        Order order = new Order();
        String nameString =(String) data.get("name");
        String phoneString =(String) data.get("phone");
        String addressString =(String) data.get("address");
        String noteString =(String) data.get("note");
        String idUString =(String) data.get("idu");
        String paymethodString =(String) data.get("paymethod");
        String deliveryDateString = (String) data.get("ngaynhanhang");
        Long idSmLong =   Long.valueOf((String) data.get("idsm"));
        BigDecimal paypriceDecimal = new BigDecimal(data.get("payprice").toString());
        BigDecimal priceProduct = new BigDecimal(data.get("priceProduct").toString());
        order.setPayMethod(PayMethod.valueOf(paymethodString));
        order.setDeliveryDate(deliveryDateString);
        order.setTotalAmount(priceProduct);
        order.setPayPrice(paypriceDecimal);
        order.setShippingAddress(addressString);
        order.setGuestName(nameString);
        order.setGuestPhone(phoneString);
        order.setNote(noteString);
        ShippingMethod shippingMethod =shippingMethodService.findById(idSmLong).get();
        order.setShippingMethod(shippingMethod);
        String guestToken  =(String) servletRequest.getAttribute("guestToken");

        if(idUString==null){
            order.setGuestToken(guestToken);
        }else{
            Long idu= Long.valueOf(idUString);
            order.setUser(userService.findUserById(idu).get());
        }

        List<String> idStrListC = (List<String>) data.get("idc");

        List<Long> idLongListC = idStrListC.stream()
                .map(Long::valueOf)
                .toList();
        List<Cart> carts = cartService.getAllCartById(idLongListC);

        List<OrderDetail> orderDetails = carts.stream()
                .map(cart -> {
                    OrderDetail od = new OrderDetail();
                    od.setOrder(order);                     // gán Order
                    od.setProductVariant(cart.getProductVariant()); // gán ProductVariant
                    od.setQuantity(cart.getQuantity());     // gán số lượng từ Cart
                    // có thể thêm giá nếu cần
                    return od;
                })
                .collect(Collectors.toList());
        order.setOrderDetails(orderDetails);
        return order;
    }

    @PostMapping("/submitOrder")
    @ResponseBody
    public Map<String, Object> submitOrder(@RequestBody Map<String, Object> data ,HttpServletRequest servletRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Xử lý tạo đơn hàng...
            Order order =createOder(data,shippingMethodService,servletRequest,userService,cartService);
            orderService.save(order);
            List<String> idStrListV = (List<String>) data.get("checkedVouchers");


            if (idStrListV != null && !idStrListV.isEmpty()) {
                List<Long> idLongListV = idStrListV.stream()
                        .map(Long::valueOf)
                        .toList();
                // xử lý danh sách idLongListV
                List<Voucher> voucherList = voucherService.getAllById(idLongListV);
                List<UsedVoucher>  usedVoucherList = usedVoucherService.applyVouchers(order,voucherList);
            }
            response.put("success", true);
            response.put("message", "Khởi tạo đơn hàng thành công, chuyển về trang giỏ hàng để xem chi tiết");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
        return response;

    }
    @GetMapping("/page-order-detail")
    public String getPageOderDetail(@RequestParam("oid")String orderId,Model model){
        Long orderIdLong = Long.parseLong(orderId);
        Order order = orderService.getById(orderIdLong).get();
        model.addAttribute("order",order);
        System.out.println(order.getNote());
          return "page-oder-detail";
    }

    @PostMapping("/orders/save")
    @ResponseBody
    public Map<String, Object> saveOrder(@RequestBody Map<String, Object> data) {
        Long ido = Long.valueOf((String)data.get("ido"));
        Order order = orderService.getById(ido).get();
         order.setGuestName((String)data.get("guestName"));
         order.setGuestPhone((String)data.get("guestPhone"));
         order.setShippingAddress((String)data.get("shippingAddress"));
         order.setNote((String)data.get("note"));
         orderService.save(order);
        System.out.println("Dữ liệu nhận được: " + data);
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "ok");
        return resp;
    }

    @DeleteMapping("/orders/delete")
    @ResponseBody
    public ResponseEntity<String> deleteOrder(@RequestBody Map<String, Long> body) {
        Long id = body.get("id");
        try {
            orderService.deleteById(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting order");
        }
    }
    @DeleteMapping("/cart/delete")
    @ResponseBody
    public ResponseEntity<String> deleteCart(@RequestBody Map<String, Object> data) {
        try {
            Long id = Long.parseLong(data.get("id").toString());
            cartService.deleteById(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa giỏ hàng");
        }
    }
    public record DiscountInfoDTO(
            BigDecimal userVoucherDiscount,   // giảm giá từ khách hàng
            BigDecimal shopVoucherDiscount,   // giảm giá từ cửa hàng
            List<Long> voucherIds
    ) {}
    @GetMapping("/manage/admin/dashboard")
    public  String getPageManageADMIN(@RequestParam("user") String username, Model model){
        User userADMIN= userService.findUserByUsername(username);
        Map<String, List<Product>> productMap = new LinkedHashMap<>();
        List<Order> orders = orderService.getAll();
        Map<String, List<Order>> orderMap = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getUser()!= null ? "USER_" + o.getUser().getUsername() : "GUEST_" + o.getGuestToken()
                ));

        List<UsedVoucher> usedVoucherList = usedVoucherService.getAll();

        Map<Order, DiscountInfoDTO> orderDiscountMap = orders.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        order -> {
                            List<Voucher> vouchers = usedVoucherList.stream()
                                    .filter(uv -> uv.getOrder().equals(order))
                                    .map(UsedVoucher::getVoucher)
                                    .collect(Collectors.toList());

                            // Nếu không có voucher => giữ BigDecimal.ZERO
                            BigDecimal userDiscount = BigDecimal.ZERO;
                            BigDecimal shopDiscount = BigDecimal.ZERO;

                            Set<Voucher> userVouchers = new HashSet<>();
                            if (order.getUser() != null && vouchers!=null) {
                                // Có user → lấy danh sách voucher riêng của user
                                userVouchers = order.getUser().getUserInfo().getVouchers();
                            }

                            // Tính toán tuỳ logic bạn
                            if (vouchers!=null ){
                                for (Voucher v : vouchers) {
                                    BigDecimal discountValue = VoucherUtil.getDiscountForProduct(v, order.getTotalAmount());
                        /*            System.out.println("Check: " + v.getId() + " - discountValue=" + discountValue + " - total=" + order.getTotalAmount());

                                    if (discountValue == null) discountValue = BigDecimal.ZERO;

                                    if (order.getUser() != null && userVouchers.contains(v))
                                        userDiscount = userDiscount.add(discountValue);
                                    else
                                        shopDiscount = shopDiscount.add(discountValue);

                         */
                                    if (order.getUser() != null && userVouchers.contains(v) ) userDiscount = userDiscount.add(VoucherUtil.getDiscountForProduct(v,order.getTotalAmount()));
                                else shopDiscount = shopDiscount.add(VoucherUtil.getDiscountForProduct(v,order.getTotalAmount()));
                                }
                            }

                            return new DiscountInfoDTO(userDiscount, shopDiscount, null);
                        }
                ));
        model.addAttribute("orderDiscountInfoDTOMap",orderDiscountMap);
        model.addAttribute("user", userADMIN);
        model.addAttribute("orderMap",orderMap);
        return "page-manage-ADMIN";
    }
    @GetMapping("/manage/admin/manageProduct")
    public String getPageManageProductbyADMIN(Model model){
        Map<String, List<ProductDetail>> map = productdetailService.getGroupedByProductAndSupplier();
        model.addAttribute("map",map);
        System.out.println("map+" +map.size());
        return "page-manage-product";
    }
    private MediaType detectImageType(byte[] imageBytes) {
        if (imageBytes.length > 3 && imageBytes[0] == (byte)0xFF && imageBytes[1] == (byte)0xD8)
            return MediaType.IMAGE_JPEG;
        else if (imageBytes.length > 8 && imageBytes[0] == (byte)0x89 && imageBytes[1] == (byte)0x50)
            return MediaType.IMAGE_PNG;
        else if (imageBytes.length > 3 && imageBytes[0] == (byte)0x47 && imageBytes[1] == (byte)0x49)
            return MediaType.IMAGE_GIF;
        else
            return MediaType.APPLICATION_OCTET_STREAM; // fallback
    }
    @GetMapping("/manage/admin/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getUserImage(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.get().getUserInfo().getProfileImage() == null) {

            System.out.println("chuỗi rỗng ");
        }
        if (optionalUser.isPresent() && optionalUser.get().getUserInfo().getProfileImage() != null) {
            byte[] imageBytes = optionalUser.get().getUserInfo().getProfileImage();



            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(detectImageType(imageBytes));
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping("/manage/admin/updateAdmin")
    @ResponseBody
        public ResponseEntity<String> updateUser(
                @RequestParam("email") String email,
                @RequestParam("phone") String phone,
                @RequestParam("address") String address,
                @RequestParam("dob") String dob,
                @RequestParam("avatar") MultipartFile avatar,
                HttpServletRequest servletRequest
        ) {
            try {
                // 🧩 Lấy user hiện tại (ví dụ từ session hoặc SecurityContext)
                UserInfo userInfo = userService.findUserByUsername(getCurrentUsername(servletRequest)).getUserInfo(); // bạn viết hàm này trong service nhé

                // Cập nhật thông tin cơ bản
                userInfo.setProfileImage(avatar.getBytes());
                userInfo.setEmail(email);
                userInfo.setPhone(phone);
                userInfo.setAddress(address);
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
                userInfo.setDob(new java.sql.Date(date.getTime()));

                // ✅ Nếu có ảnh đại diện thì cập nhật

                userInfoService.save(userInfo);

                return ResponseEntity.ok("Cập nhật thông tin thành công!");
            }catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("Lỗi khi cập nhật user: " + e.getMessage());
            }
        }

        //thanh toán ONLINE

    // Tạo đơn hàng & redirect tới "cổng thanh toán"
    @PostMapping("/fakepay/checkout")
    @ResponseBody
    public Map<String,String> checkout(@RequestBody Map<String, Object> data ,HttpServletRequest servletRequest,
                           Model model) {
        Order order = createOder(data,shippingMethodService,servletRequest,userService,cartService);
        order.setOrderStatusPay(OrderStatusPay.PAID);
        orderService.save(order);
        Map<String,String> linkmap =new HashMap<>();
        linkmap.put("result","/fakepay/page/"+ order.getOrderId());
        return linkmap; // JSP hiển thị nút thanh toán
    }
    @GetMapping("/fakepay/page/{id}")
    public String getpagefakepay(@PathVariable Long id,Model model){
        Order order =orderService.getById(id).get();
        model.addAttribute("order",order);
        return "checkout";
    }
    // Xử lý "thanh toán"
    @PostMapping("/fakepay/pay/{id}")
    public String pay(@PathVariable Long id ,
                      @RequestParam("action") String status,
                      Model model) {
        Order order = orderService.getById(id).get();
        if ("success".equalsIgnoreCase(status)) {
              order.setOrderStatusPay(OrderStatusPay.PAID);
            // ✅ Dùng Stream API để trừ tồn kho từ DB
            order.getOrderDetails().stream()
                    .filter(detail -> detail.getProductVariant() != null)
                    .forEach(detail -> {
                        ProductVariant pv = detail.getProductVariant();
                        int newStock = Math.max(pv.getQuantity() - detail.getQuantity(), 0);
                        pv.setQuantity(newStock);
                        productVariantService.save(pv);
                    });
        } else {
            order.setOrderStatusPay(OrderStatusPay.CANCELLED);
        }
        orderService.save(order);
        model.addAttribute("order", order);
        return "result"; // JSP hiển thị kết quả
    }
    @PostMapping("/manage/orders/update-status")
    @ResponseBody
    public   Map<String, String> updateOrderStatus(@RequestParam("orderId")  String orderIdStr,
                                    @RequestParam("status") String newStatus)
                                     {
         // 🔹 Lấy đơn hàng từ DB
         Long orderId = Long.parseLong(orderIdStr); // ✅ ép kiểu String → Long
         Order order = orderService.getById(orderId)
                 .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

         // 🔹 Cập nhật trạng thái đơn hàng
         OrderStatus statusEnum = OrderStatus.valueOf(newStatus);
         order.setStatus(statusEnum);

         // 🔹 Nếu đơn hàng đã xác nhận hoặc giao thành công → trừ số lượng tồn
         if (statusEnum == OrderStatus.DA_XAC_NHAN || statusEnum == OrderStatus.THANH_CONG) {

             order.getOrderDetails().stream()
                     .forEach(detail -> {
                         ProductVariant pv = detail.getProductVariant();
                         if (pv != null) {
                             int currentStock = pv.getQuantity();
                             int orderedQty = detail.getQuantity();

                             // 🔹 Giảm số lượng sản phẩm trong kho
                             int updatedStock = Math.max(currentStock - orderedQty, 0);
                             pv.setQuantity(updatedStock);

                             // 🔹 Lưu lại vào DB
                             productVariantService.save(pv);
                         }
                     });
         }

         // 🔹 Lưu đơn hàng sau khi thay đổi trạng thái
         orderService.save(order);
        return Map.of("message", "✅ Cập nhật trạng thái đơn hàng thành công!");

    }
    @PostMapping("/manage/products/update")
    @ResponseBody
        public ResponseEntity<?> updateProduct( @RequestParam("productName") String productName,
                                                @RequestParam("status") String status,
                                                @RequestParam("description") String description,
                                                @RequestParam("imgUrl") String file,
                                                @RequestParam("idpd") String idpd ) {
            try {
                System.out.println(productName);
                // ⚙️ Ở đây bạn có thể gọi service/DAO để update DB thật
                // productService.updateProduct(dto);
                ProductDetail productDetail = productdetailService.getById(Long.parseLong(idpd)).get();
                productDetail.setName(productName);
                if("CON_HANG".equals(status)){
                    productDetail.setStatus(1);
                }else if ("HET_HANG".equals(status)){
                    productDetail.setStatus(0);
                }
                productDetail.setDescriptiondetail(description);
                productDetail.setImgProduct(file);
                productdetailService.save(productDetail);
                return ResponseEntity.ok().body(Map.of(
                        "success", true,
                        "message", "Đã cập nhật sản phẩm thành công!"
                ));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Lỗi khi cập nhật: " + e.getMessage()
                ));
            }
        }

    @PostMapping("/manage/delete-product")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@RequestParam("id") Long id) {
        try {
            System.out.println("🗑️ Xóa sản phẩm có ID: " + id);

            // Gọi service hoặc repository để xóa DB
            // productService.deleteById(id);
            productdetailService.deletePd(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Đã xóa thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Lỗi khi xóa: " + e.getMessage()
            ));
        }
    }
    @PostMapping("/manage/product/add")
    @ResponseBody
    public ResponseEntity<String> addProduct(@RequestBody Map<String, String> data) {
        try {
            String productName = data.get("productName");
            String status = data.get("status");
            String description = data.get("description");
            String imgUrl = data.get("imgUrl");
            ProductDetail productDetail =new ProductDetail();
            String key = data.get("idPAndS");

            if("CON_HANG".equals(status)){
                productDetail.setStatus(1);
            }else if ("HET_HANG".equals(status)){
                productDetail.setStatus(0);
            }
            productDetail.setName(productName);
            productDetail.setDescriptiondetail(description);
            productDetail.setImgProduct(imgUrl);
            Integer productId = null;
            Integer supplierId = null;

            java.util.regex.Matcher matcher = java.util.regex.Pattern
                    .compile("mã\\s*:(\\d+).*mã\\s*:(\\d+)")
                    .matcher(key);

            if (matcher.find()) {
                productId = Integer.parseInt(matcher.group(1));
                supplierId = Integer.parseInt(matcher.group(2));
            }

            System.out.println("Product ID: " + productId);
            System.out.println("Supplier ID: " + supplierId);
            productDetail.setProduct(productService.getproductbyid(productId));
            productDetail.setSupplier(supplierService.getSupplierById(supplierId).get());
            productdetailService.save(productDetail);
            // Trả kết quả về JS
            return ResponseEntity.ok("Sản phẩm '" + productName + "' đã được thêm thành công!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
    }

    }



