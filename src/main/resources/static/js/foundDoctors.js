

(async () => {
    try {
        let response = await fetch("https://healthbox-8c741.firebaseio.com/");
        if (response.status < 400) {
            let data = await response.json();
            console.log(data);
        }
    } catch (e) {
        console.log(e.messageerror);
    }
})();