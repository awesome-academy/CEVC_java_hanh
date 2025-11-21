
function confirmDelete(button) {
  const msg = button.dataset.msg;
  const userId = button.dataset.userId;
  if (confirm(msg)) {
    document.getElementById('deleteForm-' + userId).submit();
  }
}

function confirmChangeStatus(button) {
  const msg = button.dataset.msg;
  const userId = button.dataset.userId;
  if (confirm(msg)) {
    document.getElementById('changeStatusForm-' + userId).submit();
  }
}
