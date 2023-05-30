<script setup>
import { useRoute } from 'vue-router'
import { toast } from "vue-sonner";
import router from "@/router";

const route = useRoute();

const potwierdz = async () => {
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
    <button @click="potwierdz">Potwierdź</button>
</template>