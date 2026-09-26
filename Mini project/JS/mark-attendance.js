let getcalendar = document.getElementById("cal");
let currDate=null;
let currSubject=null;
let h2 = document.getElementById("selectedDate");
let calendar = new FullCalendar.Calendar(getcalendar, {
    initialView: "dayGridMonth",
    dateClick: function(info) {
        console.log(info.dateStr);
        h2.innerText=`SELECTED DATE:${info.dateStr}`;
        currDate=info.dateStr;
        update_status(currDate + "_" + currSubject);
        update_attend();
    }
});

calendar.render();

let statussub=document.getElementById("status")
let sublist = document.querySelector(".sublist");
let subbtn = document.querySelector("#addSubject");
let select_sub=document.getElementById("selectedSubject");
let curr_inp = null;
let attendance={};

subbtn.addEventListener("click", () => {
    if (curr_inp != null) return;
    let inp = document.createElement("input");
    inp.type = "text";
    inp.placeholder = "enter the subject name:";
    sublist.append(inp);
    curr_inp = inp;
    inp.addEventListener("keydown", (e) => {
        if (e.key == "Enter") {
            let name = inp.value;
            if (name.trim() === "") { return; }
            let para = document.createElement("p");
            para.innerText = name;
            para.addEventListener("click",()=>{
                select_sub.innerText=name;
                currSubject=name;
                update_status(currDate+"_"+name);
                update_attend();
            })
            sublist.appendChild(para);
            inp.remove();
            curr_inp = null;
        }
    })
})

let presentBtn = document.getElementById("presentBtn");
let absentBtn = document.getElementById("absentBtn");
let unmarkBtn = document.getElementById("unmarkBtn");

presentBtn.addEventListener("click",()=>{
    let key=currDate+"_"+currSubject;
    statussub.innerText="PRESENT";
    attendance[key]="present";
    update_attend();
})

absentBtn.addEventListener("click",()=>{
    let key=currDate+"_"+currSubject;
    statussub.innerText="ABSENT";
    attendance[key]="absent";
    update_attend();
})

unmarkBtn.addEventListener("click",()=>{
    let key=currDate+"_"+currSubject;
    delete attendance[key];
    statussub.innerText = "NOT MARKED";
    update_attend();
})

function update_status(key){
  if(attendance[key])
  {
    statussub.innerText=attendance[key];
  }
  else {
        statussub.innerText = "NOT MARKED";
    }
}

let pres=document.getElementById("present");
let abs=document.getElementById("absent");
let total=document.getElementById("total");
let percen=document.getElementById("percentage");

const update_attend=()=>{
    let p=0,a=0;
    for(let i of Object.values(attendance))
    {
        if(i=="present")
        {
            p++;
        }
        else if(i=="absent")
        {
            a++;
        }
    }
    pres.innerText=p;
    abs.innerText=a;
    let t=p+a;
    total.innerText=t;
   if(t>0)
   {
    percent=(p/t)*100;
   }
   percen.innerText = percent.toFixed(2) + "%";
}
