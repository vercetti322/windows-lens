import { BACKEND_URL, PROVIDERS } from "../../constants/api";

const checkProvider = async (body) => {
    try {
        const response = await fetch(BACKEND_URL + '/api/provider/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });

        const result = await response.text();
        return result === 'Ok';
    } catch (error) {
        console.error('Failed to check provider:', error);
        return false;
    }
};

const checkOllama = async (ollamaPort) => {
    return checkProvider({
        provider: PROVIDERS.OLLAMA,
        value: ollamaPort,
    });
};

const checkGemini = async (geminiApiKey) => {
    const encodedKey = btoa(
        String.fromCodePoint(...new TextEncoder().encode(geminiApiKey))
    );

    return checkProvider({
        provider: PROVIDERS.GEMINI,
        value: encodedKey,
    });
};

export const canClickContinue = (ollamaPort, geminiApiKey) => {
    return ollamaPort || geminiApiKey;
};

export const canContinue = async (ollamaPort, geminiApiKey, setPortStatus, setApiKeyStatus) => {
    const [geminiValid, ollamaValid] = await Promise.all([
        geminiApiKey ? checkGemini(geminiApiKey) : true,
        ollamaPort ? checkOllama(ollamaPort) : true,
    ]);

    setApiKeyStatus(geminiValid);
    setPortStatus(ollamaValid);

    return geminiValid && ollamaValid;
}

export const cancelWindow = () => {
    window.cefQuery({
        request: "quit",
        onSuccess: () => { },
        onFailure: (code, msg) => console.error(code, msg)
    });
}