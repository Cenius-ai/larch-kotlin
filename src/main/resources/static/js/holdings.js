/**
 * Larch — Holdings page: delete functionality
 */
(function () {
  'use strict';

  document.addEventListener('DOMContentLoaded', function () {
    var deleteButtons = document.querySelectorAll('.delete-holding');
    deleteButtons.forEach(function (btn) {
      btn.addEventListener('click', function () {
        var id = btn.getAttribute('data-id');
        if (!id) return;

        if (!confirm('Remove this holding from your portfolio?')) return;

        btn.disabled = true;
        btn.textContent = 'Deleting…';

        fetch('/api/holdings/' + id + '/delete', { method: 'POST' })
          .then(function (res) {
            if (!res.ok) throw new Error('Delete failed');
            return res.json();
          })
          .then(function (data) {
            if (data.success) {
              // Remove the row
              var row = btn.closest('tr');
              if (row) row.remove();

              // Update total value
              updateTotalValue();
            } else {
              alert('Failed to delete: ' + (data.message || 'Unknown error'));
              btn.disabled = false;
              btn.textContent = 'Delete';
            }
          })
          .catch(function (err) {
            console.error('Delete error:', err);
            alert('Failed to delete holding. Please try again.');
            btn.disabled = false;
            btn.textContent = 'Delete';
          });
      });
    });
  });

  function updateTotalValue() {
    var rows = document.querySelectorAll('#holdingsBody tr');
    var total = 0;
    rows.forEach(function (row) {
      // Value column is 5th (0-indexed: 4)
      var cells = row.querySelectorAll('td');
      if (cells.length >= 5) {
        var valText = cells[4].textContent.replace(/[^0-9.\-]/g, '');
        var val = parseFloat(valText);
        if (!isNaN(val)) total += val;
      }
    });

    var totalEl = document.querySelector('.total-number');
    if (totalEl) {
      totalEl.textContent = total.toLocaleString('en-US', {
        style: 'currency', currency: 'USD', minimumFractionDigits: 2
      });
    }

    // Show empty state if no rows left
    if (rows.length === 0) {
      var tableResponsive = document.querySelector('.table-responsive');
      var emptyState = document.querySelector('.empty-state');
      if (tableResponsive) tableResponsive.style.display = 'none';
      if (emptyState) emptyState.style.display = 'block';
    }
  }
})();
