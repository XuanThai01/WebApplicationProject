document.querySelectorAll(".select-voucher").forEach(btn => {
  btn.addEventListener("click", function() {
    const value = this.getAttribute("data-value");
    document.getElementById("voucher-value").textContent = value;
  });
});

//js xử lý button nhập thông tin khách hàng
const inputs = document.querySelectorAll(
  'input[name="nameInputCus"], input[name="phoneInputCus"], input[name="addressInputCus"]'
);
const lockBtn = document.getElementById("lockBtn");
const editBtn = document.getElementById("editBtn");

// Khóa tất cả input
function lockAll() {
  inputs.forEach(input => {
    input.classList.add("locked");
    input.readOnly = true;
  });
}

// Mở tất cả input
function unlockAll() {
  inputs.forEach(input => {
    input.classList.remove("locked");
    input.readOnly = false;
  });
}

lockBtn.addEventListener("click", lockAll);
editBtn.addEventListener("click", unlockAll);

// Khi load trang nếu có sẵn dữ liệu thì khóa
window.addEventListener("DOMContentLoaded", () => {
  inputs.forEach(input => {
    if (input.value.trim() !== "") {
      input.classList.add("locked");
      input.readOnly = true;
    }
  });
});


// Hàm format số thành tiền VND
function formatCurrency(value) {
    return value.toLocaleString("vi-VN") + "₫";
  }

  // Hàm xử lý đổi voucher


  // ================== Cập nhật ước tính giao hàng ===================
  function formatDate(d) {
    return d.toLocaleDateString("vi-VN", { weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric' });
  }
    let cost = 0 ;
    let ngaynhanhang = null;
    function updateEstimate() {
    const method = document.getElementById('shippingMethod').value;
    selectedOption = document.getElementById('shippingMethod').options[this.selectedIndex];
     cost = parseFloat(selectedOption.dataset.cost);  // lấy data-cost
     console.log("giá cost "+cost);
    document.querySelectorAll("#shippingFee, #shippingTotal").forEach(el => {
      el.textContent = formatCurrencyVND(cost);
    });

    const element = document.querySelector(".PriceQuantitys");
    const productPrice = parseFloat(element.dataset.pricequantitys);

    //chưa chọn voucher nào thì in tổng thanh toán chỗ này
    const checkedVouchers = document.querySelectorAll(".voucher-checkbox:checked");
    if (checkedVouchers.length === 0) {
    payprice = productPrice + cost;
        document.querySelector("#grandTotal").textContent = formatCurrencyVND(productPrice + cost);
    }else{
    payprice = remainingPrice + cost;
       document.querySelector("#grandTotal").textContent = formatCurrencyVND(remainingPrice + cost);
    }

    const estimateEl = document.getElementById('shippingEstimate');
    const today = new Date();

    let startDate, endDate;

    if (method === "") {
      estimateEl.textContent = "---";
    } else if (method === "1") {
      startDate = new Date(today);
      console.log(startDate);
      startDate.setDate(today.getDate() + 3);
      endDate = new Date(today);
      endDate.setDate(today.getDate() + 5);
      estimateEl.textContent = "Ước tính: " + formatDate(startDate) + " - " + formatDate(endDate);
        ngaynhanhang = `Ước tính: ${formatDate(startDate)} - ${formatDate(endDate)}`;
    } else if (method === "2") {
      startDate = new Date(today);
      startDate.setDate(today.getDate() + 1);
      endDate = new Date(today);
      endDate.setDate(today.getDate() + 2);
      estimateEl.textContent = "Ước tính: " + formatDate(startDate) + " - " + formatDate(endDate);
      ngaynhanhang = `Ước tính: ${formatDate(startDate)} - ${formatDate(endDate)}`;
    } else if (method === "3") {
      startDate = new Date(today.getTime() + 12 * 60 * 60 * 1000);
      estimateEl.textContent = "Ước tính: " + startDate.toLocaleDateString("vi-VN", { weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit',  second: '2-digit' });
      ngaynhanhang = `Ước tính: ${startDate.toLocaleDateString("vi-VN", {  weekday: 'long',  day: '2-digit',  month: '2-digit',  year: 'numeric',hour: '2-digit', minute: '2-digit'})}`;
    }
  }

    function formatCurrencyVND(amount) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
    }
    document.getElementById('shippingMethod').addEventListener('change', updateEstimate);

    let remainingPrice = 0;
    let payprice = 0;
    document.addEventListener("DOMContentLoaded", function () {
    const maxVouchers = 3;
    const checkboxes = document.querySelectorAll(".voucher-checkbox");
    let selectedVouchers =[];

    const vouchers = document.getElementById('voucher-value');
    const element = document.querySelector(".PriceQuantitys");
    const productPrice = parseFloat(element.getAttribute("data-pricequantitys"));
    document.querySelector("#grandTotal").textContent = formatCurrencyVND(productPrice);
    document.querySelector("#productTotal").textContent = formatCurrencyVND(productPrice);
    console.log(productPrice);


    checkboxes.forEach(cb => {
        cb.addEventListener("change", function () {
            const checked = document.querySelectorAll(".voucher-checkbox:checked");

            // Nếu đủ 3 thì disable mấy cái còn lại
            if (checked.length >= maxVouchers) {
                checkboxes.forEach(c => {
                    if (!c.checked) {
                        c.disabled = true;
                        c.closest(".card").classList.add("opacity-50"); // làm xám
                    }
                });
            } else {
                // Nếu < 3 thì mở lại hết
                checkboxes.forEach(c => {
                    c.disabled = false;
                    c.closest(".card").classList.remove("opacity-50");
                });
            }

            // Lấy danh sách voucher được chọn
            let selectedVouchers = Array.from(checked).map(cb => ({
                type: cb.dataset.type,
                discount: parseFloat(cb.dataset.discount)
            }));

            console.log("Voucher đã chọn:", selectedVouchers);

            // Tính giá trị giảm theo từng voucher
            remainingPrice = productPrice;
            let totalDiscount = 0;
            const discountDetails = [];
            let result = "";

            selectedVouchers.forEach(v => {

                let discountAmount = 0;
                if (v.type === 'AMOUNT') {
                    discountAmount = v.discount;
                } else if (v.type === 'PERCENT') {
                    discountAmount = remainingPrice * (v.discount / 100);
                }
                const formatted = `<span style="color:green;margin: 0 6px;">-${formatCurrencyVND(discountAmount)}</span>`;
                 result +=formatted;

                remainingPrice -= discountAmount;
                totalDiscount += discountAmount;

                discountDetails.push({
                    type: v.type,
                    discountAmount: discountAmount.toFixed(0)
                });
            });
            vouchers.innerHTML = result;
            console.log("Chi tiết giảm từng voucher:", discountDetails);
            console.log("Tổng giảm:", totalDiscount.toFixed(0));
            document.querySelector("#voucherDiscount").textContent = formatCurrencyVND(totalDiscount);
            console.log("Giá sau giảm:", remainingPrice.toFixed(0));
             payprice = remainingPrice+cost;
            document.querySelector("#grandTotal").textContent = formatCurrencyVND(payprice);
            console.log("giá sau giảm +cost"+payprice);
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
  const shippingSelect = document.getElementById("shippingMethod");
  const orderBtn = document.getElementById("orderBtn");
       orderBtn.disabled = true;
  const note = document.getElementById("shippingNote");
   console.log( shippingSelect.value.trim());
  // Theo dõi khi người dùng thay đổi lựa chọn giao hàng
  shippingSelect.addEventListener("change", function () {
    if (shippingSelect.value && shippingSelect.value.trim() !== "") {
    console.log( shippingSelect.value.trim());
      // Đã chọn -> bật nút & ẩn chú thích
      orderBtn.disabled = false;
      note.classList.remove("d-block");
      note.classList.add("d-none");
    } else {
    console.log( shippingSelect.value.trim());
      // Chưa chọn -> tắt nút & hiện chú thích
      orderBtn.disabled = true;
        note.classList.remove("d-none");
        note.classList.add("d-block");
    }
  });
});
  const idc = [];
  document.addEventListener("DOMContentLoaded", function () {
       const elements = document.querySelectorAll(".dataIdc");
          elements.forEach(function(el) {
               idc.push(el.getAttribute("data-idc"));
           });

  });

  document.getElementById("orderBtn").addEventListener("click", function() {
    const iduEl = document.querySelector(".address");
    const idu = iduEl ? iduEl.dataset.idu : null;
    const noteEl = document.querySelector(".notes");
    const note = noteEl ? noteEl.textContent : ""; // nếu không tìm thấy sẽ trả về chuỗi rỗng

    const checkedVouchers = Array.from(document.querySelectorAll(".voucher-checkbox:checked"))
        .map(checkbox => {
            // Lấy phần tử cha gần nhất (ví dụ là div chứa checkbox)
            const parent = checkbox.closest(".vochers");
            // Lấy dữ liệu từ data attribute, ví dụ data-vid
            return parent ? parent.dataset.idvocher : null;
        })
        .filter(v => v !== null); // loại bỏ trường hợp không tìm thấy parent

        const selectEl = document.getElementById("shippingMethod");
        const selectedOption = selectEl.options[selectEl.selectedIndex];
        const idsm = selectedOption.dataset.idsm;

        const selectElpay = document.getElementById("payMethod");
        const selectedOptionPay = selectElpay.options[selectElpay.selectedIndex];
        const paymethod = selectedOptionPay.value;
        console.log("mehot thanh toán"+paymethod);

        const priceProduct = document.querySelector(".PriceQuantitys")?.dataset.pricequantitys;

      // Lấy CSRF token

      const csrfInput = document.querySelector("input[name='_csrf']");
      const csrfHeader = csrfInput ? csrfInput.name : null;
      const csrfToken = csrfInput ? csrfInput.value : null;
      console.log(csrfToken);
      console.log(csrfHeader);

       const data = {
          name: document.getElementById("nameInputCus").value.trim(),
          phone: document.getElementById("phoneInputCus").value.trim(),
          address: document.getElementById("addressInputCus").value.trim(),
          paymethod : paymethod,
          note: note,
          checkedVouchers: checkedVouchers,
          idsm: idsm,
          priceProduct : priceProduct,
          payprice : payprice,
          idc :idc,
          idu : idu,
          ngaynhanhang : ngaynhanhang
      };
      console.log("Server trả về:",data);
if (paymethod === "COD") {
         fetch("/submitOrder", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                     "X-CSRF-TOKEN": csrfToken // gắn CSRF token vào header
                },
                body: JSON.stringify(data)
            })
            .then(res => res.json())
            .then(result => {
                if (result.success) {
                    alert(result.message);
                    window.location.href = "/page-cart"; // chuyển về trang giỏ hàng
                } else {
                    alert(result.message);
                }
            })
            .catch(err => console.error("Lỗi:", err));
        } else if (paymethod === "VNPAY") {
               handleVnPay(data); // gọi hàm async
         } else {
             alert("Vui lòng chọn phương thức thanh toán!");
         }
          async function handleVnPay(data) {
              try {
                  const response = await fetch("/payment/create", {
                      method: "POST",
                      headers: {
                          "Content-Type": "application/json",
                          "X-CSRF-TOKEN": document.querySelector("input[name='_csrf']").value
                      },
                      body: JSON.stringify(data)
                  });

                  const result = await response.json(); // parse response JSON

                  if (result.paymentUrl) {
                        window.location.href = result.paymentUrl;
                  } else {
                      alert("Không thể tạo link thanh toán!");
                  }
              } catch (err) {
                  console.error("Lỗi kết nối:", err);
                  alert("Lỗi khi thanh toán VNPAY!");
              }
          }
      });
