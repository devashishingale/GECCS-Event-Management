const API_BASE_URL = "http://localhost:8080/api";

export async function loginUser(collegeEmail, password) {

    const response = await fetch(
        `${API_BASE_URL}/auth/login`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                collegeEmail: collegeEmail,
                password: password
            })
        }
    );

    const data = await response.json();

    if (!response.ok) {
        throw new Error(
            data.message || "Login failed"
        );
    }

    return data;
}


export async function getEvents(token) {

    const response = await fetch(
        `${API_BASE_URL}/events`,
        {
            method: "GET",

            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        }
    );

    if (!response.ok) {
        throw new Error(
            `Failed to fetch events: ${response.status}`
        );
    }

    return response.json();
}