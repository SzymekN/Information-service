<script setup>
defineProps({
  msg: {
    type: String,
    required: true
  }
})
</script>

<template>
  <!--TODO: multiple props to use msg and href in one or delete component and implement in Home.vue-->
  <div class="buttons">
    <button><a href="#" class="aButton">{{ msg }}</a></button>
  </div>
</template>


<style scoped>

.buttons {
  float: left;
  border-top: 1px solid #8f8f8f;
  align-content: center;
}

.buttons button {
  background: linear-gradient(aliceblue, #ffffff);
  border: none;
  padding: 0.8rem;
  font-size: 0.9rem;
}

.buttons:hover button {
  background: linear-gradient(#d6e7ef, #ffffff);
  font-size: 1rem;
}
</style>