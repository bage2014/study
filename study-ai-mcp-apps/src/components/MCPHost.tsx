import { useState, useEffect } from 'react';
import { UIResourceRenderer } from '@mcp-ui/client';
import type { UIResource } from '@mcp-ui/server';

interface MCPHostProps {
  serverUrl: string;
  onConnect: (connected: boolean) => void;
}

type CurrentView = 'welcome' | 'todo' | 'kanban' | 'family-login' | 'family-home' | 'family-manage' | 'family-tree' | 'member-manage' | 'relationship' | 'history' | 'profile' | 'album' | 'feed' | 'memorial' | 'stats';

function MCPHost({ serverUrl, onConnect }: MCPHostProps) {
  const [uiResource, setUiResource] = useState<UIResource | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [currentView, setCurrentView] = useState<CurrentView>('family-login');
  const [currentFamilyId, setCurrentFamilyId] = useState<string | undefined>(undefined);

  useEffect(() => {
    setLoading(true);
    setError(null);
    
    const testConnection = async () => {
      try {
        const apiUrl = serverUrl.replace('/mcp', '/api/call');
        await fetch(apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            toolName: 'listTodos',
            params: {},
          }),
        });
        
        setLoading(false);
        onConnect(true);
      } catch (err) {
        console.error('Connection error:', err);
        setLoading(false);
        setError((err as Error).message);
      }
    };
    
    testConnection();
  }, [serverUrl, onConnect]);

  const callToolDirectly = async (toolName: string, params: Record<string, unknown>) => {
    const apiUrl = serverUrl.replace('/mcp', '/api/call');
    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        toolName,
        params,
      }),
    });

    const data = await response.json();
    
    if (data.error) {
      throw new Error(data.error);
    }

    return data;
  };

  const handleToolCall = async (toolName: string, params: Record<string, unknown> = {}) => {
    try {
      setLoading(true);
      setError(null);

      const result = await callToolDirectly(toolName, params);
      
      const resourceContent = result.content.find(
        (c: { type?: string }) => c.type === 'resource'
      );
      
      if (resourceContent && 'resource' in resourceContent) {
        setUiResource({
          type: 'resource' as const,
          resource: resourceContent.resource,
        });
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
      } else if (event.data && event.data.type === 'navigate') {
          const { page, familyId, uri } = event.data;
          
          if (familyId) {
            setCurrentFamilyId(familyId);
          }
          
          if (uri) {
          const urlParams = new URLSearchParams(uri.split('?')[1] || '');
          const paramFamilyId = urlParams.get('familyId');
          const albumId = urlParams.get('albumId');
          
          if (uri.startsWith('ui://family/home')) {
            setCurrentView('family-home');
            handleToolCall('getHomeUI', {});
          } else if (uri.startsWith('ui://family/album')) {
            setCurrentView('album');
            handleToolCall('getAlbumUI', { familyId: paramFamilyId, albumId });
          } else if (uri.startsWith('ui://family/feed')) {
            setCurrentView('feed');
            handleToolCall('getFeedUI', { familyId: paramFamilyId });
          } else if (uri.startsWith('ui://family/memorial')) {
            setCurrentView('memorial');
            handleToolCall('getMemorialUI', { familyId: paramFamilyId });
          } else if (uri.startsWith('ui://family/stats')) {
            setCurrentView('stats');
            handleToolCall('getStatsUI', { familyId: paramFamilyId });
          } else if (uri.startsWith('ui://family/profile')) {
            setCurrentView('profile');
            handleToolCall('getProfileUI', {});
          }
          return;
        }
        
        switch (page) {
          case 'login':
            setCurrentView('family-login');
            handleToolCall('getLoginUI', {});
            break;
          case 'home':
            setCurrentView('family-home');
            handleToolCall('getHomeUI', {});
            break;
          case 'familyManage':
            setCurrentView('family-manage');
            handleToolCall('getFamilyManageUI', { familyId });
            break;
          case 'familyTree':
            setCurrentView('family-tree');
            handleToolCall('getFamilyTreeUI', { familyId });
            break;
          case 'memberManage':
            setCurrentView('member-manage');
            handleToolCall('getMemberManageUI', { familyId });
            break;
          case 'relationship':
            setCurrentView('relationship');
            handleToolCall('getRelationshipUI', { familyId });
            break;
          case 'history':
            setCurrentView('history');
            handleToolCall('getHistoryUI', { familyId });
            break;
          case 'profile':
            setCurrentView('profile');
            handleToolCall('getProfileUI', {});
            break;
          case 'album':
            setCurrentView('album');
            handleToolCall('getAlbumUI', { familyId });
            break;
          case 'feed':
            setCurrentView('feed');
            handleToolCall('getFeedUI', { familyId });
            break;
          case 'memorial':
            setCurrentView('memorial');
            handleToolCall('getMemorialUI', { familyId });
            break;
          case 'stats':
            setCurrentView('stats');
            handleToolCall('getStatsUI', { familyId });
            break;
          default:
            setCurrentView('family-login');
            handleToolCall('getLoginUI', {});
        }
      }
    };
    
    window.addEventListener('message', handleMessage);
    
    return () => {
      window.removeEventListener('message', handleMessage);
    };
  }, []);

  useEffect(() => {
    if (currentView === 'family-login') {
      handleToolCall('getLoginUI', {});
    }
  }, []);

  const handleGetUI = () => {
    setCurrentView('todo');
    handleToolCall('getUI', {});
  };

  const handleGetKanbanUI = () => {
    setCurrentView('kanban');
    handleToolCall('getKanbanUI', {});
  };

  const handleGetLoginUI = () => {
    setCurrentView('family-login');
    handleToolCall('getLoginUI', {});
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

  const isFamilyView = currentView.startsWith('family-') || 
                       ['member-manage', 'relationship', 'history', 'profile', 'album', 'feed', 'memorial', 'stats'].includes(currentView);

  return (
    <div style={styles.hostContainer}>
      {currentView !== 'welcome' && currentView !== 'family-login' && (
        <div style={styles.menuBar}>
          <div style={styles.menuLeft}>
            {isFamilyView ? (
              <button style={styles.menuBackButton} onClick={handleGetLoginUI}>
                ← 返回首页
              </button>
            ) : (
              <button style={styles.menuBackButton} onClick={handleBackToWelcome}>
                ← Back
              </button>
            )}
          </div>
          <div style={styles.menuCenter}>
            {isFamilyView ? (
              <>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'family-home' ? '#10B981' : 'transparent',
                    color: currentView === 'family-home' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('family-home'); handleToolCall('getHomeUI', {}); }}
                >
                  🏠 首页
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'family-tree' ? '#10B981' : 'transparent',
                    color: currentView === 'family-tree' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('family-tree'); handleToolCall('getFamilyTreeUI', { familyId: currentFamilyId }); }}
                >
                  🌳 家族树
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'family-manage' ? '#10B981' : 'transparent',
                    color: currentView === 'family-manage' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('family-manage'); handleToolCall('getFamilyManageUI', { familyId: currentFamilyId }); }}
                >
                  📋 家族管理
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'member-manage' ? '#10B981' : 'transparent',
                    color: currentView === 'member-manage' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('member-manage'); handleToolCall('getMemberManageUI', { familyId: currentFamilyId }); }}
                >
                  👥 成员管理
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'relationship' ? '#10B981' : 'transparent',
                    color: currentView === 'relationship' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('relationship'); handleToolCall('getRelationshipUI', { familyId: currentFamilyId }); }}
                >
                  🔗 关系管理
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'history' ? '#10B981' : 'transparent',
                    color: currentView === 'history' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('history'); handleToolCall('getHistoryUI', {}); }}
                >
                  📅 历史记录
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'album' ? '#10B981' : 'transparent',
                    color: currentView === 'album' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('album'); handleToolCall('getAlbumUI', {}); }}
                >
                  🖼️ 相册
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'feed' ? '#10B981' : 'transparent',
                    color: currentView === 'feed' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('feed'); handleToolCall('getFeedUI', {}); }}
                >
                  📱 动态
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'memorial' ? '#10B981' : 'transparent',
                    color: currentView === 'memorial' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('memorial'); handleToolCall('getMemorialUI', {}); }}
                >
                  🏛️ 纪念堂
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'stats' ? '#10B981' : 'transparent',
                    color: currentView === 'stats' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('stats'); handleToolCall('getStatsUI', {}); }}
                >
                  📊 统计
                </button>
                <button
                  style={{
                    ...styles.menuItem,
                    backgroundColor: currentView === 'profile' ? '#10B981' : 'transparent',
                    color: currentView === 'profile' ? '#fff' : 'rgba(255,255,255,0.8)',
                  }}
                  onClick={() => { setCurrentView('profile'); handleToolCall('getProfileUI', {}); }}
                >
                  👤 个人中心
                </button>
              </>
            ) : (
              <>
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
              </>
            )}
          </div>
          <div style={styles.menuRight}>
            <span style={styles.menuTitle}>
              {isFamilyView ? '家庭族谱' : 'MCP Apps'}
            </span>
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
          <p style={styles.welcomeSubtitle}>Interactive Applications</p>
          <div style={styles.welcomeButtons}>
            <button style={styles.welcomeButton} onClick={handleGetLoginUI}>
              👨‍👩‍👧‍👦 家庭族谱
            </button>
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
    flexWrap: 'wrap',
  },
  menuItem: {
    padding: '8px 16px',
    background: 'transparent',
    border: 'none',
    borderRadius: '6px',
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: '13px',
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
    flexWrap: 'wrap',
    justifyContent: 'center',
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
