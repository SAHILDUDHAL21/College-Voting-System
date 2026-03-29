// config.js - Global configuration for the frontend
const BASE_URL = "http://localhost:8000";

function checkAuth(requiredRole) {
    const role = localStorage.getItem("userRole");
    if (!role) {
        window.location.href = "login.html";
        return;
    }
    if (requiredRole && role !== requiredRole) {
        // Redirect to their respective dashboard if they are on the wrong page
        if (role === 'admin') window.location.href = "admin_dashboard.html";
        else if (role === 'teacher') window.location.href = "teacher_dashboard.html";
        else if (role === 'student') window.location.href = "student_dashboard.html";
        else window.location.href = "login.html";
    }
}
