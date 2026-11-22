export const HistoryWebSocket = (() => {
  const WS_PATH = "/ws/history";

  const INITIAL_RECONNECT_DELAY = 1000;
  const MAX_RECONNECT_DELAY = 30000;

  let socket = null;
  let reconnectTimer = null;
  let reconnectDelay = INITIAL_RECONNECT_DELAY;
  let manuallyDisconnected = false;
  let historyRefreshHandler = null;

  function getWebSocketUrl() {
    const protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
    return `${protocol}//${window.location.host}${WS_PATH}`;
  }

  function setHistoryRefreshHandler(handler) {
    historyRefreshHandler = handler;
  }

  function reloadHistory() {
    if (typeof historyRefreshHandler === "function") {
      historyRefreshHandler();
    }
  }

  function connect() {
    manuallyDisconnected = false;

    if (
      socket &&
      (socket.readyState === WebSocket.OPEN ||
        socket.readyState === WebSocket.CONNECTING)
    ) {
      return;
    }

    clearReconnectTimer();

    try {
      socket = new WebSocket(getWebSocketUrl());
    } catch {
      scheduleReconnect();
      return;
    }

    socket.onopen = handleOpen;
    socket.onmessage = handleMessage;
    socket.onclose = handleClose;
    socket.onerror = handleError;
  }

  function handleOpen() {
    reconnectDelay = INITIAL_RECONNECT_DELAY;
    reloadHistory();
  }

  function handleMessage(event) {
    let message;

    try {
      message = JSON.parse(event.data);
    } catch {
      return;
    }

    if (message.type === "HISTORY_UPDATED") {
      reloadHistory();
    }
  }

  function handleClose() {
    socket = null;

    if (!manuallyDisconnected) {
      scheduleReconnect();
    }
  }

  function handleError() {
    if (socket) {
      socket.close();
    }
  }

  function scheduleReconnect() {
    if (manuallyDisconnected || reconnectTimer !== null) {
      return;
    }

    reconnectTimer = setTimeout(() => {
      reconnectTimer = null;
      connect();

      reconnectDelay = Math.min(
        reconnectDelay * 2,
        MAX_RECONNECT_DELAY
      );
    }, reconnectDelay);
  }

  function clearReconnectTimer() {
    if (reconnectTimer !== null) {
      clearTimeout(reconnectTimer);
      reconnectTimer = null;
    }
  }

  function disconnect() {
    manuallyDisconnected = true;
    clearReconnectTimer();

    if (socket !== null) {
      socket.onclose = null;
      socket.onerror = null;
      socket.onmessage = null;
      socket.onopen = null;

      socket.close();
      socket = null;
    }
  }

  function isConnected() {
    return socket !== null &&
      socket.readyState === WebSocket.OPEN;
  }

  return {
    connect,
    disconnect,
    isConnected,
    setHistoryRefreshHandler
  };
})();
