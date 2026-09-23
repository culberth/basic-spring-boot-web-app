const form = document.querySelector('#demo-form');
const slider = document.querySelector('#enthusiasm');
const sliderValue = document.querySelector('#slider-value');
const status = document.querySelector('#status');
const result = document.querySelector('#result');
const emptyState = document.querySelector('#empty-state');
const submitButton = document.querySelector('#submit');
let pendingRequest;

function setDefaultDate() {
    const now = new Date();
    document.querySelector('#date').value = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
}

setDefaultDate();
slider.addEventListener('input', () => { sliderValue.value = `${slider.value}%`; });

form.addEventListener('reset', () => {
    pendingRequest?.abort();
    pendingRequest = undefined;
    submitButton.disabled = false;
    result.replaceChildren();
    result.hidden = true;
    emptyState.hidden = false;
    status.textContent = '';
    status.className = '';
    // Reset default input values before updating the date and slider output.
    setTimeout(() => { setDefaultDate(); sliderValue.value = `${slider.value}%`; }, 0);
});

form.addEventListener('submit', async (event) => {
    event.preventDefault();
    const data = new FormData(form);
    const request = new AbortController();
    pendingRequest?.abort();
    pendingRequest = request;
    submitButton.disabled = true;
    status.className = '';
    status.textContent = 'Sending to Spring Boot…';
    result.hidden = true;
    emptyState.hidden = false;
    try {
        const response = await fetch('/api/demo', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            signal: request.signal,
            body: JSON.stringify({
                name: data.get('name').trim(),
                language: data.get('language'),
                enthusiasm: Number(data.get('enthusiasm')),
                date: data.get('date'),
                theme: data.get('theme'),
                notifications: data.has('notifications'),
                notes: data.get('notes')
            })
        });
        if (!response.ok) {
            throw new Error(response.status === 400
                ? 'Please check your entries. A name and a valid date are required.'
                : 'The server could not process the form. Please try again.');
        }
        const saved = await response.json();
        if (request.signal.aborted) return;
        const fields = [
            ['Name', saved.name], ['Favorite language', saved.language],
            ['Enthusiasm', `${saved.enthusiasm}%`], ['Date', saved.date],
            ['Theme preference', saved.theme],
            ['Notifications', saved.notifications ? 'Yes' : 'No'],
            ['Notes', saved.notes || 'No notes added.']
        ];
        result.replaceChildren();
        for (const [label, value] of fields) {
            const term = document.createElement('dt');
            const description = document.createElement('dd');
            term.textContent = label;
            description.textContent = value;
            result.append(term, description);
        }
        result.hidden = false;
        emptyState.hidden = true;
        status.textContent = 'Received! Your server response is ready.';
    } catch (error) {
        if (request.signal.aborted) return;
        status.className = 'error';
        status.textContent = error instanceof TypeError
            ? 'Could not reach the server. Check that the app is running and try again.'
            : error.message;
    } finally {
        if (pendingRequest === request) {
            submitButton.disabled = false;
            pendingRequest = undefined;
        }
    }
});
