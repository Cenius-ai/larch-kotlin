/**
 * Larch — Dashboard allocation pie chart
 * Consumes /api/allocation returning [{symbol, name, valueUsd, percentage}]
 */
(function () {
  'use strict';

  var CHART_COLORS = [
    '#d33a3e', '#e05a3e', '#c7463c', '#b84050', '#e8734a',
    '#d94f3a', '#c25540', '#e6885a', '#bf4a44', '#d46240'
  ];

  function formatUsd(val) {
    var n = Number(val);
    if (isNaN(n)) return '$0.00';
    return n.toLocaleString('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 2 });
  }

  function buildBreakdownList(data, colors) {
    var list = document.getElementById('allocationList');
    if (!list) return;
    list.innerHTML = '';
    data.forEach(function (entry, i) {
      var div = document.createElement('div');
      div.className = 'allocation-item d-flex align-items-center gap-3 py-2';
      div.innerHTML =
        '<span class="allocation-dot" style="background:' + colors[i % colors.length] + '"></span>' +
        '<div class="flex-grow-1">' +
          '<span class="fw-semibold">' + escapeHtml(entry.symbol) + '</span>' +
          '<span class="text-secondary ms-2">' + escapeHtml(entry.name) + '</span>' +
        '</div>' +
        '<div class="text-end">' +
          '<span class="fw-semibold">' + formatUsd(entry.valueUsd) + '</span>' +
          '<span class="text-secondary ms-2">' + entry.percentage + '%</span>' +
        '</div>';
      list.appendChild(div);
    });
  }

  function escapeHtml(str) {
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(str));
    return div.innerHTML;
  }

  document.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('allocationChart');
    if (!canvas) return;

    fetch('/api/allocation')
      .then(function (res) {
        if (!res.ok) throw new Error('Failed to load allocation data');
        return res.json();
      })
      .then(function (data) {
        if (!data || data.length === 0) {
          var container = canvas.parentElement;
          if (container) {
            container.innerHTML = '<p class="text-center text-secondary py-5">No holdings to display.</p>';
          }
          return;
        }

        var colors = data.map(function (_, i) { return CHART_COLORS[i % CHART_COLORS.length]; });
        var labels = data.map(function (d) { return d.symbol; });
        var values = data.map(function (d) { return Number(d.valueUsd); });
        var percentages = data.map(function (d) { return d.percentage; });

        buildBreakdownList(data, colors);

        var ctx = canvas.getContext('2d');
        new Chart(ctx, {
          type: 'pie',
          data: {
            labels: labels,
            datasets: [{
              data: values,
              backgroundColor: colors,
              borderColor: getComputedStyle(document.documentElement)
                .getPropertyValue('--surface-card').trim() || '#faf7f2',
              borderWidth: 3
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
              legend: { display: false },
              tooltip: {
                callbacks: {
                  label: function (ctx) {
                    var pct = percentages[ctx.dataIndex];
                    return labels[ctx.dataIndex] + ': ' + formatUsd(ctx.raw) + ' (' + pct + '%)';
                  }
                }
              }
            }
          }
        });
      })
      .catch(function (err) {
        console.error('Chart load error:', err);
        var container = canvas.parentElement;
        if (container) {
          container.innerHTML = '<p class="text-center text-danger py-5">Could not load chart data. <button onclick="location.reload()" class="btn btn-sm btn-outline-secondary">Retry</button></p>';
        }
      });
  });
})();
