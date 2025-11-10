const chatBox = document.getElementById('chat-box');
const userInput = document.getElementById('user-input');
const sendBtn = document.getElementById('send-btn');

sendBtn.addEventListener('click', sendMessage);
userInput.addEventListener('keypress', e => {
  if (e.key === 'Enter') sendMessage();
});

function sendMessage() {
  const question = userInput.value.trim();
  if (!question) return;

  // Display user message
  appendMessage('user', question);
  userInput.value = '';

  // Typing indicator
  const typing = appendMessage('bot', '💭 Typing...');

  // Call backend
  fetch('/api/ai/advice', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ question })
  })
  .then(res => res.json())
  .then(data => {
    typing.remove();
    if (data.reply) appendMessage('bot', formatResponse(data.reply));
    else appendMessage('bot', '⚠️ Something went wrong.');
  })
  .catch(() => {
    typing.remove();
    appendMessage('bot', '🚫 Network or server error. Please try again.');
  });
}

// Append chat message
function appendMessage(sender, text) {
  const msg = document.createElement('div');
  msg.classList.add(sender === 'user' ? 'user-message' : 'bot-message');
  msg.innerHTML = text;
  chatBox.appendChild(msg);
  chatBox.scrollTop = chatBox.scrollHeight;
  return msg;
}

// Format Gemini response: line breaks for bullets
function formatResponse(text) {
  return text
    .replace(/\*\*/g, '')       // remove bold markdown
    .replace(/\*/g, '•')        // bullets
    .replace(/\n/g, '<br>');    // line breaks
}
