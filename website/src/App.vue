<script setup>
import { onMounted, onUnmounted } from "vue";
import {RouterView} from "vue-router"
import jsCookie from "js-cookie";

onMounted(() => {
  if (jsCookie.get("ROLE") != undefined) {
    jsCookie.remove("ROLE");
  }
});

onUnmounted(() => {
  jsCookie.remove("ROLE");
});

</script>

<template>
  <RouterView />
</template>

<style>

</style>
