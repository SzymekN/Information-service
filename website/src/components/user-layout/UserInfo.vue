<script setup>
import jsCookie from 'js-cookie';
</script>

<template>

    <p v-html="jsCookie.get('role')"></p>

</template>