document.querySelectorAll('.change-status-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const orderId = btn.dataset.orderId;
      document.getElementById('orderId').value = orderId;
      const modal = new bootstrap.Modal(document.getElementById('statusModal'));
      modal.show();
    });
  });

  document.getElementById('saveStatusBtn').addEventListener('click',  async function() {
    const orderId = document.getElementById('orderId').value;
    const newStatus = document.getElementById('statusSelect').value;

    // Demo đổi màu badge (mô phỏng update)
    const card = document.querySelector(`[data-order-id="${orderId}"]`).closest('.order-card');
    const badge = card.querySelector('.badge');

    const csrfToken = document.querySelector('input[name="_csrf"]').value;
    const form = document.getElementById('statusForm');
    // Lấy dữ liệu từ form
        const formData = new FormData(form);

        try {
            const response = await fetch(form.action, {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN': csrfToken
                },
                body: formData
            });

            if (!response.ok) {
                throw new Error('Có lỗi xảy ra khi gửi yêu cầu.');
            }

            // Giả sử server trả JSON như { message: "Cập nhật thành công!" }
            const result = await response.json();
            alert(result.message || "Cập nhật trạng thái thành công!");
        } catch (error) {
            console.error(error);
            alert("❌ Lỗi khi cập nhật trạng thái đơn hàng!");
        }

    switch (newStatus) {
      case 'CHUA_XAC_NHAN':
        badge.className = 'badge bg-warning text-dark';
        badge.textContent = '🕓 Chưa xác nhận';
        break;
      case 'DANG_XU_LY':

        badge.className = 'badge bg-info text-dark';
        badge.textContent = '🔄 Đang xử lý';
        break;
      case 'DA_XAC_NHAN':
        badge.className = 'badge bg-success';
        badge.textContent = '✅ Đã xác nhận';
        break;
      case 'THANH_CONG':
        badge.className = 'badge bg-success';
        badge.textContent = '🚚 đã giao thành công';
        break;
      case 'HUY':
        badge.className = 'badge bg-danger';
        badge.textContent = '❌ Đã hủy';
        break;
    }


    // Đóng modal
    bootstrap.Modal.getInstance(document.getElementById('statusModal')).hide();
  });

  const menuItems = document.querySelectorAll('.menu-item');
  const sections = document.querySelectorAll('.content-section');

  menuItems.forEach(item => {
      item.addEventListener('click', () => {
          menuItems.forEach(i => i.classList.remove('active'));
          item.classList.add('active');

          const target = item.dataset.target;
          sections.forEach(sec => {
              if (sec.id === target) {
                  sec.classList.add('active');
              } else {
                  sec.classList.remove('active');
              }
          });
      });
  });


  const editBtn = document.getElementById('editBtn');
  const saveBtn = document.getElementById('saveBtn');
  const cancelBtn = document.getElementById('cancelBtn');
  const btnAvatarInput = document.getElementById('btnAvatarInput');
  const inputs = document.querySelectorAll('#userForm input');

  // Lưu giá trị gốc
  const originalValues = {};
  inputs.forEach(input => originalValues[input.id] = input.value);

  document.addEventListener('DOMContentLoaded', function() {
    // 💡 Code ở đây chỉ chạy sau khi toàn bộ HTML đã tải xong
    inputs.forEach(i => {
      i.setAttribute('readonly', true);
  });
    console.log('DOM đã sẵn sàng!');
  });

  const avatarPreviewOriginal = document.querySelector('.avatar-container img');
  console.log("đây là ảnh gốc"+avatarPreviewOriginal.src);
  originalValues['avatar'] = avatarPreviewOriginal.src;
  console.log(originalValues);
  // Khi ấn "Sửa"
  editBtn.addEventListener('click', () => {
      inputs.forEach(i => i.removeAttribute('readonly'));
      editBtn.classList.add('d-none');
      saveBtn.classList.remove('d-none');
      cancelBtn.classList.remove('d-none');
      btnAvatarInput.classList.remove('d-none');
  });

  // Khi ấn "Hủy"
  cancelBtn.addEventListener('click', () => {
      inputs.forEach(i => {
          i.setAttribute('readonly', true);
          i.value = originalValues[i.id];
      });
      avatarPreview.src = originalValues['avatar'];
      console.log(originalValues);
      editBtn.classList.remove('d-none');
      saveBtn.classList.add('d-none');
      cancelBtn.classList.add('d-none');
      btnAvatarInput.classList.add('d-none');
  });

  // Khi ấn "Lưu"
  saveBtn.addEventListener('click', async (e)=> {
      e.preventDefault();
      inputs.forEach(i => i.setAttribute('readonly', true));
      // (nếu muốn gửi dữ liệu lên server, thêm AJAX ở đây)
              // ✅ Gói vào FormData để gửi tới controller
              let csrfToken = document.querySelector("input[name='_csrf']").value;
              const formData = new FormData();
              const avatarPreview1 = document.querySelector('.avatar-container img');

                // Lấy dữ liệu từ input
              const email = document.getElementById("email").value;
              const phone = document.getElementById("phone").value;
              const address = document.getElementById("address").value;
              const dob = document.getElementById("dob").value;

              const response = await fetch(avatarPreview1.src);
               console.log("đây là khi submit"+avatarPreview1.src);

              const blob = await response.blob();
              formData.append("avatar", blob, "avatar.jpg");
              formData.append("email", email);
              formData.append("phone", phone);
              formData.append("address", address);
              formData.append("dob", dob);
              try {
                   const response = await fetch("/manage/admin/updateAdmin", {
                        method: "POST",
                        headers: {
                         "X-CSRF-TOKEN": csrfToken // gắn CSRF token vào header
                        },
                        body: formData
                   });
                  if (response.ok) {
                      alert("Cập nhật ảnh thành công!");
                  } else {
                      const msg = await response.text();
                      alert("Lỗi khi upload: " + msg);
                  }
              } catch (err) {
                  console.error("Lỗi kết nối:", err);
              }
      console.log(formData);
      inputs.forEach(i => originalValues[i.id] = i.value); // cập nhật giá trị gốc mới
        originalValues['avatar'] = avatarPreviewOriginal.src;

      console.log(originalValues);
      editBtn.classList.remove('d-none');
      saveBtn.classList.add('d-none');
      cancelBtn.classList.add('d-none');
      btnAvatarInput.classList.add('d-none');
      alert("✅ Lưu thành công!");
  });

const input = document.getElementById('avatarInput');
const avatarPreview = document.querySelector('.avatar-container img');

let blob = null;
input.addEventListener('change', (event) => {
    const file = event.target.files[0];
    if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();

        reader.onload = (e) => {
            // ✅ Lấy byte array
            const bytes = new Uint8Array(e.target.result);
            console.log('Dữ liệu byte:', bytes);

            // ✅ Tạo blob để hiển thị ảnh
             blob = new Blob([bytes], { type: file.type });
            const url = URL.createObjectURL(blob);

            // ✅ Hiển thị ảnh trong thẻ img
            avatarPreview.src = url;
           console.log("đây là khi chọn ảnh"+url);
            // (Tuỳ chọn) Lưu byte vào biến nếu bạn muốn gửi lên server
            window.uploadBytes = bytes;
        };

        // 🧠 Đọc file dưới dạng nhị phân (mảng byte)
        reader.readAsArrayBuffer(file);
    } else {
        alert('Vui lòng chọn tệp ảnh hợp lệ!');
    }
});

const buttons = document.querySelectorAll('.toggle-btn');
const panels = document.querySelectorAll('.panel');

// Duyệt qua từng cặp nút và panel tương ứng
buttons.forEach((btn, index) => {
  const panel = panels[index]; // panel tương ứng với nút này
  const originalText = btn.textContent.trim(); // Lưu nội dung nút gốc

  // Khởi tạo trạng thái ban đầu
  btn.textContent = originalText + ' ▼';

  // Thêm sự kiện click
  btn.addEventListener('click', () => {
    panel.classList.toggle('show'); // Ẩn/hiện panel
    const arrow = panel.classList.contains('show') ? ' ▲' : ' ▼';
    btn.textContent = originalText + arrow;
  });
});

