import './Setup.css';
import OllamaSetup from '../../components/OllamaSetup/OllamaSetup';
import GeminiSetup from '../../components/GeminiSetup/GeminiSetup';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { canClickContinue, canContinue } from './SetupUtils';

function Setup() {
  const [ollamaPort, setOllamaPort] = useState('');
  const [geminiApiKey, setGeminiApiKey] = useState('');

  const [portStatus, setPortStatus] = useState(true);
  const [apiKeyStatus, setApiKeyStatus] = useState(true);

  const [isChecking, setIsChecking] = useState(false);
  const [toastKey, setToastKey] = useState(0);

  const navigate = useNavigate();

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
        <OllamaSetup
          port={ollamaPort}
          onPortChange={(value) => {
            setOllamaPort(value);
            setPortStatus(true);
            setApiKeyStatus(true);
          }}
        />
        <GeminiSetup
          apiKey={geminiApiKey}
          onApiKeyChange={(value) => {
            setGeminiApiKey(value);
            setApiKeyStatus(true);
            setPortStatus(true);
          }}
        />
      </div>
      <div className="buttons">
        <button className="cancel" type="button">
          Cancel
        </button>
        <button
          className="continue"
          type="submit"
          disabled={!canClickContinue(ollamaPort, geminiApiKey) || isChecking}
          onClick={async () => {
            setIsChecking(true);
            const valid = await canContinue(
              ollamaPort,
              geminiApiKey,
              setPortStatus,
              setApiKeyStatus,
            );

            if (valid) {
              navigate('/chat');
              return;
            }
            setToastKey((key) => key + 1);
            setIsChecking(false);
          }}
        >
          Continue with Chat
        </button>
      </div>
      {(!portStatus || !apiKeyStatus) && (
        <p key={toastKey} className="error-toast">
          <span className="error-icon">⚠ </span>
          {!portStatus && 'Invalid Ollama port'}
          {!portStatus && !apiKeyStatus && ' & '}
          {!apiKeyStatus && 'Invalid Gemini API key'}
        </p>
      )}
    </div>
  );
}

export default Setup;
