import { useState } from 'react'
import MCPHost from './components/MCPHost'

function App() {
  const [serverUrl, setServerUrl] = useState('http://localhost:3000/mcp')
  const [connected, setConnected] = useState(false)

  return (
    <div style={styles.container}>
      <header style={styles.header}>
        <h1 style={styles.title}>MCP Apps Host</h1>
        <p style={styles.subtitle}>Interactive UI Preview for MCP Server</p>
      </header>

      <div style={styles.content}>
        <div style={styles.settings}>
          <div style={styles.settingRow}>
            <label style={styles.label}>Server URL:</label>
            <input
              type="text"
              value={serverUrl}
              onChange={(e) => setServerUrl(e.target.value)}
              style={styles.input}
              disabled={connected}
            />
          </div>
        </div>

        <MCPHost serverUrl={serverUrl} onConnect={setConnected} />
      </div>

      <footer style={styles.footer}>
        <p>Powered by SEP-1865 MCP Apps</p>
      </footer>
    </div>
  )
}

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    minHeight: '100vh',
    background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)',
    padding: '20px',
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
  },
  header: {
    textAlign: 'center',
    marginBottom: '30px',
    padding: '20px',
  },
  title: {
    fontSize: '32px',
    fontWeight: '700',
    color: '#fff',
    margin: '0 0 8px 0',
  },
  subtitle: {
    fontSize: '16px',
    color: 'rgba(255, 255, 255, 0.7)',
    margin: '0',
  },
  content: {
    maxWidth: '900px',
    margin: '0 auto',
  },
  settings: {
    background: 'rgba(255, 255, 255, 0.1)',
    borderRadius: '12px',
    padding: '20px',
    marginBottom: '20px',
    backdropFilter: 'blur(10px)',
  },
  settingRow: {
    display: 'flex',
    alignItems: 'center',
    gap: '12px',
  },
  label: {
    color: '#fff',
    fontSize: '14px',
    fontWeight: '500',
    minWidth: '100px',
  },
  input: {
    flex: 1,
    padding: '10px 14px',
    borderRadius: '8px',
    border: '1px solid rgba(255, 255, 255, 0.2)',
    background: 'rgba(255, 255, 255, 0.1)',
    color: '#fff',
    fontSize: '14px',
    outline: 'none',
    transition: 'border-color 0.2s',
  },
  footer: {
    textAlign: 'center',
    marginTop: '40px',
    color: 'rgba(255, 255, 255, 0.5)',
    fontSize: '12px',
  },
}

export default App
