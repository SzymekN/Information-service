<script setup>
import { useRoute } from 'vue-router'
import { toast } from "vue-sonner";
import router from "@/router";

const route = useRoute();

const confirm = async () => {
    var code = route.query["code"] 
    const response = await fetch(`/client/validate?code=${code}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
    });
    if (!response.ok) {
        const text = await response.text();
        toast.error(text)
    }
    else {
        toast.success("Konto zweryfikowane")
        router.push({ name: 'Home' })
    }
}

</script>
<template>
  <div class="validateCon">
    <h1>Dziękujemy za rejestrację!</h1>
    <p>Kliknij w poniższy przycisk by potwierdzić.</p>
    <button @click="confirm">Potwierdź</button>
  </div>
</template>

<style>
body {
  background: linear-gradient(to top,#add0ff, #e4f2ff);
}
.validateCon {
  background: white;
  border-radius: 10px;
  box-shadow: 5px 5px 20px -10px;
  width: 60%;
  margin-left: 20%;
  padding: 5%;
}

.validateCon button {
  margin-top: 5%;
  padding: 1% 2%;
  border: none;
  border-radius: 25px;
  background: rgba(149, 206, 255, 0.5);
  transition: all 0.4s;
  font-size: 0.9rem;
}

.validateCon button:hover {
  background-color: #333333;
  color: white;
}
</style>