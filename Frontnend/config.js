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

// Global background video and aesthetics injection
document.addEventListener('DOMContentLoaded', () => {
    // 1. Add background video and blur overlay if it's not already in the HTML
    if (!document.querySelector('.bg-video')) {
        const video = document.createElement('video');
        video.autoplay = true;
        video.muted = true;
        video.loop = true;
        video.playsInline = true;
        video.className = 'bg-video';
        
        const source = document.createElement('source');
        source.src = 'video/168572-839370257_medium.mp4';
        source.type = 'video/mp4';
        video.appendChild(source);
        
        const overlay = document.createElement('div');
        overlay.className = 'video-overlay';
        
        // Insert at the beginning of the body
        if (document.body.firstChild) {
            document.body.insertBefore(overlay, document.body.firstChild);
            document.body.insertBefore(video, document.body.firstChild);
        } else {
            document.body.appendChild(video);
            document.body.appendChild(overlay);
        }
    }

    // 2. Slow down the video playback (either existed or freshly injected)
    const videoElem = document.querySelector('.bg-video');
    if (videoElem) {
        videoElem.playbackRate = 0.5; // slow down to 50% speed
    }
});
