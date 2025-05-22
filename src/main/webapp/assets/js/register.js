function togglePassword(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    if (!passwordInput || !icon) {
        console.error(`Element not found: inputId=${inputId}, iconId=${iconId}`);
        return;
    }
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        icon.classList.replace('fa-eye-slash', 'fa-eye');
    }
}

function previewImage() {
    const fileInput = document.getElementById('image');
    const preview = document.getElementById('imagePreview');
    const previewText = document.querySelector('.preview-text');
    if (!fileInput || !preview) return;
    if (fileInput.files && fileInput.files[0]) {
        if (!fileInput.files[0].type.startsWith('image/')) {
            showToast('Only image files are allowed', 'error');
            fileInput.value = '';
            preview.src = '${pageContext.request.contextPath}/assets/image/default-profile.jpg';
            preview.style.display = 'block';
            if (previewText) previewText.style.display = 'block';
            return;
        }
        if (fileInput.files[0].size > 5 * 1024 * 1024) {
            showToast('Image size must be less than 5MB', 'error');
            fileInput.value = '';
            preview.src = '${pageContext.request.contextPath}/assets/image/default-profile.jpg';
            preview.style.display = 'block';
            if (previewText) previewText.style.display = 'block';
            return;
        }
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
            if (previewText) previewText.style.display = 'none';
        };
        reader.readAsDataURL(fileInput.files[0]);
    }
}

function validateRegisterForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;
    return true; // No client-side validation; let server handle it
}

function showError(inputId, message) {
    // No longer needed since validation is server-side
}

function showToast(message, type) {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = message + '<span class="close-btn" onclick="this.parentElement.remove()">×</span>';
    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add('show'), 100);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.getElementById('image');
    const form = document.getElementById('registerForm');

    if (fileInput) fileInput.addEventListener('change', previewImage);
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validateRegisterForm('registerForm')) {
                e.preventDefault();
            }
        });
    }
});