async function request(url, options = {}) {
  const res = await fetch(url, options)
  if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`)
  return res.json()
}

export function fetchLogs({ logType, page = 0, size = 20 } = {}) {
  const params = new URLSearchParams({ page, size })
  if (logType) params.set('logType', logType)
  return request(`/api/logs?${params}`)
}

export function fetchTrace(traceId) {
  return request(`/api/logs/trace/${encodeURIComponent(traceId)}`)
}

export function fetchSessions() {
  return request('/api/replay/sessions')
}

export function fetchSession(sessionId) {
  return fetch(`/api/replay/sessions/${encodeURIComponent(sessionId)}`)
    .then(res => {
      if (res.status === 404) return null
      if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`)
      return res.json()
    })
}

export function createSession(count) {
  return request('/api/replay/sessions', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ count })
  })
}

export function fetchLogCount() {
  return request('/api/logs?page=0&size=1')
}