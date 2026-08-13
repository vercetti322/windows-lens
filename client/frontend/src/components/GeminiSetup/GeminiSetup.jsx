import './GeminiSetup.css';

function GeminiSetup() {
  return (
    <div className="gemini-setup">
      <img src="/gemini.svg" alt="Gemini" className="gemini-logo" />
      <div className="gemini-content">
        <p>
          Create an API Key at{' '}
          <a
            href="https://aistudio.google.com/api-keys"
            target="_blank"
            rel="noopener noreferrer"
          >
            https://aistudio.google.com/api-keys
          </a>
          {''}
          , and paste it here: <input type="password" maxLength="60" />
        </p>
      </div>
    </div>
  );
}

export default GeminiSetup;
