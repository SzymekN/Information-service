<script setup>
import list from "../data/nav.json"
</script>

<template>
  <table>
    <div class="buttons" v-for="item in list">
      <button><router-link :to="item.link" ><a class="aButton">{{item.text}}</a></router-link></button>
    </div>
  </table>

 
</template>


<style scoped>

.buttons {
  float: left;
  border-top: 0.1rem solid #8f8f8f;
  align-content: center;
}

.buttons button {
  background: linear-gradient(aliceblue, #ffffff);
  border: none;
  padding: 1rem;
  font-size: 1rem;
}

.buttons button:hover {
  background: linear-gradient(#d6e7ef, #ffffff);
  /*border-bottom: 1px solid #8f8f8f;*/
}
</style>