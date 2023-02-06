function selectHelper() {
    const select = document.querySelector('#action-select');
    const para = document.querySelector('p');

    select.addEventListener('change', setActionParams);

    function setActionParams() {
        const choice = select.value;

        if (choice === 'save') {
            para.innerHTML =
                '<label><input type="time" name="time-start" value="08:00"></label>' +
                '<label><input type="time" name="time-end" value="20:00"></label>';
        } else if (choice === 'delete') {
            para.textContent = 'Choose working days to delete.';
        } else {

            para.textContent = '';
        }
    }
}

function confirmBeforeSubmit(form) {
    const agree = confirm("Are you sure you wish to continue?");
    if (agree) {
        form.submit();
        return true;
    } else {
        form.cancel();
        return false;
    }
}