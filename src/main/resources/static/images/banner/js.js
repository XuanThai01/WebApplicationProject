<script>
    document.querySelectorAll(".btn-edit-pv").forEach((btn) => {
  btn.addEventListener("click", function () {
    const row = btn.closest("tr");
    row.classList.add("editing");
    row.querySelectorAll(".itempv").forEach(inp => inp.disabled = false);

    // Ẩn/hiện nút
    row.querySelector(".action-main-pv").classList.add("d-none");
    row.querySelector(".action-edit-pv").classList.remove("d-none");
  });
});

document.querySelectorAll(".btn-cancel-pv").forEach((btn) => {
  btn.addEventListener("click", function () {
    const row = btn.closest("tr");
    row.classList.remove("editing");
    row.querySelectorAll(".itempv").forEach(inp => inp.disabled = true);

    // Ẩn/hiện nút
    row.querySelector(".action-main-pv").classList.remove("d-none");
    row.querySelector(".action-edit-pv").classList.add("d-none");
  });
});

document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('tr').forEach(row => {
    const inputs = row.querySelectorAll('.itempv');

    // Nếu hàng có input (nghĩa là hàng dữ liệu thật)
    if (inputs.length > 0) {
      const originalValuesPv = [];
      inputs.forEach(input => originalValuesPv.push(input.value));

      // 🔹 Lưu vào dataset
      row.dataset.originalValuesPv = JSON.stringify(originalValuesPv);

    }
  });
});

document.querySelectorAll('.btn-cancel-pv').forEach(cancelBtn => {
  cancelBtn.addEventListener('click', function () {
    const row = this.closest('tr');
    const inputs = row.querySelectorAll('.itempv');

    // 🔹 Lấy lại giá trị từ dataset
    const originalValuesPv = JSON.parse(row.dataset.originalValuesPv || "[]");
    inputs.forEach((input, i) => {
      input.value = originalValuesPv[i] || "";
      input.disabled = true;
      input.classList.remove('editable-active');
    });
    row.querySelector('.action-main-pv').classList.remove('d-none');
    row.querySelector('.action-edit-pv').classList.add('d-none');
  });
});

    const btns = document.querySelectorAll(".btnAddProductVariant");
    btns.forEach(btn => {
     btn.addEventListener("click", () => {
    const tbody = document.querySelector("#productTablePv tbody");

    let idPd = btn.getAttribute("data-idpd");


  // Tạo một hàng mới

    const html = document.createElement("tr");

  html.innerHTML = `

         <input type="hidden" name="idpd" value="${idPd}">

        <td>
            <input type="text" id="pv-color" class="form-control form-control-sm itempv"
                >
        </td>

        <td>
            <input type="text" id="pv-size" class="form-control form-control-sm itempv"
                  >
        </td>

        <td>
            <input type="number" id="pv-quantity" class="form-control form-control-sm itempv"
                   >
        </td>

        <td>
            <input type="text" id="pv-price" class="form-control form-control-sm itempv"
                  >
        </td>

        <td>
            <input type="text" id="pv-material" class="form-control form-control-sm itempv"
                   >
        </td>

        <td>
               <!-- Nhóm Lưu/Hủy (ẩn mặc định) -->
                <div class="action-edit-pv  d-flex justify-content-center g-2">
                    <button type="button" class="btn btn-sm btn-success btn-save-pv">
                        <i class="bi bi-check-circle"></i> Lưu
                    </button>
                    <button type="button" class="btn btn-sm btn-secondary btn-cancel-pv ms-2">
                        <i class="bi bi-x-circle"></i> Hủy
                    </button>
                </div>

            </div>
        </td>

  `;

  tbody.appendChild(html);

  // 🔹 Xử lý nút XÓA
  const btnDelete = html.querySelector(".btn-cancel-pv");
  btnDelete.addEventListener("click", () => html.remove());

  // 🔹 Xử lý nút LƯU
  const btnSave = html.querySelector(".btn-save-pv");
  btnSave.addEventListener("click", async () => {
    const data = {
        productId: idPd,  // lấy từ button
        color: html.querySelector("#pv-color").value.trim(),
        size: html.querySelector("#pv-size").value.trim(),
        quantity: html.querySelector("#pv-quantity").value.trim(),
        price: html.querySelector("#pv-price").value.trim(),
        material: html.querySelector("#pv-material").value.trim()
    };


    // Gửi AJAX đến server
    try {
      const res = await fetch("/manage/productVariant/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": document.querySelector("input[name='_csrf']").value
        },
        body: JSON.stringify(data)
      });
       console.log(data);
      const result = await res.text();
      alert("✅ " + result);
    } catch (err) {
      alert("❌ Lỗi khi lưu: " + err.message);
    }
  });
});
});
    document.querySelectorAll('.btn-save-pv').forEach(saveBtn => {
      saveBtn.addEventListener('click', async function () {
        const row = this.closest('tr');
        const inputs = row.querySelectorAll('.itempv');
        const color     = row.querySelector("#pv-color").value.trim();
        const size      = row.querySelector("#pv-size").value.trim();
        const quantity  = row.querySelector("#pv-quantity").value.trim();
        const price     = row.querySelector("#pv-price").value.trim();
        const material  = row.querySelector("#pv-material").value.trim();

        // Lấy idpd từ <tr data-idp="...">
        const idpd = row.dataset.idp;

        // Tạo param gửi về server
        const params = new URLSearchParams();
        params.append("color", color);
        params.append("size", size);
        params.append("quantity", quantity);
        params.append("price", price);
        params.append("material", material);
        params.append("idpd", idpd);
      console.log("📦 Dữ liệu FormData gửi đi:");

        try {
          // 🔹 Gửi dữ liệu lên server bằng Fetch API (AJAX)
          const response = await fetch('/manage/productDetail/update', {
            method: 'POST',
            headers: {
              'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]')?.value || ''
            },
            body: params
          });

          const result = await response.json();

          if (response.ok) {
            alert("✅ Đã lưu dữ liệu thành công!");
            console.log("📥 Server trả về:", result);
        const newValues = [];
        inputs.forEach(input => {
          input.disabled = true;
          input.classList.remove('editable-active');
          newValues.push(input.value);
        });
        row.dataset.originalValues = JSON.stringify(newValues);



        row.querySelector('.action-main-pv').classList.remove('d-none');
        row.querySelector('.action-edit-pv').classList.add('d-none');
        console.log("✅ Đã lưu giá trị mới:", newValues);
      }
    }catch (error) {
          console.error("🚨 Lỗi khi gửi dữ liệu:", error);
          alert("❌ Gửi dữ liệu thất bại, kiểm tra console để xem chi tiết.");
        }
      });
    });
//----------- đây là phần xử lý cho Product Detail----------------

document.querySelectorAll(".btn-edit").forEach((btn) => {
  btn.addEventListener("click", function () {
    const row = btn.closest("tr");
    row.classList.add("editing");
    row.querySelectorAll(".editable").forEach(inp => inp.disabled = false);

    // Ẩn/hiện nút
    row.querySelector(".action-main").classList.add("d-none");
    row.querySelector(".action-edit").classList.remove("d-none");
    row.querySelector(".change-img-btn").classList.remove("d-none");
  });
});

document.querySelectorAll(".btn-cancel").forEach((btn) => {
  btn.addEventListener("click", function () {
    const row = btn.closest("tr");
    row.classList.remove("editing");
    row.querySelectorAll(".editable").forEach(inp => inp.disabled = true);

    // Ẩn/hiện nút
    row.querySelector(".action-main").classList.remove("d-none");
    row.querySelector(".action-edit").classList.add("d-none");
    row.querySelector(".change-img-btn").classList.add("d-none");

  });
});
document.querySelectorAll(".change-img-btn").forEach(btn => {
  btn.addEventListener("click", () => {
    const wrapper = btn.closest(".img-url-wrapper");       // 🔗 gom 3 phần lại thành 1 nhóm
    const fileInput = wrapper.querySelector(".img-file");  // 🔗 lấy input file
    const textInput = wrapper.querySelector(".img-url");   // 🔗 lấy ô text hiển thị link

    fileInput.click(); // 👈 khi bấm nút, mở hộp chọn file

  });
});
document.querySelectorAll('.img-file').forEach(input => {
  input.addEventListener('change', function() {
    const file = this.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
        // tìm <tr> chứa input này và cập nhật ảnh hiển thị
        const tr = input.closest('tr');
        const img = tr.querySelector('.img-show');
        img.src = e.target.result;
        const imgUrlInput = input.parentElement.querySelector('.img-url');
        imgUrlInput.value="/imagesFolder/" + file.name;
      };
      reader.readAsDataURL(file);
    }
  });
});
document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('tr').forEach(row => {
    const inputs = row.querySelectorAll('.editable');
    const imgShow = row.querySelector('.img-show');

    // Nếu hàng có input (nghĩa là hàng dữ liệu thật)
    if (inputs.length > 0) {
      const originalValues = [];
      inputs.forEach(input => originalValues.push(input.value));

      // 🔹 Lưu vào dataset
      row.dataset.originalValues = JSON.stringify(originalValues);
      if (imgShow) {
        row.dataset.originalImgSrc = imgShow.src;
      }
    }
  });
});
document.querySelectorAll('.btn-save').forEach(saveBtn => {
  saveBtn.addEventListener('click', async function () {
    const row = this.closest('tr');
    const form = this.closest('form');
    const inputs = row.querySelectorAll('.editable');
    const imgShow = row.querySelector('.img-show');
    const imgBtn = row.querySelector('.change-img-btn');
    console.log(inputs[0].value);
    // 🔹 Ghi lại giá trị mới thành dữ liệu gốc

    const params = new URLSearchParams();
params.append("productName", inputs[0].value);
params.append("status", inputs[1].value);
params.append("description", inputs[2].value);
params.append("imgUrl", inputs[3].value);
params.append("idpd",row.dataset.idp)
  console.log("📦 Dữ liệu FormData gửi đi:");
  for (let [key, value] of params.entries()) {
    console.log(`- ${key}:`, value);
  }
    try {
      // 🔹 Gửi dữ liệu lên server bằng Fetch API (AJAX)
      const response = await fetch('/manage/products/update', {
        method: 'POST',
        headers: {
          'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]')?.value || ''
        },
        body: params
      });

      const result = await response.json();

      if (response.ok) {
        alert("✅ Đã lưu dữ liệu thành công!");
        console.log("📥 Server trả về:", result);
    const newValues = [];
    inputs.forEach(input => {
      input.disabled = true;
      input.classList.remove('editable-active');
      newValues.push(input.value);
    });
    row.dataset.originalValues = JSON.stringify(newValues);
    if (imgShow) {
      row.dataset.originalImgSrc = imgShow.src;
    }

    // Ẩn nút chọn ảnh + chuyển lại nhóm nút
    imgBtn.classList.add('d-none');
    row.querySelector('.action-main').classList.remove('d-none');
    row.querySelector('.action-edit').classList.add('d-none');
    console.log("✅ Đã lưu giá trị mới:", newValues);
  }
}catch (error) {
      console.error("🚨 Lỗi khi gửi dữ liệu:", error);
      alert("❌ Gửi dữ liệu thất bại, kiểm tra console để xem chi tiết.");
    }
  });
});
document.querySelectorAll('.btn-cancel').forEach(cancelBtn => {
  cancelBtn.addEventListener('click', function () {
    const row = this.closest('tr');
    const inputs = row.querySelectorAll('.editable');
    const imgBtn = row.querySelector('.change-img-btn');
    const imgShow = row.querySelector('.img-show');

    // 🔹 Lấy lại giá trị từ dataset
    const originalValues = JSON.parse(row.dataset.originalValues || "[]");
    inputs.forEach((input, i) => {
      input.value = originalValues[i] || "";
      input.disabled = true;
      input.classList.remove('editable-active');
    });

    if (imgShow) imgShow.src = row.dataset.originalImgSrc;
    imgBtn.classList.add('d-none');

    row.querySelector('.action-main').classList.remove('d-none');
    row.querySelector('.action-edit').classList.add('d-none');
  });
});
document.querySelectorAll('.btn-delete').forEach(deleteBtn => {
  deleteBtn.addEventListener('click', async function () {
    const row = this.closest('tr');
    const productName = row.querySelector('.editable').value; // Tên sản phẩm
    const idInput = row.dataset.idp || null; // Nếu bạn lưu id trong data-id

    // ✅ Hiển thị xác nhận trước khi xóa
    if (!confirm(`⚠️ Bạn có chắc muốn xóa sản phẩm "${productName}" không?`)) {
      return;
    }

    try {
      const response = await fetch("/manage/delete-product", {
        method: "POST",
        headers: {
           'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]')?.value || '',
           "Content-Type": "application/x-www-form-urlencoded"
           },
        body: new URLSearchParams({ id: idInput })
      });

      const result = await response.json();

      if (result.success) {
        alert("🗑️ Đã xóa sản phẩm thành công!");
        row.remove(); // Xóa dòng khỏi bảng giao diện
      } else {
        alert("❌ Không thể xóa: " + result.message);
      }

    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      alert("❌ Kết nối server thất bại!");
    }
  });
});

document.getElementById("btnAddProductDetail").addEventListener("click", function () {
  const tbody = document.querySelector("#productTable tbody");
  const idPAndS = this.closest(".text-end")
    .previousElementSibling // nhảy lên .card
    .previousElementSibling // nhảy lên .idPAndS
    .textContent.trim();

  // Tạo một hàng mới
  const tr1 = document.createElement("tr");
  tr1.innerHTML = `
        <input type="hidden" name="idPAndS" value="${idPAndS}">
      <td><input type="text" name="productName" class="form-control form-control-sm" placeholder="Tên sản phẩm"></td>
      <td>
        <select name="status" class="form-control form-control-sm">
          <option value="CON_HANG">Còn hàng</option>
          <option value="HET_HANG">Hết hàng</option>
        </select>
      </td>
      <td><input type="text" name="description" class="form-control form-control-sm" placeholder="Mô tả"></td>

      <!-- Cột ảnh -->
      <td class="text-center">
        <div class="img-url-wrapper d-flex flex-column align-items-center">
          <!-- Input lưu URL -->
          <input type="text" name="imgUrl" class="form-control form-control-sm img-url mb-1" placeholder="URL ảnh" readonly>

          <!-- Input chọn ảnh -->
          <input type="file" class="form-control form-control-sm img-file mb-1" accept="image/*">

          <!-- Nút chọn ảnh -->
          <button type="button" class="btn btn-outline-primary btn-sm change-img-btn">Chọn ảnh</button>
        </div>
      </td>

      <!-- Hiển thị ảnh -->
      <td><img src="" class="img-show border rounded" alt="" style="width:80px;height:80px;object-fit:cover;"></td>

      <!-- Hành động -->
      <td class="text-center">
        <button type="button" class="btn btn-success btn-save btn-sm">Lưu</button>
        <button type="button" class="btn btn-danger btn-delete btn-sm ms-1">Xóa</button>
      </td>
  `;

  tbody.appendChild(tr1);

  // 🔹 Xử lý chọn ảnh
  const imgFileInput = tr1.querySelector(".img-file");
  const imgUrlInput = tr1.querySelector(".img-url");
  const imgShow = tr1.querySelector(".img-show");
  const changeImgBtn = tr1.querySelector(".change-img-btn");

  // Khi click "Chọn ảnh" => kích hoạt input file
  changeImgBtn.addEventListener("click", () => imgFileInput.click());

  // Khi chọn file ảnh
  imgFileInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (file) {
      const url = URL.createObjectURL(file);
      imgShow.src = url;

      // Giả lập đường dẫn lưu trong hệ thống
      imgUrlInput.value = "/imagesFolder/" + file.name;
    }
  });

  // 🔹 Xử lý nút XÓA
  const btnDelete = tr1.querySelector(".btn-delete");
  btnDelete.addEventListener("click", () => tr1.remove());

  // 🔹 Xử lý nút LƯU
  const btnSave = tr1.querySelector(".btn-save");
  btnSave.addEventListener("click", async () => {
  const data = {
        idPAndS: idPAndS,
        productName: tr1.querySelector("input[name='productName']").value.trim(),
        status: tr1.querySelector("select[name='status']").value.trim(),
        description: tr1.querySelector("input[name='description']").value.trim(),
        imgUrl: tr1.querySelector("input[name='imgUrl']").value.trim()
      };

    // Gửi AJAX đến server
    try {
      const res = await fetch("/manage/product/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": document.querySelector("input[name='_csrf']").value
        },
        body: JSON.stringify(data)
      });
       console.log(data);
      const result = await res.text();
      alert("✅ " + result);
    } catch (err) {
      alert("❌ Lỗi khi lưu: " + err.message);
    }
  });
});
</script>