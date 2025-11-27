document.addEventListener("DOMContentLoaded", () => {
  const currentStatus = document.querySelector("input[name='status']:checked")?.value;

  const rec = document.querySelector("input[value='received']");
  const pro = document.querySelector("input[value='processing']");
  const app = document.querySelector("input[value='approved']");
  const rej = document.querySelector("input[value='rejected']");

  if (currentStatus === "received") {
    rec.disabled = false;
    pro.disabled = false;
    app.disabled = true;
    rej.disabled = true;
  }

  if (currentStatus === "processing") {
    rec.disabled = true;
    pro.disabled = false;
    app.disabled = false;
    rej.disabled = false;
  }

  if (currentStatus === "approved" || currentStatus === "rejected") {
    rec.disabled = true;
    pro.disabled = true;
    app.disabled = true;
    rej.disabled = true;
  }
});
