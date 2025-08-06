export function createKrakenSocket(pairs, onData) {
  const ws = new WebSocket('wss://ws.kraken.com/v2');

  ws.onopen = () => {
    const subscribeMsg = {
      method: "subscribe",
      params: {
        channel: "ticker",
        symbol: pairs
      }
    };
    ws.send(JSON.stringify(subscribeMsg));
  };

  ws.onmessage = (event) => {
    const data = JSON.parse(event.data);
    if (Array.isArray(data) && data[1]?.price) {
      const symbol = data[3];
      const price = data[1].price;
      onData(symbol, parseFloat(price));
    }
  };

  return ws;
}
