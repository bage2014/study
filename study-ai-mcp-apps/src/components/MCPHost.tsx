import { useState, useEffect, useCallback, useRef } from 'react';
import { StreamableHTTPClientTransport } from '@modelcontextprotocol/sdk/client/streamableHttp';
import { Client } from '@modelcontextprotocol/sdk/client';
import { UIResourceRenderer } from '@mcp-ui/client';
import type { UIResource } from '@mcp-ui/client';

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

function MCPHost({ serverUrl, onConnect }: MCPHostProps) {
  const [connected, setConnected] = useState(false);
  const [tools, setTools] = useState<ToolInfo[]>([]);
  const [selectedTool, setSelectedTool] = useState<string | null>(null);
  const [toolParams, setToolParams] = useState<Record<string, unknown>>({});
  const [uiResource, setUiResource] = useState<UIResource | null>(null);
  const [results, setResults] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const clientRef = useRef<Client | null>(null);
  const transportRef = useRef<StreamableHTTPClientTransport | null>(null);

  useEffect(() => {
    let client: Client;
    let transport: StreamableHTTPClientTransport;

    const connect = async () => {
      try {
        setLoading(true);
        setError(null);
        
        console.log('Step 1: Creating transport with URL:', serverUrl);
        transport = new StreamableHTTPClientTransport(new URL(serverUrl), {
          sessionId: undefined,
        });
        
        console.log('Step 2: Creating client');
        client = new Client({ name: 'mcp-app-host', version: '1.0.0' });

        console.log('Step 3: Connecting client to transport');
        await client.connect(transport);
        console.log('Step 4: Client connected, transport:', client.transport);
        console.log('Step 5: Server capabilities:', client.getServerCapabilities());

        console.log('Step 6: Calling listTools');
        const toolList = await client.listTools();
        console.log('Step 7: Tool list received:', toolList);
        
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
        console.error('Error stack:', (err as Error).stack);
        setError((err as Error).message);
      } finally {
        setLoading(false);
      }
    };

    connect();

    return () => {
      transport?.close();
      client?.close();
    };
  }, [serverUrl, onConnect]);

  const handleToolCall = useCallback(async (toolName: string, params: Record<string, unknown>) => {
    if (!clientRef.current) return;

    try {
      setLoading(true);
      setError(null);

      const result = await clientRef.current.callTool({ name: toolName, arguments: params });
      
      setResults(prev => [...prev, `${toolName}: ${JSON.stringify(result.content)}`]);

      const resourceContent = result.content.find(
        (c: { type?: string }) => c.type === 'resource'
      );
      
      if (resourceContent && 'resource' in resourceContent) {
        const resource = resourceContent.resource;
        setUiResource({
          uri: resource.uri,
          mimeType: resource.mimeType,
          text: resource.text,
          blob: resource.blob,
        });
      } else {
        const textContent = result.content.find((c: { type?: string }) => c.type === 'text');
        if (textContent?.text) {
          setResults(prev => [...prev, `Result: ${textContent.text}`]);
        }
      }

    } catch (err) {
      console.error('Tool call error:', err);
      setError((err as Error).message);
    } finally {
      setLoading(false);
    }
  }, []);

  const handleUIAction = useCallback(async (action: {
    type: string;
    payload: {
      toolName: string;
      params: Record<string, unknown>;
    };
  }) => {
    console.log('UI Action:', action);
    
    if (action.type === 'tool') {
      await handleToolCall(action.payload.toolName, action.payload.params);
    }
  }, [handleToolCall]);

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

  const handleListTodos = () => {
    handleToolCall('listTodos', {});
  };

  const handleGetUI = () => {
    handleToolCall('getUI', {});
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
      <div style={styles.sidebar}>
        <h3 style={styles.sidebarTitle}>Tools</h3>
        <div style={styles.toolList}>
          <button
            style={styles.toolButton}
            onClick={handleGetUI}
          >
            📋 Open Todo UI
          </button>
          {tools.map(tool => (
            <button
              key={tool.name}
              style={{
                ...styles.toolButton,
                backgroundColor: selectedTool === tool.name ? '#667eea' : 'transparent',
              }}
              onClick={() => handleSelectTool(tool.name)}
            >
              {tool.title}
            </button>
          ))}
        </div>

        {selectedTool && (
          <div style={styles.toolDetails}>
            <h4 style={styles.toolDetailsTitle}>{selectedTool}</h4>
            <p style={styles.toolDescription}>
              {tools.find(t => t.name === selectedTool)?.description}
            </p>
            
            <div style={styles.paramsSection}>
              <h5 style={styles.paramsTitle}>Parameters:</h5>
              {(() => {
                const tool = tools.find(t => t.name === selectedTool);
                const properties = tool?.inputSchema?.['properties'] as Record<string, { type: string; description: string }> || {};
                
                return Object.entries(properties).map(([key, value]) => (
                  <div key={key} style={styles.paramRow}>
                    <label style={styles.paramLabel}>{key}</label>
                    <input
                      type="text"
                      placeholder={value.description}
                      style={styles.paramInput}
                      onChange={(e) => handleParamChange(key, e.target.value)}
                    />
                  </div>
                ));
              })()}
            </div>
            
            <button style={styles.executeButton} onClick={handleExecute}>
              Execute
            </button>
          </div>
        )}
      </div>

      <div style={styles.mainContent}>
        <div style={styles.resultsSection}>
          <h3 style={styles.sectionTitle}>Results</h3>
          <div style={styles.resultsList}>
            {results.map((result, index) => (
              <div key={index} style={styles.resultItem}>
                {result}
              </div>
            ))}
            {results.length === 0 && (
              <p style={styles.emptyResults}>Click "Open Todo UI" to view the interactive Todo List</p>
            )}
          </div>
        </div>

        {uiResource && (
          <div style={styles.uiSection}>
            <h3 style={styles.sectionTitle}>UI Preview</h3>
            <div style={styles.uiPreview}>
              <UIResourceRenderer
                resource={uiResource}
                onUIAction={handleUIAction}
                htmlProps={{
                  autoResizeIframe: true,
                }}
              />
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

const styles: { [key: string]: React.CSSProperties } = {
  hostContainer: {
    display: 'flex',
    gap: '20px',
    height: 'calc(100vh - 180px)',
  },
  sidebar: {
    width: '300px',
    background: 'rgba(255, 255, 255, 0.1)',
    borderRadius: '12px',
    padding: '20px',
    backdropFilter: 'blur(10px)',
    overflowY: 'auto',
  },
  sidebarTitle: {
    color: '#fff',
    fontSize: '18px',
    fontWeight: '600',
    margin: '0 0 16px 0',
    borderBottom: '1px solid rgba(255, 255, 255, 0.2)',
    paddingBottom: '12px',
  },
  toolList: {
    display: 'flex',
    flexDirection: 'column',
    gap: '8px',
    marginBottom: '20px',
  },
  toolButton: {
    padding: '12px',
    background: 'transparent',
    border: '1px solid rgba(255, 255, 255, 0.2)',
    borderRadius: '8px',
    color: '#fff',
    fontSize: '14px',
    cursor: 'pointer',
    textAlign: 'left',
    transition: 'all 0.2s',
  },
  toolDetails: {
    background: 'rgba(255, 255, 255, 0.05)',
    borderRadius: '8px',
    padding: '16px',
  },
  toolDetailsTitle: {
    color: '#fff',
    fontSize: '16px',
    fontWeight: '600',
    margin: '0 0 8px 0',
  },
  toolDescription: {
    color: 'rgba(255, 255, 255, 0.7)',
    fontSize: '13px',
    margin: '0 0 12px 0',
  },
  paramsSection: {
    marginBottom: '16px',
  },
  paramsTitle: {
    color: '#fff',
    fontSize: '14px',
    fontWeight: '500',
    margin: '0 0 8px 0',
  },
  paramRow: {
    display: 'flex',
    flexDirection: 'column',
    gap: '4px',
    marginBottom: '8px',
  },
  paramLabel: {
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: '12px',
  },
  paramInput: {
    padding: '8px',
    borderRadius: '6px',
    border: '1px solid rgba(255, 255, 255, 0.2)',
    background: 'rgba(255, 255, 255, 0.1)',
    color: '#fff',
    fontSize: '13px',
    outline: 'none',
  },
  executeButton: {
    width: '100%',
    padding: '10px',
    background: '#667eea',
    border: 'none',
    borderRadius: '8px',
    color: '#fff',
    fontSize: '14px',
    fontWeight: '600',
    cursor: 'pointer',
    transition: 'background 0.2s',
  },
  mainContent: {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    gap: '20px',
  },
  resultsSection: {
    flex: 1,
    background: 'rgba(255, 255, 255, 0.1)',
    borderRadius: '12px',
    padding: '20px',
    backdropFilter: 'blur(10px)',
    overflowY: 'auto',
  },
  uiSection: {
    flex: 2,
    background: 'rgba(255, 255, 255, 0.1)',
    borderRadius: '12px',
    padding: '20px',
    backdropFilter: 'blur(10px)',
    overflowY: 'auto',
  },
  sectionTitle: {
    color: '#fff',
    fontSize: '16px',
    fontWeight: '600',
    margin: '0 0 16px 0',
    borderBottom: '1px solid rgba(255, 255, 255, 0.2)',
    paddingBottom: '8px',
  },
  resultsList: {
    display: 'flex',
    flexDirection: 'column',
    gap: '8px',
  },
  resultItem: {
    background: 'rgba(255, 255, 255, 0.05)',
    padding: '12px',
    borderRadius: '8px',
    color: '#fff',
    fontSize: '13px',
    fontFamily: 'monospace',
  },
  emptyResults: {
    color: 'rgba(255, 255, 255, 0.5)',
    fontSize: '14px',
    textAlign: 'center',
    padding: '40px',
  },
  uiPreview: {
    width: '100%',
    minHeight: '400px',
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