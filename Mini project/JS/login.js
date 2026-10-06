// Works for BOTH faculty-dashboard.html and student-dashboard.html
const loginSection = document.getElementById("loginSection");
const dashboardSection = document.getElementById("dashboardSection");
const loginForm = document.querySelector(".login-form");
const nameSpan =
    document.getElementById("facultyName") ||
    document.getElementById("studentName");

const SESSION_KEY = "loggedIn_" + loginForm.id; // separate key for faculty / student

function showDashboard(userId) {
    loginSection.classList.add("hidden");
    dashboardSection.classList.remove("hidden");
    if (nameSpan && userId) nameSpan.innerText = userId;
}

loginForm.addEventListener("submit", (e) => {
    e.preventDefault(); // stop the page from reloading

    const userId = loginForm.querySelector('input[type="text"]').value.trim();
    const password = loginForm.querySelector('input[type="password"]').value.trim();

    if (userId === "" || password === "") {
        alert("please enter your id and password");
        return;
    }

    try { sessionStorage.setItem(SESSION_KEY, userId); } catch (err) {}
    showDashboard(userId);
});

// If already logged in this session (e.g. coming back from another page), skip the login form
try {
    const saved = sessionStorage.getItem(SESSION_KEY);
    if (saved) showDashboard(saved);
} catch (err) {}
