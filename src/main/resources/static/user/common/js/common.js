// Shared responsive header behavior for both DAKS and AI x SOFTWAVE.
(() => {
  const menuButton = document.querySelector('.menu-toggle');
  const mobileMenu = document.querySelector('#mobile-menu');
  if (!menuButton || !mobileMenu) return;
  const desktopBreakpoint = window.matchMedia('(min-width: 1024px)');
  let previousFocus = null;

  function setMenu(open, restoreFocus = false) {
    mobileMenu.hidden = !open;
    document.body.classList.toggle('menu-open', open);
    menuButton.setAttribute('aria-expanded', String(open));
    menuButton.setAttribute('aria-label', open ? '메뉴 닫기' : '메뉴 열기');
    const main = document.querySelector('main');
    const footer = document.querySelector('footer');
    if (main) main.inert = open;
    if (footer) footer.inert = open;
    if (open) {
      previousFocus = document.activeElement;
      mobileMenu.querySelector('a')?.focus();
    } else if (restoreFocus) {
      previousFocus?.focus();
    }
  }

  menuButton.addEventListener('click', () => setMenu(mobileMenu.hidden, !mobileMenu.hidden));
  mobileMenu.querySelectorAll('a').forEach(link => link.addEventListener('click', () => {
    setMenu(false);
    const hash = link.hash;
    if (!hash) return;
    // Only focus a target on this page; cross-page links navigate as usual.
    const target = document.getElementById(decodeURIComponent(hash.slice(1)));
    if (target && new URL(link.href).pathname === location.pathname) {
      target.setAttribute('tabindex', '-1');
      target.focus({ preventScroll: true });
    }
  }));
  document.addEventListener('keydown', event => {
    if (mobileMenu.hidden) return;
    if (event.key === 'Escape') { setMenu(false, true); return; }
    if (event.key !== 'Tab') return;
    const focusable = [menuButton, ...mobileMenu.querySelectorAll('a')];
    const first = focusable[0], last = focusable[focusable.length - 1];
    if (event.shiftKey && document.activeElement === first) {
      event.preventDefault(); last.focus();
    } else if (!event.shiftKey && document.activeElement === last) {
      event.preventDefault(); first.focus();
    }
  });
  desktopBreakpoint.addEventListener('change', event => {
    if (event.matches) setMenu(false);
  });
  // On original AI page map-link was optional; guard for absence.
  document.querySelector('.map-link')?.addEventListener('click', () => {
    const map = document.querySelector('#coex-map');
    map?.focus({ preventScroll: true });
    const venue = document.querySelector('#venue-map');
    venue?.scrollIntoView({ block: 'center', behavior: window.matchMedia('(prefers-reduced-motion: reduce)').matches ? 'instant' : 'smooth' });
  });
})();
