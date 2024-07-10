$(document).ready(function() {
    loadBrands();

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

});

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
