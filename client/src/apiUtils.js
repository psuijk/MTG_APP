// apiUtils.js
export const fetchWithAuth = (url, options = {}) => {
    const token = localStorage.getItem('authToken');
    console.log("Token being sent:", token);
    return fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
};

export const fetchPlayer = (username) => {
    const url = `http://localhost:8080/api/player/getplayer?username=${encodeURIComponent(username)}`;
    const token = localStorage.getItem('authToken');

    return fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.status === 200) {
            return response.json();
        } else {
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
    });
};



