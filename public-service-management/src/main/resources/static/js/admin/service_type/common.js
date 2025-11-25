function confirmDelete(button) {
  const msg = button.dataset.msg;
  const serviceTypeId = button.dataset.serviceTypeId;
  if (confirm(msg)) {
    document.getElementById('deleteForm-' + serviceTypeId).submit();
  }
}
