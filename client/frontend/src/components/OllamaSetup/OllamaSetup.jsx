import './OllamaSetup.css';

function OllamaSetup({ port, onPortChange }) {
  return (
    <div className="ollama-setup">
      <img src="/ollama.svg" alt="Ollama" className="ollama-logo" />
      <div className="ollama-content">
        <p>
          Download Ollama from{' '}
          <a
            href="https://ollama.com/download/windows"
            target="_blank"
            rel="noopener noreferrer"
          >
            https://ollama.com/download/windows
          </a>
          {''}, and mention the port where it runs:{' '}
          <input
            type="text"
            value={port}
            onChange={(e) => onPortChange(e.target.value)}
            maxLength="5"
          />
        </p>
      </div>
    </div>
  );
}

export default OllamaSetup;
