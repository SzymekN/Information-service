<script setup>
defineProps({
  msg: {
    type: String,
    required: true
  }
})
</script>

<template>
  <router-link to="/">
    <div class="greetings">
      <h1 class="header_title">{{ msg }}</h1>
    </div>
  </router-link>
</template>

<style scoped>
h1 {
  font-size: 25px;
}

.greetings h1 {
  text-align: center;
  font-weight: 200;
}
</style>
