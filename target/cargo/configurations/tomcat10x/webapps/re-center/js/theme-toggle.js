(function(){
    const KEY = 'site-theme';
    const toggleId = 'theme-toggle';

    function applyTheme(theme) {
        if (theme === 'dark') document.documentElement.classList.add('theme-dark');
        else document.documentElement.classList.remove('theme-dark');
    }

    function getStored() {
        try { return localStorage.getItem(KEY); } catch(e){ return null; }
    }

    function store(theme) {
        try { localStorage.setItem(KEY, theme); } catch(e){}
    }

    document.addEventListener('DOMContentLoaded', function(){
        const btn = document.getElementById(toggleId);
        if (!btn) return;

        let theme = getStored();
        if (!theme) {
            const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
            theme = prefersDark ? 'dark' : 'light';
        }
        applyTheme(theme);
        btn.textContent = theme === 'dark' ? '☀️' : '🌙';

        btn.addEventListener('click', function(){
            const isDark = document.documentElement.classList.toggle('theme-dark');
            const newTheme = isDark ? 'dark' : 'light';
            store(newTheme);
            btn.textContent = newTheme === 'dark' ? '☀️' : '🌙';
        });
    });
})();
