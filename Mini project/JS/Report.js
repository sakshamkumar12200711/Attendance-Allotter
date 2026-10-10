
const API = "http://localhost:8080/api/v1/attendance";

const overallSpan = document.getElementById("overallPercentage");
const tbody = document.getElementById("reportBody");
const studentLabel = document.getElementById("studentLabel");
const subjectList = document.getElementById("subjectSummary");
const message = document.getElementById("message");


let role = null;
let userId = null;
try {
    role = sessionStorage.getItem("role");
    userId = sessionStorage.getItem("userId");
} catch (err) {}

let studentId = null;
if (role === "student") {
    studentId = userId;                         
} else if (role === "faculty") {
    // faculty: reports.html?studentId=351  (or type it when asked)
    studentId = new URLSearchParams(window.location.search).get("studentId")
        || prompt("Enter student ID");
}
if (!role || !userId) {
    window.location.replace("student-dashboard.html");  
} else if (!studentId || studentId.trim() === "") {
    showMessage("No student selected.");
} else {
    studentId = studentId.trim();
    studentLabel.textContent = studentId;
    loadReport();
}
function showMessage(text) {
    if (message) message.textContent = text;
    else alert(text);
}
async function loadReport() {
    try {
        const res = await fetch(API + "?studentId=" + encodeURIComponent(studentId));
        if (!res.ok) throw new Error("bad response");
        const rows = await res.json();         
        render(rows);
    } catch (e) {
        showMessage("Could not load attendance. Is the backend running?");
    }
}
function percent(present, total) {
    return total > 0 ? ((present / total) * 100).toFixed(2) + "%" : "--%";
}
function render(rows) {
    tbody.innerHTML = "";
    if (subjectList) subjectList.innerHTML = "";

    if (rows.length === 0) {
        const tr = document.createElement("tr");
        const td = document.createElement("td");
        td.colSpan = 3;
        td.textContent = "No attendance marked yet";
        tr.appendChild(td);
        tbody.appendChild(tr);
        overallSpan.textContent = "--%";
        showMessage("");
        return;
    }
    let present = 0;
    let total = 0;
    const perSubject = {};                      
    rows.forEach(r => {
        
        const tr = document.createElement("tr");
        const dateTd = document.createElement("td");
        const subjectTd = document.createElement("td");
        const statusTd = document.createElement("td");
        dateTd.textContent = r.date;
        subjectTd.textContent = r.subject;
        statusTd.textContent = r.status;
        statusTd.className = r.status === "PRESENT" ? "status-present" : "status-absent";
        tr.append(dateTd, subjectTd, statusTd);
        tbody.appendChild(tr);

        // counts
        const key = r.subject.toLowerCase();
        if (!perSubject[key]) perSubject[key] = { name: r.subject, present: 0, total: 0 };
        total++;
        perSubject[key].total++;
        if (r.status === "PRESENT") {
            present++;
            perSubject[key].present++;
        }
    });
    overallSpan.textContent = percent(present, total) + "  (" + present + " / " + total + ")";
    if (subjectList) {
        for (const key in perSubject) {
            const s = perSubject[key];
            const li = document.createElement("li");
            li.textContent = s.name + ": " + percent(s.present, s.total)
                + " (" + s.present + " / " + s.total + ")";
            subjectList.appendChild(li);
        }
    }
    showMessage("");
}
const dashLink = document.getElementById("dashLink");
if (dashLink) {
    dashLink.href = role === "faculty" ? "faculty-dashboard.html" : "student-dashboard.html";
}
const logoutBtn = document.getElementById("logout");
if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
        window.location.href = "logout.html";  
    });
}