// admin.js - Admin Dashboard Functionality

document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const addBookBtn = document.getElementById('addBookBtn');
    const bookFormContainer = document.querySelector('.book-form-container');
    const cancelBookBtn = document.getElementById('cancelBookBtn');
    const bookForm = document.getElementById('bookForm');
    const userSearch = document.getElementById('userSearch');
    const bookImageInput = document.getElementById('bookImage');
    const imagePreview = document.getElementById('imagePreview');
    const previewImage = document.getElementById('previewImage');

    // Book Management Functions
    function toggleBookForm(show = true) {
        bookFormContainer.style.display = show ? 'block' : 'none';
        if (!show) {
            bookForm.reset();
            document.getElementById('bookId').value = '';
            imagePreview.style.display = 'none';
        }
    }

    function handleBookFormSubmit(e) {
        e.preventDefault();

        const formData = new FormData();
        const bookId = document.getElementById('bookId').value;
        const isUpdate = !!bookId;

        // Add form data
        formData.append('title', document.getElementById('bookTitle').value);
        formData.append('author', document.getElementById('bookAuthor').value);
        formData.append('publicationYear', document.getElementById('bookYear').value);
        formData.append('category', document.getElementById('bookCategory').value);
        formData.append('stock', document.getElementById('bookStock').value);

        // Add image if exists
        if (bookImageInput.files.length > 0) {
            formData.append('image', bookImageInput.files[0]);
        }

        // Set the appropriate action
        const action = isUpdate ? 'updateBook' : 'addBook';
        formData.append('action', action);

        if (isUpdate) {
            formData.append('bookId', bookId);
        }

        fetch('BookServlet', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(err => { throw err; });
                }
                return response.text();
            })
            .then(message => {
                alert(message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error: ' + error);
            });
    }

    function editBook(bookId) {
        fetch(`BookServlet?action=getBook&id=${bookId}`)
            .then(response => response.json())
            .then(book => {
                document.getElementById('bookId').value = book.bookId;
                document.getElementById('bookTitle').value = book.title;
                document.getElementById('bookAuthor').value = book.author;
                document.getElementById('bookYear').value = book.publicationYear;
                document.getElementById('bookCategory').value = book.category;
                document.getElementById('bookStock').value = book.stock;

                // Handle image preview if exists
                if (book.imageUrl) {
                    previewImage.src = book.imageUrl;
                    imagePreview.style.display = 'block';
                }

                toggleBookForm(true);
            })
            .catch(error => console.error('Error:', error));
    }

    function deleteBook(bookId) {
        if (confirm('Are you sure you want to delete this book?')) {
            fetch('BookServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=deleteBook&bookId=${bookId}`
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(message => {
                    alert(message);
                    window.location.reload();
                })
                .catch(error => {
                    alert('Error deleting book: ' + error.message);
                });
        }
    }

    // User Management Functions
    function deleteUser(userId) {
        if (confirm(`Are you sure you want to delete user #${userId}?`)) {
            fetch('AdminServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=deleteUser&userId=${userId}`
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to delete user');
                    }
                    return response.text();
                })
                .then(message => {
                    alert(message);
                    document.querySelector(`tr[data-userid="${userId}"]`).remove();
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                });
        }
    }

    // Event Listeners
    if (addBookBtn) {
        addBookBtn.addEventListener('click', () => toggleBookForm(true));
    }

    if (cancelBookBtn) {
        cancelBookBtn.addEventListener('click', () => toggleBookForm(false));
    }

    if (bookForm) {
        bookForm.addEventListener('submit', handleBookFormSubmit);
    }

    // Edit book buttons
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function() {
            const bookId = this.closest('tr').querySelector('td:nth-child(2)').textContent;
            editBook(bookId);
        });
    });

    // Delete book buttons
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            const bookId = this.closest('tr').querySelector('td:nth-child(2)').textContent;
            deleteBook(bookId);
        });
    });

    // Return book buttons
    document.querySelectorAll('.return-btn').forEach(button => {
        button.addEventListener('click', function() {
            if (confirm('Mark this book as returned?')) {
                const row = this.closest('tr');
                row.cells[5].innerHTML = '<span class="status-returned">Returned</span>';
                this.disabled = true;
            }
        });
    });

    // Delete user buttons
    document.querySelectorAll('.delete-user-btn').forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-userid');
            deleteUser(userId);
        });
    });

    // User search functionality
    if (userSearch) {
        userSearch.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            document.querySelectorAll('.users-table tbody tr').forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        });
    }

    // Image preview functionality
    if (bookImageInput) {
        bookImageInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(event) {
                    previewImage.src = event.target.result;
                    imagePreview.style.display = 'block';
                };
                reader.readAsDataURL(file);
            } else {
                imagePreview.style.display = 'none';
            }
        });
    }
});