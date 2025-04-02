document.addEventListener("DOMContentLoaded", () => {

    
    async function testUrl(){
        const token = btoa('lilian@gmail.com:lilian123');
        console.log(token);
        const resultat = await fetch('http://localhost/travel-app-server/api/v1/register', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': `Basic ${token}`
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
    //testUrl();

    async function testTravels(){
        const resultat = await fetch("http://localhost/travel-app-server/api/v1/travels", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
        });
        const jsonResult = await resultat.json();
        console.log(jsonResult);
    }
    testTravels();

    async function testSearch(){
        // Example 1: Simple search
        const simpleSearch = encodeURIComponent('fuji');
        
        // Example 2: Multiple words with spaces
        const multiWordSearch = encodeURIComponent('beach resort');
        
        // Example 3: Special characters
        const specialCharsSearch = encodeURIComponent('côte d\'azur');
        
        // Example 4: Multiple keywords with special characters
        const complexSearch = encodeURIComponent('paris france eiffel');
        
        // Using the complex search as an example
        const baseUrl = 'http://localhost/travel-app-server/api/v1/search';
        const searchUrl = `${baseUrl}/${simpleSearch}`;
        
        console.log('Search URL:', searchUrl);
        
        const resultat = await fetch(searchUrl, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
        });
        const jsonResult = await resultat.json();
        console.log('Search Results:', jsonResult);
        const body = document.getElementById('body');
        body.textContent = JSON.stringify(jsonResult, null, 2);
    }
    testSearch();
});