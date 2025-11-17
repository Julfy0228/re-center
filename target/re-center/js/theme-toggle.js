// Toggle theme script: toggles class 'theme-dark' on <html> and persists choice in localStorage
(function(){
    const KEY = 'site-theme'; // 'dark' or 'light'
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

    // init on DOMContentLoaded
    document.addEventListener('DOMContentLoaded', function(){
        const btn = document.getElementById(toggleId);
        if (!btn) return;

        // set initial from storage or from prefers-color-scheme
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
