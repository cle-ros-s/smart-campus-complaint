const API_BASE = window.API_BASE_URL || localStorage.getItem('API_BASE_URL') || 'http://localhost:8080/api';

function getToken() {
    return localStorage.getItem('jwt');
}

function getHeaders(auth = true) {
    const headers = { 'Content-Type': 'application/json' };
    if (auth) headers['Authorization'] = `Bearer ${getToken()}`;
    return headers;
}

async function handleResponse(res) {
    const data = await res.json().catch(() => ({}));
    if (res.status === 401) {
        if (typeof clearSession === 'function') clearSession();
        window.location.href = 'login.html?expired=true';
        throw new Error('Session expired. Please log in again.');
    }
    if (!res.ok) throw new Error(data.message || `Error: ${res.status}`);
    return data;
}

const api = {
    // Auth
    login: (email, password) =>
        fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: getHeaders(false),
            body: JSON.stringify({ email, password })
        }).then(handleResponse),

    register: (payload) =>
        fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: getHeaders(false),
            body: JSON.stringify(payload)
        }).then(handleResponse),

    getCurrentUser: () =>
        fetch(`${API_BASE}/auth/me`, {
            headers: getHeaders()
        }).then(handleResponse),

    // Complaints
    submitComplaint: (payload) =>
        fetch(`${API_BASE}/complaints`, {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify(payload)
        }).then(handleResponse),

    getCategories: () =>
        fetch(`${API_BASE}/complaints/categories`, {
            headers: getHeaders(false)
        }).then(handleResponse),

    getMyComplaints: () =>
        fetch(`${API_BASE}/complaints/my`, {
            headers: getHeaders()
        }).then(handleResponse),

    trackComplaint: (complaintNumber, trackingCode) =>
        fetch(`${API_BASE}/complaints/track?complaintNumber=${encodeURIComponent(complaintNumber)}&trackingCode=${encodeURIComponent(trackingCode)}`, {
            headers: getHeaders(false)
        }).then(handleResponse),

    getComplaintById: (id) =>
        fetch(`${API_BASE}/complaints/${id}`, {
            headers: getHeaders()
        }).then(handleResponse),

    getStaffComplaints: () =>
        fetch(`${API_BASE}/complaints`, {
            headers: getHeaders()
        }).then(handleResponse),

    updateStatus: (id, status, comment) =>
        fetch(`${API_BASE}/complaints/${id}/status`, {
            method: 'PATCH',
            headers: getHeaders(),
            body: JSON.stringify({ status, comment })
        }).then(handleResponse),

    // Admin
    getAdminStats: () =>
        fetch(`${API_BASE}/admin/stats`, { headers: getHeaders() }).then(handleResponse),

    getAllUsers: () =>
        fetch(`${API_BASE}/admin/users`, { headers: getHeaders() }).then(handleResponse),

    deleteUser: (id) =>
        fetch(`${API_BASE}/admin/users/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        }).then(handleResponse),

    changeUserRole: (id, role) =>
        fetch(`${API_BASE}/admin/users/${id}/role`, {
            method: 'PATCH',
            headers: getHeaders(),
            body: JSON.stringify({ role })
        }).then(handleResponse),

    // Attachments
    getAttachments: (complaintId) =>
        fetch(`${API_BASE}/attachments/complaint/${complaintId}`, {
            headers: getHeaders()
        }).then(handleResponse),
};
