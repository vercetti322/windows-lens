import './Setup.css';
import OllamaSetup from '../../components/OllamaSetup/OllamaSetup';
import GeminiSetup from '../../components/GeminiSetup/GeminiSetup';

function Setup() {
  return (
    <div className="setup">
      <div className="title">
        <img src="/windows-lens.svg" alt="Windows Lens logo" />
        <h1>Windows Lens</h1>
      </div>
      <p>
        Follow below steps to setup any of the Gemini or Ollama Model Providers
        for AI Completions.
      </p>
      <div className="providers">
        <OllamaSetup />
        <GeminiSetup />
      </div>
    </div>
  );
}

export default Setup;
