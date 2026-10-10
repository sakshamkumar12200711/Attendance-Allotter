
const API_BASE = "http://localhost:8080/api/v1";

const loginSection = document.getElementById("loginSection");
const dashboardSection = document.getElementById("dashboardSection");
const loginForm = document.querySelector(".login-form");
const nameSpan =
    document.getElementById("facultyName") ||
    document.getElementById("studentName");

const role = loginForm.id === "facultyLoginForm" ? "faculty" : "student";
const SESSION_KEY = "loggedIn_" + loginForm.id;

function showDashboard(userId) {
    loginSection.classList.add("hidden");
    dashboardSection.classList.remove("hidden");
    if (nameSpan && userId) nameSpan.innerText = userId;
}

loginForm.addEventListener("submit", async (e) => {
    e.preventDefault(); 

    const userId = loginForm.querySelector('input[type="text"]').value.trim();
    const passwordInput = loginForm.querySelector('input[type="password"]');
    const password = passwordInput.value;  

    if (userId === "" || password === "") {
        alert("please enter your id and password");
        return;
    }

    if (role === "student" && isNaN(userId)) {
        alert("Student ID must be a number, for example 351");
        return;
    }

    const btn = loginForm.querySelector('button[type="submit"]');
    btn.disabled = true;                 

    try {
       
        const res = await fetch(API_BASE + "/" + role + "/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: userId, password: password })
        });

        if (res.status === 401) {
            alert("Invalid ID or password");
            passwordInput.value = "";
            return;
        }
        if (!res.ok) throw new Error("bad response");

        const data = await res.json();
        if (!data.success) {
            alert(data.message || "Login failed");
            return;
        }

        try {
            sessionStorage.clear();                     
            sessionStorage.setItem(SESSION_KEY, userId);
            sessionStorage.setItem("role", role);      
            sessionStorage.setItem("userId", userId);
        } catch (err) {}

        passwordInput.value = "";
        showDashboard(userId);
    } catch (err) {
        alert("Could not reach the server. Is the backend running?");
    } finally {
        btn.disabled = false;
    }
});

try {
    const saved = sessionStorage.getItem(SESSION_KEY);
    if (saved && sessionStorage.getItem("role") === role) showDashboard(saved);
} catch (err) {}