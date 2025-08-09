import React, { useEffect, useRef, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
  CircularProgress,
  Typography,
} from "@mui/material";
import axios from "axios";

const TickerTable = ({ onTickersUpdate }) => {
  const [tickers, setTickers] = useState({});
  const [loading, setLoading] = useState(true);
  const ws = useRef(null);

  // Pares válidos (asegúrate de que existen en Kraken)
  const symbols = ["BTC/USD", "ETH/USD", "ADA/USD", "SOL/USD", "DOT/USD"];

  const fetchInitialData = async () => {
    try {
      const response = await axios.get(
        "https://api.kraken.com/0/public/Ticker",
        {
          params: { pair: symbols.join(",").replaceAll("/", "") },
        }
      );

      const result = {};
      const data = response.data.result;

      Object.keys(data).forEach((pair) => {
        const raw = data[pair];
        const symbol = symbols.find((s) => s.replace("/", "") === pair) || pair;

        result[symbol] = {
          last: parseFloat(raw.c[0]),
          ask: parseFloat(raw.a[0]),
          bid: parseFloat(raw.b[0]),
          high: parseFloat(raw.h[1]),
          low: parseFloat(raw.l[1]),
          change_pct: 0, // inicializado en 0
        };
      });

      setTickers(result);
      setLoading(false);
    } catch (error) {
      console.error("❌ Error fetching initial ticker data:", error);
    }
  };

  const connectWebSocket = () => {
    ws.current = new WebSocket("wss://ws.kraken.com/v2");

    ws.current.onopen = () => {
      console.log("✅ Connected to Kraken WebSocket");

      ws.current.send(
        JSON.stringify({
          method: "subscribe",
          params: {
            channel: "ticker",
            symbol: symbols,
          },
        })
      );
    };

    ws.current.onmessage = (event) => {
      const msg = JSON.parse(event.data);

      if (msg.channel === "status") {
        console.log("ℹ️ Status update:", msg);
      }

      if (
        msg.channel === "ticker" &&
        msg.type === "update" &&
        Array.isArray(msg.data)
      ) {
        const updates = {};

        msg.data.forEach((entry) => {
          if (entry.symbol && entry.last !== undefined) {
            updates[entry.symbol] = {
              last: parseFloat(entry.last),
              ask: parseFloat(entry.ask),
              bid: parseFloat(entry.bid),
              high: parseFloat(entry.high),
              low: parseFloat(entry.low),
              change_pct: parseFloat(entry.change_pct),
              volume: parseFloat(entry.volume),
            };
          }
        });

        setTickers((prev) => {
          return { ...prev, ...updates };
        });
      }

      if (msg.error) {
        console.error("❌ WebSocket error (server):", msg.error);
      }
    };

    ws.current.onerror = (error) => {
      console.error("🚨 WebSocket error (client):", error);
    };

    ws.current.onclose = () => {
      console.log("🔌 WebSocket connection closed");
    };
  };

  useEffect(() => {
    fetchInitialData();
    connectWebSocket();

    return () => {
      if (ws.current) {
        ws.current.close();
      }
    };
  }, []);

  // Aquí llamamos a onTickersUpdate solo después de actualizar tickers
  useEffect(() => {
    if (Object.keys(tickers).length > 0) {
      onTickersUpdate?.(tickers);
    }
  }, [tickers, onTickersUpdate]);

  return (
    <Paper style={{ padding: 20 }}>
      <Typography variant="h4" gutterBottom>
        Top 20 Crypto Prices (Live)
      </Typography>
      {loading ? (
        <CircularProgress />
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                <strong>Symbol</strong>
              </TableCell>
              <TableCell align="right">
                <strong>Last</strong>
              </TableCell>
              <TableCell align="right">
                <strong>Ask</strong>
              </TableCell>
              <TableCell align="right">
                <strong>Bid</strong>
              </TableCell>
              <TableCell align="right">
                <strong>High</strong>
              </TableCell>
              <TableCell align="right">
                <strong>Low</strong>
              </TableCell>
              <TableCell align="right">
                <strong>Change %</strong>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {symbols.map((symbol) => {
              const data = tickers[symbol];
              return (
                <TableRow key={symbol}>
                  <TableCell>{symbol}</TableCell>
                  <TableCell align="right">
                    ${data?.last?.toFixed(2) ?? "-"}
                  </TableCell>
                  <TableCell align="right">
                    ${data?.ask?.toFixed(2) ?? "-"}
                  </TableCell>
                  <TableCell align="right">
                    ${data?.bid?.toFixed(2) ?? "-"}
                  </TableCell>
                  <TableCell align="right">
                    ${data?.high?.toFixed(2) ?? "-"}
                  </TableCell>
                  <TableCell align="right">
                    ${data?.low?.toFixed(2) ?? "-"}
                  </TableCell>
                  <TableCell align="right">
                    {data?.change_pct?.toFixed(2) ?? "-"}%
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      )}
    </Paper>
  );
};

export default TickerTable;
