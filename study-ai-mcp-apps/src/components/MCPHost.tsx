import { useState, useEffect, useCallback, useRef } from 'react';
import { StreamableHTTPClientTransport } from '@modelcontextprotocol/sdk/client/streamableHttp';
import { Client } from '@modelcontextprotocol/sdk/client';
import { UIResourceRenderer } from '@mcp-ui/client';
import type { UIResource } from '@mcp-ui/server';

interface MCPHostProps {
  serverUrl: string;
  onConnect: (connected: boolean) => void;
}

interface ToolInfo {
  name: string;
  title: string;
  description: string;
  inputSchema: object;
}

type CurrentView = 'welcome' | 'todo' | 'kanban';

function MCPHost({ serverUrl, onConnect }: MCPHostProps) {
  const [connected, setConnected] = useState(false);
  const [tools, setTools] = useState<ToolInfo[]>([]);
  const [selectedTool, setSelectedTool] = useState<string | null>(null);
  const [toolParams, setToolParams] = useState<Record<string, unknown>>({});
  const [uiResource, setUiResource] = useState<UIResource | null>(null);
  const [results, setResults] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [currentView, setCurrentView] = useState<CurrentView>('welcome');
  
  const clientRef = useRef<Client | null>(null);
  const transportRef = useRef<StreamableHTTPClientTransport | null>(null);

  useEffect(() => {
    const connect = async () => {
      if (clientRef.current) return;
      
      setLoading(true);
      setError(null);
      
      try {
        const transport = new StreamableHTTPClientTransport(new URL(serverUrl), {
          sessionId: undefined,
        });
        
        const client = new Client({ name: 'mcp-app-host', version: '1.0.0' });

        await client.connect(transport);
        
        const toolList = await client.listTools();
        setTools(toolList.tools.map(t => ({
          name: t.name,
          title: t.title || t.name,
          description: t.description || '',
          inputSchema: t.inputSchema || {},
        })));

        clientRef.current = client;
        transportRef.current = transport;
        setConnected(true);
        onConnect(true);
      } catch (err) {
        console.error('Connection error:', err);
        setError((err as Error).message);
      } finally {
        setLoading(false);
      }
    };

    connect();

    return () => {
      transportRef.current?.close();
      clientRef.current?.close();
      clientRef.current = null;
      transportRef.current = null;
    };
  }, [serverUrl, onConnect]);



  const callToolDirectly = async (toolName: string, params: Record<string, unknown>) => {
    const response = await fetch(serverUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json, text/event-stream',
      },
      body: JSON.stringify({
        jsonrpc: '2.0',
        id: Date.now(),
        method: 'tools/call',
        params: {
          name: toolName,
          arguments: params,
        },
      }),
    });

    const text = await response.text();
    
    let data;
    const dataMatch = text.match(/data:\s*(.+)/);
    if (dataMatch) {
      data = JSON.parse(dataMatch[1]);
    } else {
      data = JSON.parse(text);
    }
    
    if (data.error) {
      throw new Error(data.error.message);
    }

    return data.result;
  };

  const handleToolCall = async (toolName: string, params: Record<string, unknown>) => {
    try {
      setLoading(true);
      setError(null);

      const result = await callToolDirectly(toolName, params);
      
      setResults(prev => [...prev, `${toolName}: ${JSON.stringify(result.content)}`]);

      const resourceContent = result.content.find(
        (c: { type?: string }) => c.type === 'resource'
      );
      
      if (resourceContent && 'resource' in resourceContent) {
        setUiResource({
          type: 'resource' as const,
          resource: resourceContent.resource,
        });
      } else {
        const textContent = result.content.find((c: { type?: string }) => c.type === 'text');
        if (textContent?.text) {
          setResults(prev => [...prev, `Result: ${textContent.text}`]);
        }
      }

      return result;
    } catch (err) {
      console.error('Tool call error:', err);
      setError((err as Error).message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const handleMessage = async (event: MessageEvent) => {
      if (event.data && event.data.type === 'tool') {
        const { messageId, payload } = event.data;
        
        try {
          const response = await callToolDirectly(payload.toolName, payload.params);
          
          if (event.source) {
            event.source.postMessage({
              messageId: messageId,
              response: response,
            }, '*');
          }
        } catch (err) {
          if (event.source) {
            event.source.postMessage({
              messageId: messageId,
              error: (err as Error).message,
            }, '*');
          }
        }
      }
    };
    
    window.addEventListener('message', handleMessage);
    
    return () => {
      window.removeEventListener('message', handleMessage);
    };
  }, []);

  const handleUIAction = async (action: {
    type: string;
    payload: {
      toolName: string;
      params: Record<string, unknown>;
    };
  }) => {
    console.log('UI Action:', action);
    
    if (action.type === 'tool') {
      try {
        const response = await callToolDirectly(action.payload.toolName, action.payload.params);
        return response;
      } catch (err) {
        throw err;
      }
    }
  };

  const handleSelectTool = (toolName: string) => {
    setSelectedTool(toolName);
    setToolParams({});
  };

  const handleParamChange = (key: string, value: unknown) => {
    setToolParams(prev => ({ ...prev, [key]: value }));
  };

  const handleExecute = () => {
    if (selectedTool) {
      handleToolCall(selectedTool, toolParams);
    }
  };

  const handleGetUI = () => {
    setCurrentView('todo');
    handleToolCall('getUI', {});
  };

  const handleGetKanbanUI = () => {
    setCurrentView('kanban');
    handleToolCall('getKanbanUI', {});
  };

  const handleBackToWelcome = () => {
    setCurrentView('welcome');
    setUiResource(null);
  };

  if (loading) {
    return (
      <div style={styles.loadingContainer}>
        <div style={styles.loadingSpinner}></div>
        <p style={styles.loadingText}>Connecting to MCP Server...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div style={styles.errorContainer}>
        <p style={styles.errorText}>Error: {error}</p>
        <button style={styles.retryButton} onClick={() => window.location.reload()}>
          Retry
        </button>
      </div>
    );
  }

  return (
    <div style={styles.hostContainer}>
      {currentView !== 'welcome' && (
        <div style={styles.menuBar}>
          <div style={styles.menuLeft}>
            <button style={styles.menuBackButton} onClick={handleBackToWelcome}>
              ← Back
            </button>
          </div>
          <div style={styles.menuCenter}>
            <button
              style={{
                ...styles.menuItem,
                backgroundColor: currentView === 'kanban' ? '#667eea' : 'transparent',
                color: currentView === 'kanban' ? '#fff' : 'rgba(255,255,255,0.8)',
              }}
              onClick={handleGetKanbanUI}
            >
              📊 Kanban Board
            </button>
            <button
              style={{
                ...styles.menuItem,
                backgroundColor: currentView === 'todo' ? '#667eea' : 'transparent',
                color: currentView === 'todo' ? '#fff' : 'rgba(255,255,255,0.8)',
              }}
              onClick={handleGetUI}
            >
              📋 Todo List
            </button>
          </div>
          <div style={styles.menuRight}>
            <span style={styles.menuTitle}>MCP Apps</span>
          </div>
        </div>
      )}
      {uiResource ? (
        <div style={styles.uiSection}>
          <UIResourceRenderer
            resource={uiResource.resource}
            htmlProps={{
              autoResizeIframe: true,
            }}
          />
        </div>
      ) : (
        <div style={styles.welcomeContainer}>
          <h1 style={styles.welcomeTitle}>MCP Apps</h1>
          <p style={styles.welcomeSubtitle}>Interactive Project Management</p>
          <div style={styles.welcomeButtons}>
            <button style={styles.welcomeButton} onClick={handleGetKanbanUI}>
              📊 Open Kanban Board
            </button>
            <button style={styles.welcomeButton} onClick={handleGetUI}>
              📋 Open Todo UI
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

const styles: { [key: string]: React.CSSProperties } = {
  hostContainer: {
    display: 'flex',
    flexDirection: 'column',
    width: '100vw',
    height: '100vh',
    margin: '0',
    padding: '0',
    overflow: 'hidden',
    background: '#1a1a2e',
  },
  menuBar: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    height: '50px',
    background: 'rgba(0, 0, 0, 0.3)',
    backdropFilter: 'blur(10px)',
    padding: '0 20px',
    borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
  },
  menuLeft: {
    display: 'flex',
    alignItems: 'center',
    gap: '12px',
  },
  menuBackButton: {
    padding: '8px 16px',
    background: 'rgba(255, 255, 255, 0.1)',
    border: 'none',
    borderRadius: '6px',
    color: '#fff',
    fontSize: '14px',
    cursor: 'pointer',
    transition: 'background 0.2s',
  },
  menuCenter: {
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
  },
  menuItem: {
    padding: '8px 20px',
    background: 'transparent',
    border: 'none',
    borderRadius: '6px',
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: '14px',
    fontWeight: '500',
    cursor: 'pointer',
    transition: 'all 0.2s',
  },
  menuRight: {
    display: 'flex',
    alignItems: 'center',
  },
  menuTitle: {
    color: 'rgba(255, 255, 255, 0.6)',
    fontSize: '13px',
  },
  uiSection: {
    flex: 1,
    width: '100%',
    height: '100%',
    margin: '0',
    padding: '0',
    overflow: 'hidden',
  },
  welcomeContainer: {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    color: '#fff',
  },
  welcomeTitle: {
    fontSize: '36px',
    fontWeight: '700',
    margin: '0 0 12px 0',
  },
  welcomeSubtitle: {
    fontSize: '18px',
    color: 'rgba(255, 255, 255, 0.8)',
    margin: '0 0 40px 0',
  },
  welcomeButtons: {
    display: 'flex',
    gap: '16px',
  },
  welcomeButton: {
    padding: '16px 32px',
    background: 'rgba(255, 255, 255, 0.2)',
    border: '2px solid rgba(255, 255, 255, 0.4)',
    borderRadius: '12px',
    color: '#fff',
    fontSize: '16px',
    fontWeight: '600',
    cursor: 'pointer',
    transition: 'all 0.3s',
  },
  loadingContainer: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '60px',
  },
  loadingSpinner: {
    width: '48px',
    height: '48px',
    border: '4px solid rgba(255, 255, 255, 0.2)',
    borderTopColor: '#667eea',
    borderRadius: '50%',
    animation: 'spin 1s linear infinite',
  },
  loadingText: {
    color: '#fff',
    fontSize: '16px',
    marginTop: '20px',
  },
  errorContainer: {
    background: 'rgba(255, 107, 107, 0.2)',
    borderRadius: '12px',
    padding: '40px',
    textAlign: 'center',
  },
  errorText: {
    color: '#ff6b6b',
    fontSize: '16px',
    margin: '0 0 20px 0',
  },
  retryButton: {
    padding: '12px 24px',
    background: '#667eea',
    border: 'none',
    borderRadius: '8px',
    color: '#fff',
    fontSize: '14px',
    fontWeight: '600',
    cursor: 'pointer',
  },
};

export default MCPHost;