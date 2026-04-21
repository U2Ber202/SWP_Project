(function () {
    const storageKey = 'theme';
    const defaultTheme = 'dark';

    function getTheme() {
        const savedTheme = localStorage.getItem(storageKey);
        return savedTheme === 'light' || savedTheme === 'dark' ? savedTheme : defaultTheme;
    }

    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        updateToggleIcon(theme);
    }

    function toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme') || getTheme();
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        localStorage.setItem(storageKey, newTheme);
        applyTheme(newTheme);
    }

    function bindToggle() {
        document.addEventListener('click', function (event) {
            const toggleBtn = event.target.closest('#theme-toggle');
            if (!toggleBtn) {
                return;
            }
            event.preventDefault();
            toggleTheme();
        });
        updateToggleIcon(getTheme());
    }

    function updateToggleIcon(theme) {
        const icons = document.querySelectorAll('#theme-toggle i');
        icons.forEach(function (icon) {
            icon.className = theme === 'light' ? 'fas fa-sun' : 'fas fa-moon';
        });
    }

    window.updateToggleIcon = updateToggleIcon;
    window.toggleTheme = toggleTheme;
    applyTheme(getTheme());

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', bindToggle);
    } else {
        bindToggle();
    }
})();
