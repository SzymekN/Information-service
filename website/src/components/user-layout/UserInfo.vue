<script setup>
import jsCookie from 'js-cookie';
</script>

<template>
<div class="user-info">
  <h1>
    Twoje dane
  </h1>

  <span class="data-label">Imię: </span>
  <span>TEST</span>
  <br/>

  <span class="data-label">Nazwisko: </span>
  <span>TEST</span>
  <br/>

  <span class="data-label">Login: </span>
  <span>TEST</span>
  <br/>

  <span class="data-label">E-mail: </span>
  <span>TEST</span>
  <br/>

  <span class="data-label">Rola: </span>
  <span v-html="jsCookie.get('role')"></span>
  <br/>

  <button>
    Edytuj profil
  </button>

  <button class="button-del">
    Usuń profil
  </button>
</div>


</template>

<style>
span {
  margin: 3% 1%;
}

.user-info {
  margin-top: 2rem;
  background: white;
  width: fit-content;
  padding: 3%;
  border-radius: 10px;
  min-width: 60%;
  box-shadow: 1px 1px 20px -10px;
  margin-left: 20%;
}

.data-label {
  font-weight: 550;
}

.user-info button {
  margin: 5% 2% 2% 2%;
  padding: 1%;
  background: rgb(238, 246, 253);
  border: none;
  width: fit-content;
  font-size: 0.9rem;
  /*border-radius: 10px;*/
}

.user-info button:hover {
  background-color: #c8d8f1;
}

.user-info .button-del:hover {
  background-color: #ef4e70;
}
</style>