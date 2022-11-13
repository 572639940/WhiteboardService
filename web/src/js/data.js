const websocket = new WebSocket("ws://localhost:8080/chat")

export const saveToStorage = (key, data) => {
    localStorage.setItem(key, data)
    // websocket.send(data)
}

export const loadInStore = (key) => {
    let data;
    data = localStorage.getItem(key)
    // websocket.addEventListener('message', (event) => {
    //     console.log('Message from server ', event.data);
    //     data = event.data
    // });
    return data
}
