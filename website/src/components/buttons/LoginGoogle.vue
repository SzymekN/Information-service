<script setup>
import { toast } from "vue-sonner";

const loginGoogle = async () =>{
    try{
    const url = '/client/login/google';
    const response = await fetch(url, {
      method: 'GET',
      headers: {
          'Content-Type': 'application/json',
      },
    });
    const res = await response.json();
    console.log(res);
    if (!response.ok) {
      toast.error(text)
    }
    else
      window.location.href = res.url;
    } catch (error) {
      console.log(error);
    }
  }
  
</script>
<template>
    <button class="login100-form-btn login200-form-btn" @click="loginGoogle()">
        <img src="../../../public/google_icon.png" height="48" width="48"/>
        Zaloguj się przez Google
    </button>
</template>
<style scoped>
@import '@/assets/login.css';

</style>