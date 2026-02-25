async function placeOrder() {

    const id = document.getElementById("orderId").value;

    if (!id) {
        alert("Enter Order ID");
        return;
    }

    try {
        const response = await fetch("/orders", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ orderId: id })
        });

        const text = await response.text();
        document.getElementById("result").innerText = text;

    } catch (err) {
        document.getElementById("result").innerText = "Error: " + err;
    }
}


async function checkStatus() {

    const id = document.getElementById("statusId").value;

    if (!id) {
        alert("Enter Order ID");
        return;
    }

    try {
        const response = await fetch(`/orders/${id}`);
        const text = await response.text();

        document.getElementById("result").innerText = text;

    } catch (err) {
        document.getElementById("result").innerText = "Error: " + err;
    }
}