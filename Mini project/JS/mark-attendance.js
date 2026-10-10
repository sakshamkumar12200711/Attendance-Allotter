try {
    if (sessionStorage.getItem("role") !== "faculty") {
        window.location.replace("faculty-dashboard.html");
    }
} catch (err) {}

const API = "http://localhost:8080/api/v1/attendance";

let getcalendar = document.getElementById("cal");
let currDate = null;
let currSubject = null;
let currStudent = null;
let h2 = document.getElementById("selectedDate");

let studentId = document.getElementById("studentId");
let statussub = document.getElementById("status");
let sublist = document.querySelector(".sublist");
let subbtn = document.querySelector("#addSubject");
let select_sub = document.getElementById("selectedSubject");
let curr_inp = null;
let attendance = {};   
let pres = document.getElementById("present");
let abs = document.getElementById("absent");
let total = document.getElementById("total");
let percen = document.getElementById("percentage");

let presentBtn = document.getElementById("presentBtn");
let absentBtn = document.getElementById("absentBtn");
let unmarkBtn = document.getElementById("unmarkBtn");

function makeKey() {
    return currStudent + "_" + currDate + "_" + currSubject;
}
function ready() {
    const typed = studentId.value.trim();
    if (typed === "") {
        alert("please enter student id");
        return false;
    }
    if (typed !== currStudent) {
        alert("Press Enter in the student ID box to load this student first");
        return false;
    }
    if (currDate == null || currSubject == null) {
        alert("please select date and subject");
        return false;
    }
    return true;
}
function update_status() {
    if (!currStudent || !currDate || !currSubject) return;
    let st = attendance[makeKey()];
    statussub.innerText = st ? st.toUpperCase() : "NOT MARKED";
}

const update_attend = () => {
    let p = 0;
    let a = 0;
    for (let key in attendance) {
        if (key.startsWith(currStudent + "_")) {
            if (attendance[key] == "present") p++;
            else if (attendance[key] == "absent") a++;
        }
    }
    pres.innerText = p;
    abs.innerText = a;
    let t = p + a;
    total.innerText = t;
    let percent = t > 0 ? (p / t) * 100 : 0;
    percen.innerText = percent.toFixed(2) + "%";
};
function addSubjectToList(name) {
    name = name.trim();
    if (name === "") return;
    for (const p of sublist.querySelectorAll("p")) {
        if (p.innerText.toLowerCase() === name.toLowerCase()) return;
    }
    let para = document.createElement("p");
    para.innerText = name;
    para.addEventListener("click", () => {
        sublist.querySelectorAll("p").forEach(x => x.classList.remove("active"));
        para.classList.add("active");
        select_sub.innerText = name;
        currSubject = name;
        update_status();
        update_attend();
    });
    sublist.appendChild(para);
}

subbtn.addEventListener("click", () => {
    if (curr_inp != null) return;
    let inp = document.createElement("input");
    inp.type = "text";
    inp.placeholder = "enter the subject name:";
    sublist.append(inp);
    curr_inp = inp;
    inp.addEventListener("keydown", (e) => {
        if (e.key == "Enter") {
            if (inp.value.trim() === "") return;
            addSubjectToList(inp.value);
            inp.remove();
            curr_inp = null;
        }
    });
});
async function loadStudent() {
    for (let key in attendance) {
        if (key.startsWith(currStudent + "_")) delete attendance[key];
    }
    try {
        const res = await fetch(API + "?studentId=" + encodeURIComponent(currStudent));
        if (!res.ok) throw new Error("bad response");
        const rows = await res.json();
        rows.forEach(r => {
            attendance[r.record] = r.status.toLowerCase();
            addSubjectToList(r.subject);     
        });
    } catch (e) {
        alert("Could not load attendance. Is the backend running?");
    }
}
async function setStatus(status) {          
    if (!ready()) return;
    const key = makeKey();
    try {
        const res = await fetch(API + "/mark", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ records: [key], status: status })
        });
        if (!res.ok) throw new Error("bad response");
        const data = await res.json();
        if (data.saved + data.updated === 0) {
            alert("Not saved. Check that the student ID exists.");
            return;
        }
        attendance[key] = status.toLowerCase();
        statussub.innerText = status;
        update_attend();
    } catch (e) {
        alert("Could not reach the server.");
    }
}

async function unmark() {
    if (!ready()) return;
    const key = makeKey();
    try {
        const res = await fetch(API + "?record=" + encodeURIComponent(key), {
            method: "DELETE"
        });
        if (!res.ok) throw new Error("bad response");
        delete attendance[key];
        statussub.innerText = "NOT MARKED";
        update_attend();
    } catch (e) {
        alert("Could not reach the server.");
    }
}
let calendar = new FullCalendar.Calendar(getcalendar, {
    initialView: "dayGridMonth",
    dateClick: function (info) {
        h2.innerText = `SELECTED DATE: ${info.dateStr}`;
        currDate = info.dateStr;
        update_status();
        update_attend();
    }
});
calendar.render();
presentBtn.addEventListener("click", () => setStatus("PRESENT"));
absentBtn.addEventListener("click", () => setStatus("ABSENT"));
unmarkBtn.addEventListener("click", unmark);

studentId.addEventListener("keydown", async (e) => {
    if (e.key == "Enter") {
        currStudent = studentId.value.trim();
        if (currStudent === "") return;
        await loadStudent();
        update_status();
        update_attend();
    }
});

document.getElementById("logout").addEventListener("click", () => {
    window.location.href = "logout.html";   // logout.html clears the session
});