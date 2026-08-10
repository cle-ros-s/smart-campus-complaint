document.addEventListener('DOMContentLoaded', () => {
    const themeToggleBtn = document.getElementById('theme-toggle');
    const currentTheme = localStorage.getItem('theme') || 'light';

    const updateToggleBtn = (isDark) => {
        if (!themeToggleBtn) return;
        const iconSpan = themeToggleBtn.querySelector('.nav-icon');
        if (iconSpan) {
            iconSpan.textContent = isDark ? '☀️' : '🌙';
        } else {
            themeToggleBtn.textContent = isDark ? '☀️' : '🌙';
        }
    };

    if (currentTheme === 'dark') {
        document.documentElement.classList.add('dark');
        updateToggleBtn(true);
    }

    if(themeToggleBtn) {
        themeToggleBtn.removeAttribute('onclick');
        themeToggleBtn.addEventListener('click', () => {
            document.documentElement.classList.toggle('dark');
            const isDark = document.documentElement.classList.contains('dark');
            localStorage.setItem('theme', isDark ? 'dark' : 'light');
            updateToggleBtn(isDark);
        });
    }
});
