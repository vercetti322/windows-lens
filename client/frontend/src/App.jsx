import { Route, Routes } from 'react-router-dom';
import Setup from './pages/Setup/Setup';
import Chat from './pages/Chat';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Setup />} />
      <Route path="/chat" element={<Chat />} />
    </Routes>
  );
}

export default App;
