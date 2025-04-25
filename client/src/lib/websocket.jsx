import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient = null;

export function connectWebSocket(onMessage) {
    stompClient = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8082/ws'),
        reconnectDelay: 5000,
        onConnect: () => {
            console.log('✅ Connected to WebSocket');

            stompClient.subscribe('/topic/updates', (message) => {
                if (message.body) {
                    const data = JSON.parse(message.body);
                    onMessage(data);
                }
            });
        },
        onDisconnect: () => console.log('❌ Disconnected from WebSocket'),
        onStompError: (frame) => console.error('STOMP Error:', frame),
    });

    stompClient.activate();
}

export function sendEditMessage(message) {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: '/app/edit',
            body: JSON.stringify(message),
        });
    }
}
