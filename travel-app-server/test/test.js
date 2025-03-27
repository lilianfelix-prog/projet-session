document.addEventListener("DOMContentLoaded", () => {

    
    async function testUrl(){
        const token = btoa('alice@gmail.com:alice123');
        console.log(token);
        const resultat = await fetch('http://localhost/travel-app-server/api/v1/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });
        const jsonResult = await resultat.json();
        const body = document.getElementById('body');
        if (jsonResult.error) {
            body.textContent = JSON.stringify(jsonResult);
        } else {
            body.textContent = 'Login successful!';
        }
    }
    testUrl();

});