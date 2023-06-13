<template>
    <button v-if="!jsCookie.get('ROLE')"><router-link :to="'/login'" ><a>Zaloguj/Zarejestruj</a></router-link></button>
    <button v-if="jsCookie.get('ROLE')"><router-link :to="'#'"  @click="logout()"><a>Wyloguj</a></router-link></button>
 
</template>
<script setup>
import jsCookie from "js-cookie";
import { toast } from "vue-sonner";

const logout = () => {
    fetch('/logout', {
    method: 'GET',
    credentials: 'same-origin',
  })
  .then(response => {
    // Handle the response from the server
    if (response.ok) {
      // Logout successful, do something
      jsCookie.remove('role');
      jsCookie.remove('ROLE');
      toast.success('Wylogowano pomyślnie');
      window.location.href = "/";
    } else {
      // Logout failed, do something
      toast.error('Wystąpił błąd podczas wylogowywania');
    }
  })
  .catch(error => {
    console.log(error);
    toast.error('Wystąpił błąd podczas wylogowywania');
  });
}

</script>
<style scoped>

button {
  /*background-color: rgba(240, 248, 255);*/
  border: none;
  font-size: 0.9rem;
  /*width: 100%;*/
  background-color: rgba(149, 206, 255, 0.2);
  border-radius: 50px;
  box-shadow: 2px 2px 20px -10px;
}

button:hover {
  background-color: #333333;

}

a {
  color: #666666;
  display: block;
  padding: 0.3rem;
}

a:hover{
  color: #fff;
}

</style>