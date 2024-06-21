$(document).ready(function() {
    loadCategories();
    loadBrands();
    loadProducts();

    // Category Form Submission
    $('#category-form').submit(function(event) {
        event.preventDefault();
        const categoryId = $('#category-id').val();
        const categoryName = $('#category-name').val();
        const categoryData = { name: categoryName };

        if (categoryId) {
            // Update category
            $.ajax({
                url: `http://localhost:20000/v1/categories/${categoryId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(categoryData),
                success: function() {
                    loadCategories();
                    $('#category-form')[0].reset();
                },
                error: function(xhr) {
                    alert('Error updating category: ' + xhr.responseText);
                }
            });
        } else {
            // Create category
            $.post({
                url: 'http://localhost:20000/v1/categories',
                contentType: 'application/json',
                data: JSON.stringify(categoryData),
                success: function() {
                    loadCategories();
                    $('#category-form')[0].reset();
                },
                error: function(xhr) {
                    alert('Error creating category: ' + xhr.responseText);
                }
            });
        }
    });

    // Brand Form Submission
    $('#brand-form').submit(function(event) {
        event.preventDefault();
        const brandId = $('#brand-id').val();
        const brandName = $('#brand-name').val();
        const brandData = { name: brandName };

        if (brandId) {
            // Update brand
            $.ajax({
                url: `http://localhost:20000/v1/brands/${brandId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(brandData),
                success: function() {
                    loadBrands();
                    $('#brand-form')[0].reset();
                },
                error: function(xhr) {
                    alert('Error updating brand: ' + xhr.responseText);
                }
            });
        } else {
            // Create brand
            $.post({
                url: 'http://localhost:20000/v1/brands',
                contentType: 'application/json',
                data: JSON.stringify(brandData),
                success: function() {
                    loadBrands();
                    $('#brand-form')[0].reset();
                },
                error: function(xhr) {
                    alert('Error creating brand: ' + xhr.responseText);
                }
            });
        }
    });

    // Product Form Submission
    $('#product-form').submit(function(event) {
        event.preventDefault();
        const productId = $('#product-id').val();
        const productBrand = $('#product-brand').val();
        const productCategory = $('#product-category').val();
        const productPrice = $('#product-price').val();
        const productData = {
            brand: { id: productBrand },
            category: { id: productCategory },
            price: productPrice
        };

        if (productId) {
            // Update product
            $.ajax({
                url: `http://localhost:20000/v1/sales-products/${productId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(productData),
                success: function() {
                    loadProducts();
                    $('#product-form')[0].reset();
                },
                error: function(xhr) {
                    alert('Error updating product: ' + xhr.responseText);
                }
            });
        } else {
            // Create product
            $.post({
                url: 'http://localhost:20000/v1/sales-products',
                contentType: 'application/json',
                data: JSON.stringify(productData),
                success: function() {
                    loadProducts();
                    $('#product-form')[0].reset();
                },
                error: function(xhr) {
                    alert('Error creating product: ' + xhr.responseText);
                }
            });
        }
    });

    // Load the cheapest prices by category
    $('#load-cheapest-by-category').click(function() {
        $.get('http://localhost:20000/v1/prices/categories/cheapest-brand', function(response) {
            $('#cheapest-by-category-list').empty();
            response.data.categories.forEach(function(item) {
                $('#cheapest-by-category-list').append(`
                    <tr>
                        <td>${item.categoryName}</td>
                        <td>${item.brandName}</td>
                        <td>${item.price}</td>
                    </tr>
                `);
            });
            $('#total-price-by-category').text(response.data.totalPrice);
        }).fail(function(xhr) {
            alert('Error loading cheapest prices by category: ' + xhr.responseText);
        });
    });

    // Load the cheapest prices by brand
    $('#load-cheapest-by-brand').click(function() {
        $.get('http://localhost:20000/v1/prices/brands/cheapest-total-category-price', function(response) {
            $('#cheapest-by-brand-list').empty();
            $('#cheapest-by-brand-list').append(`
                <tr>
                    <td>${response.data.brandName}</td>
                    <td colspan="2"></td>
                </tr>
            `);
            response.data.categories.forEach(function(item) {
                $('#cheapest-by-brand-list').append(`
                    <tr>
                        <td></td>
                        <td>${item.categoryName}</td>
                        <td>${item.price}</td>
                    </tr>
                `);
            });
            $('#total-price-by-brand').text(response.data.totalPrice);
        }).fail(function(xhr) {
            alert('Error loading cheapest prices by brand: ' + xhr.responseText);
        });
    });

    // Load category price details
    $('#category-price-details-form').submit(function(event) {
        event.preventDefault();
        const categoryName = $('#category-price-details-name').val();
        $.get(`http://localhost:20000/v1/prices/detail/categories/${categoryName}`, function(response) {
            $('#category-price-details-list').empty();
            response.data.cheapestPriceBrands.forEach(function(item) {
                $('#category-price-details-list').append(`
                    <tr>
                        <td>Cheapest</td>
                        <td>${item.brandName}</td>
                        <td>${item.price}</td>
                    </tr>
                `);
            });
            response.data.mostExpensivePriceBrands.forEach(function(item) {
                $('#category-price-details-list').append(`
                    <tr>
                        <td>Most Expensive</td>
                        <td>${item.brandName}</td>
                        <td>${item.price}</td>
                    </tr>
                `);
            });
        }).fail(function(xhr) {
            alert('Error loading category price details: ' + xhr.responseText);
        });
    });

});

function loadCategories() {
    $.get('http://localhost:20000/v1/categories', function(response) {
        $('#category-list').empty();
        $('#product-category').empty();
        response.data.forEach(function(category) {
            $('#category-list').append(`
                <tr>
                    <td>${category.id}</td>
                    <td>${category.name}</td>
                    <td>
                        <button onclick="editCategory(${category.id}, '${category.name}')">Edit</button>
                        <button onclick="deleteCategory(${category.id})">Delete</button>
                    </td>
                </tr>
            `);
            $('#product-category').append(`<option value="${category.id}">${category.name}</option>`);
        });
    }).fail(function(xhr) {
        alert('Error loading categories: ' + xhr.responseText);
    });
}

function loadBrands() {
    $.get('http://localhost:20000/v1/brands', function(response) {
        $('#brand-list').empty();
        $('#product-brand').empty();
        response.data.forEach(function(brand) {
            $('#brand-list').append(`
                <tr>
                    <td>${brand.id}</td>
                    <td>${brand.name}</td>
                    <td>
                        <button onclick="editBrand(${brand.id}, '${brand.name}')">Edit</button>
                        <button onclick="deleteBrand(${brand.id})">Delete</button>
                    </td>
                </tr>
            `);
            $('#product-brand').append(`<option value="${brand.id}">${brand.name}</option>`);
        });
    }).fail(function(xhr) {
        alert('Error loading brands: ' + xhr.responseText);
    });
}

function loadProducts() {
    $.get('http://localhost:20000/v1/sales-products', function(response) {
        $('#product-list').empty();
        response.data.forEach(function(product) {
            $('#product-list').append(`
                <tr>
                    <td>${product.id}</td>
                    <td>${product.brand.name}</td>
                    <td>${product.category.name}</td>
                    <td>${product.price}</td>
                    <td>
                        <button onclick="editProduct(${product.id}, ${product.brand.id}, ${product.category.id}, ${product.price})">Edit</button>
                        <button onclick="deleteProduct(${product.id})">Delete</button>
                    </td>
                </tr>
            `);
        });
    }).fail(function(xhr) {
        alert('Error loading products: ' + xhr.responseText);
    });
}

function editCategory(id, name) {
    $('#category-id').val(id);
    $('#category-name').val(name);
}

function deleteCategory(id) {
    $.ajax({
        url: `http://localhost:20000/v1/categories/${id}`,
        type: 'DELETE',
        success: function() {
            loadCategories();
        },
        error: function(xhr) {
            alert('Error deleting category: ' + xhr.responseText);
        }
    });
}

function editBrand(id, name) {
    $('#brand-id').val(id);
    $('#brand-name').val(name);
}

function deleteBrand(id) {
    $.ajax({
        url: `http://localhost:20000/v1/brands/${id}`,
        type: 'DELETE',
        success: function() {
            loadBrands();
        },
        error: function(xhr) {
            alert('Error deleting brand: ' + xhr.responseText);
        }
    });
}

function editProduct(id, brandId, categoryId, price) {
    $('#product-id').val(id);
    $('#product-brand').val(brandId);
    $('#product-category').val(categoryId);
    $('#product-price').val(price);
}

function deleteProduct(id) {
    $.ajax({
        url: `http://localhost:20000/v1/sales-products/${id}`,
        type: 'DELETE',
        success: function() {
            loadProducts();
        },
        error: function(xhr) {
            alert('Error deleting product: ' + xhr.responseText);
        }
    });
}
