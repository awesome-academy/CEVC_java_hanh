
function confirmDelete(button) {
  const msg = button.dataset.msg;
  const departmentId = button.dataset.departmentId;
  if (confirm(msg)) {
    document.getElementById('deleteForm-' + departmentId).submit();
  }
}
