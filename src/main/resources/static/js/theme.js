/**
 * Larch — Theme Toggle
 * Cookie-based persistence for light/dark theme.
 */
(function () {
  'use strict';

  const THEME_KEY = 'theme';
  const LIGHT = 'light';
  const DARK = 'dark';

  function getCookie(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? decodeURIComponent(match[2]) : null;
  }

  function setCookie(name, value, days) {
    const d = new Date();
    d.setTime(d.getTime() + (days || 365) * 864e5);
    document.cookie = name + '=' + encodeURIComponent(value) +
      ';expires=' + d.toUTCString() +
      ';path=/;SameSite=Lax';
  }

  function applyTheme(theme) {
    document.documentElement.setAttribute('data-bs-theme', theme);
  }

  function toggleTheme() {
    const current = document.documentElement.getAttribute('data-bs-theme') || LIGHT;
    const next = current === DARK ? LIGHT : DARK;
    applyTheme(next);
    setCookie(THEME_KEY, next);
  }

  // Initialize on load
  const saved = getCookie(THEME_KEY);
  if (saved === DARK || saved === LIGHT) {
    applyTheme(saved);
  }

  // Wire toggle button
  document.addEventListener('DOMContentLoaded', function () {
    var btn = document.getElementById('themeToggle');
    if (btn) {
      btn.addEventListener('click', toggleTheme);
    }
  });
})();
