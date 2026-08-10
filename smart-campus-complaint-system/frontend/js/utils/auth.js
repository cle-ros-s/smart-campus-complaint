// Auth helpers
function isLoggedIn() {
    return !!localStorage.getItem('jwt');
}

function getUser() {
    const u = localStorage.getItem('user');
    return u ? JSON.parse(u) : null;
}

function saveSession(data) {
    localStorage.setItem('jwt', data.token);
    localStorage.setItem('user', JSON.stringify({ id: data.id, email: data.email, roles: data.roles }));
}

function clearSession() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user');
}

function getDashboardForUser(user) {
    if (!user || !user.roles) return 'login.html';
    if (user.roles.includes('ROLE_ADMIN')) return 'admin-dashboard.html';
    if (user.roles.includes('ROLE_STAFF') || user.roles.includes('ROLE_DEPARTMENT_HEAD') || user.roles.includes('ROLE_SAFETY_OFFICER')) return 'staff-dashboard.html';
    return 'student-dashboard.html';
}

function redirectIfNotLoggedIn() {
    if (!isLoggedIn()) {
        window.location.href = 'login.html';
    }
}

function redirectIfLoggedIn() {
    if (isLoggedIn()) {
        const user = getUser();
        window.location.href = getDashboardForUser(user);
    }
}

// Toast notification
function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.cssText = 'position:fixed;top:1.5rem;right:1.5rem;z-index:9999;display:flex;flex-direction:column;gap:0.5rem;';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.style.cssText = `padding:1rem 1.5rem;border-radius:8px;color:#fff;font-weight:500;font-size:0.9rem;box-shadow:0 4px 12px rgba(0,0,0,0.2);animation:slideIn 0.3s ease;max-width:320px;background:${type === 'success' ? '#10B981' : type === 'error' ? '#EF4444' : '#3B82F6'};`;
    toast.textContent = message;
    container.appendChild(toast);
    setTimeout(() => { toast.remove(); }, 3500);
}

// Status badge helper
function statusBadge(status) {
    const colors = {
        PENDING: '#F59E0B',
        IN_PROGRESS: '#3B82F6',
        RESOLVED: '#10B981',
        CLOSED: '#6B7280',
        REJECTED: '#EF4444',
        CRITICAL: '#EF4444'
    };
    const color = colors[status] || '#6B7280';
    return `<span style="background:${color}22;color:${color};padding:0.2rem 0.75rem;border-radius:999px;font-size:0.78rem;font-weight:600;">${status?.replace('_', ' ')}</span>`;
}

// ─── Dynamic Profile Modal Listener ──────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    if (!isLoggedIn()) return;

    const findProfileLinks = () => {
        const links = Array.from(document.querySelectorAll('a, button')).filter(el => {
            const txt = el.textContent || '';
            return txt.includes('Profile');
        });
        const adminProfileBox = document.querySelector('.admin-profile');
        if (adminProfileBox) {
            adminProfileBox.style.cursor = 'pointer';
            links.push(adminProfileBox);
        }
        return links;
    };

    const profileLinks = findProfileLinks();
    if (profileLinks.length === 0) return;

    profileLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();
            openProfileModal();
        });
    });

    function openProfileModal() {
        let modal = document.getElementById('profile-modal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'profile-modal';
            modal.className = 'modal-overlay';
            modal.innerHTML = `
                <div class="modal-box" style="max-width: 480px; margin: auto;">
                    <div class="modal-head" style="background:var(--grad-primary);color:#fff;display:flex;justify-content:space-between;align-items:center;padding:1.25rem 1.5rem;">
                        <h3 style="margin:0;font-size:1.1rem;font-weight:700;">👤 User Profile</h3>
                        <button class="modal-close" id="close-profile-modal" style="background:rgba(255,255,255,0.2);border:none;color:#fff;width:30px;height:30px;border-radius:50%;cursor:pointer;font-size:1rem;display:flex;align-items:center;justify-content:center;">✕</button>
                    </div>
                    <div class="modal-body" id="profile-modal-body" style="padding:1.75rem;">
                        <div class="loading-row">Loading profile details...</div>
                    </div>
                    <div class="modal-foot" style="padding:1rem 1.75rem;border-top:1px solid var(--border);display:flex;gap:0.75rem;justify-content:flex-end;background:var(--surface-2);">
                        <button class="btn btn-secondary" id="btn-close-profile">Close</button>
                    </div>
                </div>
            `;
            document.body.appendChild(modal);

            document.getElementById('close-profile-modal').addEventListener('click', closeProfileModal);
            document.getElementById('btn-close-profile').addEventListener('click', closeProfileModal);
        }

        modal.classList.add('open');
        const bodyContainer = document.getElementById('profile-modal-body');
        bodyContainer.innerHTML = '<div class="loading-row" style="text-align:center;padding:2rem 0;color:var(--text-muted);"><span class="spinner"></span> Loading profile...</div>';

        if (typeof api !== 'undefined' && api.getCurrentUser) {
            api.getCurrentUser().then(data => {
                let roleName = (data.role || 'STUDENT').replace('ROLE_', '');
                let detailsHtml = `
                    <div style="text-align: center; margin-bottom: 1.8rem;">
                        <div style="width: 76px; height: 76px; border-radius: 50%; background: var(--primary); color: #fff; font-size: 2.2rem; font-weight: 800; display: flex; align-items: center; justify-content: center; margin: 0 auto 0.75rem; box-shadow: var(--shadow-sm);">
                            ${(data.name || data.email || 'U')[0].toUpperCase()}
                        </div>
                        <h4 style="font-size: 1.25rem; font-weight: 800; color: var(--text-main); margin-bottom: 0.35rem;">${data.name || 'User'}</h4>
                        <span class="role-badge role-${data.role || 'STUDENT'}">${roleName.replace(/_/g, ' ')}</span>
                    </div>
                    <div class="detail-grid">
                        <div class="detail-item"><div class="dl">Email Address</div><div class="dv" style="word-break:break-all;">${data.email || '—'}</div></div>
                        <div class="detail-item"><div class="dl">Phone Number</div><div class="dv">${data.phoneNumber || '—'}</div></div>
                `;

                if (data.role === 'ROLE_STUDENT' || data.collegeId) {
                    detailsHtml += `
                        <div class="detail-item"><div class="dl">College ID</div><div class="dv">${data.collegeId || '—'}</div></div>
                        <div class="detail-item"><div class="dl">Year / Section</div><div class="dv">${data.year ? data.year + ' Year' : '—'} ${data.section ? ' (Sec ' + data.section + ')' : ''}</div></div>
                    `;
                }

                if (data.department) {
                    detailsHtml += `
                        <div class="detail-item" style="grid-column: 1 / -1;"><div class="dl">Department</div><div class="dv">${data.department}</div></div>
                    `;
                }

                detailsHtml += `</div>`;
                bodyContainer.innerHTML = detailsHtml;
            }).catch(err => {
                bodyContainer.innerHTML = `<div style="color:var(--danger);text-align:center;padding:1rem;">⚠️ Failed to load profile: ${err.message || 'Error'}</div>`;
            });
        } else {
            bodyContainer.innerHTML = '<div style="color:var(--danger);text-align:center;padding:1rem;">⚠️ API Service unavailable.</div>';
        }
    }

    function closeProfileModal() {
        const modal = document.getElementById('profile-modal');
        if (modal) modal.classList.remove('open');
    }
});
