// Shared light/dark theme toggle
(function () {
    const STORAGE_KEY = 'theme';

    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        const btn = document.getElementById('themeToggle');
        if (btn) {
            btn.textContent = theme === 'dark' ? '☀️' : '🌙';
            btn.setAttribute('aria-label',
                theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode');
        }
    }

    function getInitialTheme() {
        const saved = localStorage.getItem(STORAGE_KEY);
        if (saved) return saved;
        return 'light';
    }

    function toggleTheme() {
        const current = document.documentElement.getAttribute('data-theme') || 'light';
        const next = current === 'dark' ? 'light' : 'dark';
        localStorage.setItem(STORAGE_KEY, next);
        applyTheme(next);
    }

    // Apply ASAP to avoid flash
    applyTheme(getInitialTheme());

    document.addEventListener('DOMContentLoaded', function () {
        let btn = document.getElementById('themeToggle');
        if (!btn) {
            btn = document.createElement('button');
            btn.id = 'themeToggle';
            btn.className = 'theme-toggle';
            btn.type = 'button';
            document.body.appendChild(btn);
        }
        btn.addEventListener('click', toggleTheme);
        applyTheme(document.documentElement.getAttribute('data-theme') || 'light');
    });
})();
