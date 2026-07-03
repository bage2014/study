import { useState } from 'react'
import MCPHost from './components/MCPHost'

function App() {
  const [serverUrl, setServerUrl] = useState('http://localhost:3000/mcp')
  const [connected, setConnected] = useState(false)

  return (
    <div style={styles.container}>
      <MCPHost serverUrl={serverUrl} onConnect={setConnected} />
    </div>
  )
}

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    width: '100vw',
    height: '100vh',
    margin: '0',
    padding: '0',
    overflow: 'hidden',
  },
}

export default App
