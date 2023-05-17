<script setup>
import jsCookie from "js-cookie";
import list from "../../data/nav.json"
</script>

<template>
  <table>
    <div class="buttons" v-for="item in list">
      <button v-if="item.text != 'Konto' || (item.text == 'Konto' && jsCookie.get('ROLE'))"><router-link :to="item.link" ><a>{{item.text}}</a></router-link></button>
    </div>
  </table>
</template>


<style scoped>

.buttons {
  float: left;
  border-top: 0.1rem solid #8f8f8f;
  align-content: center;
  display: block;
}
.buttons button {
  background: linear-gradient(#ffffff, aliceblue);
  border: none;
  font-size: 1rem;
  border-radius: 0 0 10px 10px;
  display: block;
}

.buttons button:hover {
  background: rgba(51, 51, 51, 0.9);
  color: white;
}

a {
  display: block;
  padding: 0.3rem;
}

a:hover {
  font-weight: 500;
  color: #f2f2f2;
}

@media (max-width: 640px){
  .buttons button {
    font-size: 0.6rem;
  }
}
</style>